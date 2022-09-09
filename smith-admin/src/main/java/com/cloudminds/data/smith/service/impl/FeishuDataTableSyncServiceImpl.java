package com.cloudminds.data.smith.service.impl;


import cn.hutool.core.lang.Pair;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.cloudminds.data.smith.dao.entity.DataField;
import com.cloudminds.data.smith.dao.entity.DataTable;
import com.cloudminds.data.smith.dto.req.FieldPropertySaveReqDTO;
import com.cloudminds.data.smith.exception.ParameterException;
import com.cloudminds.data.smith.external.feishu.bitable.dto.FieldProperty;
import com.cloudminds.data.smith.external.feishu.bitable.dto.req.*;
import com.cloudminds.data.smith.external.feishu.bitable.dto.resp.BiFieldItemRespDTO;
import com.cloudminds.data.smith.external.feishu.bitable.dto.resp.BiRecordItemRespDTO;
import com.cloudminds.data.smith.external.feishu.bitable.dto.resp.BiTableItemRespDTO;
import com.cloudminds.data.smith.external.feishu.bitable.dto.resp.TableRecordSyncRespDTO;
import com.cloudminds.data.smith.external.feishu.bitable.service.BiTableApiService;
import com.cloudminds.data.smith.external.feishu.common.dto.Page;
import com.cloudminds.data.smith.external.feishu.common.dto.PageReqDTO;
import com.cloudminds.data.smith.service.DataTableSyncService;
import com.cloudminds.data.smith.util.Lists;
import com.cloudminds.data.smith.util.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 飞书多维表格同步
 *
 * @author Tao.Liu
 * @date 2022/6/30 15:24
 */
@Slf4j
@Service
public class FeishuDataTableSyncServiceImpl implements DataTableSyncService {

    @Value("${feishu.operation.appId}")
    private String appId;

    @Value("${feishu.operation.appSecret}")
    private String appSecret;

    /**
     * 飞书多维表格接口
     */
    private BiTableApiService biTableApiService;

    @PostConstruct
    public void init() {
        biTableApiService = new BiTableApiService(appId, appSecret);
    }

    @Override
    public String syncDataTable(final DataTable dataTable) {
        if (Strings.isNotBlank(dataTable.getAppTableId())) {
            // 不支持数据表格名称修改
            return dataTable.getAppTableId();
        }
        // 查询数据表名是否存在
        final Page<BiTableItemRespDTO> page = biTableApiService.pageTableList(dataTable.getAppId(), new PageReqDTO(100));
        if (page != null && Lists.isNotEmpty(page.getItems())) {
            // 查询存在指定表名
            final Optional<BiTableItemRespDTO> optional = page.getItems().stream()
                    .filter(e -> Strings.equals(dataTable.getTableName(), e.getName()))
                    .findFirst();
            if (optional.isPresent()) {
                return optional.get().getTableId();
            }
        }
        // 新增
        final BiTableItemRespDTO respDTO = biTableApiService.createTable(dataTable.getAppId(), dataTable.getTableName());
        return respDTO.getTableId();
    }

    @Override
    public boolean deleteDataTable(final DataTable dataTable) {
        if (Strings.isBlank(dataTable.getAppTableId())) {
            return false;
        }
        biTableApiService.deleteTable(dataTable.getAppId(), dataTable.getAppTableId());
        return true;
    }

    @Override
    public Map<Long, String> syncBatchDataField(final DataTable dataTable, final List<DataField> dataFields) {
        if (Lists.isEmpty(dataFields)) {
            return MapUtil.empty();
        }
        // 查询当前表格所有字段数据
        final List<BiFieldItemRespDTO> existFieldList = biTableApiService.getFieldList(dataTable.getAppId(), dataTable.getAppTableId());
        final Map<String, BiFieldItemRespDTO> existFieldMap = Lists.toMap(existFieldList, BiFieldItemRespDTO::getFieldName);

        final Map<Long, String> resultMap = new HashMap<>(dataFields.size());

        // 飞书表格中第一个字段列，飞书会生成一个默认列，且无法删除
        final BiFieldItemRespDTO existFirstItem = existFieldList.get(0);
        final boolean isDefaultField = Strings.equals("多行文本", existFirstItem.getFieldName()) && Objects.equals(1, existFirstItem.getType());

        // 循环更新字段
        for (int i = 0, size = dataFields.size(); i < size; i++) {
            try {
                final DataField dataField = dataFields.get(i);
                final BiFieldSaveBodyReqDTO saveBodyReqDTO = new BiFieldSaveBodyReqDTO();
                saveBodyReqDTO.setFieldName(dataField.getFieldName());
                saveBodyReqDTO.setType(dataField.getType());
                if (Strings.isNotBlank(dataField.getProperty()) && Strings.isNotEquals(dataField.getProperty(), "{}")) {
                    saveBodyReqDTO.setProperty(JSONUtil.toBean(dataField.getProperty(), FieldProperty.class));
                }
                final BiFieldItemRespDTO respDTO;
                if (isDefaultField && i == 0) {
                    // 修改第1列信息
                    final BiFieldSaveReqDTO saveReqDTO = new BiFieldSaveReqDTO(existFirstItem.getFieldId(), dataTable.getAppTableId(), saveBodyReqDTO);
                    respDTO = biTableApiService.updateField(dataTable.getAppId(), saveReqDTO);
                } else {
                    if (dataField.getAppFieldId() == null) {
                        // 以飞书表格中存在的字段进行更新
                        final BiFieldItemRespDTO existField = existFieldMap.get(dataField.getFieldName());
                        if (existField == null) {
                            // 创建新字段
                            final BiFieldSaveReqDTO saveReqDTO = new BiFieldSaveReqDTO(null, dataTable.getAppTableId(), saveBodyReqDTO);
                            respDTO = biTableApiService.createField(dataTable.getAppId(), saveReqDTO);
                        } else {
                            // 修改
                            final BiFieldSaveReqDTO saveReqDTO = new BiFieldSaveReqDTO(existField.getFieldId(), dataTable.getAppTableId(), saveBodyReqDTO);
                            respDTO = biTableApiService.updateField(dataTable.getAppId(), saveReqDTO);
                        }
                    } else {
                        // 以存在的字段ID为依据进行更新
                        final BiFieldSaveReqDTO saveReqDTO = new BiFieldSaveReqDTO(dataField.getAppFieldId(), dataTable.getAppTableId(), saveBodyReqDTO);
                        respDTO = biTableApiService.updateField(dataTable.getAppId(), saveReqDTO);
                    }
                }
                resultMap.put(dataField.getId(), respDTO.getFieldId());
            } catch (Exception e) {
                log.error("同步飞书表格字段出错", e);
            }
        }
        return resultMap;
    }

    @Override
    public boolean deleteField(final DataTable dataTable, final DataField dataField) {
        final boolean flag = biTableApiService.deleteField(dataTable.getAppId(), dataTable.getAppTableId(), dataField.getAppFieldId());
        return flag;
    }

    @Override
    public Integer insertRecords(final DataTable dataTable, final List<DataField> fieldList, final List<Map<String, Object>> recordList) {
        int count = 0;
        if (Lists.isEmpty(recordList)) {
            return count;
        }
        // 创建记录,飞书api每次最多100条，进数据进行分组
        final List<List<Map<String, Object>>> splitList = Lists.split(recordList, 100);
        for (final List<Map<String, Object>> itemList : splitList) {
            final List<BiRecordSaveItemReqDTO> saveItemReqDTOList = this.convertRecordList(fieldList, itemList);
            final TableRecordSaveReqDTO saveReqDTO = new TableRecordSaveReqDTO(saveItemReqDTOList);
            biTableApiService.insertBatchRecords(dataTable.getAppId(), dataTable.getAppTableId(), saveReqDTO);
            count += saveItemReqDTOList.size();
        }
        return count;
    }

    @Override
    public TableRecordSyncRespDTO syncRecords(final DataTable dataTable, final List<DataField> fieldList, final List<Map<String, Object>> recordList) {
        if (Lists.isEmpty(recordList)) {
            return new TableRecordSyncRespDTO();
        }
        // 获取记录主键信息
        final List<DataField> primaryFieldList = fieldList.stream().filter(DataField::getPrimaryFlag).collect(Collectors.toList());
        if (Lists.isEmpty(primaryFieldList)) {
            throw new ParameterException("数据表未配置主键信息");
        }
        // 记录转换
        final List<BiRecordSaveItemReqDTO> saveItemReqDTOList = this.convertRecordList(fieldList, recordList);

        // 同步记录较多，直接把飞书表格中的所有数据拿出来比较
        final Pair<List<BiRecordSaveItemReqDTO>, List<BiRecordSaveItemReqDTO>> pair = saveItemReqDTOList.size() > 100 ?
                this.buildMoreRecord(dataTable, saveItemReqDTOList, primaryFieldList) :
                this.buildSmallRecord(dataTable, saveItemReqDTOList, primaryFieldList);

        final List<BiRecordSaveItemReqDTO> recordInsertList = pair.getKey(), recordUpdateList = pair.getValue();
        // 创建记录,飞书api每次最多100条
        final List<List<BiRecordSaveItemReqDTO>> recordSplitInsertList = Lists.split(recordInsertList, 100);
        for (final List<BiRecordSaveItemReqDTO> itemReqDTOList : recordSplitInsertList) {
            final TableRecordSaveReqDTO saveReqDTO = new TableRecordSaveReqDTO(itemReqDTOList);
            biTableApiService.insertBatchRecords(dataTable.getAppId(), dataTable.getAppTableId(), saveReqDTO);
        }

        // 修改记录,飞书api每次最多100条
        final List<List<BiRecordSaveItemReqDTO>> recordSplitUpdateList = Lists.split(recordUpdateList, 100);
        for (final List<BiRecordSaveItemReqDTO> itemReqDTOList : recordSplitUpdateList) {
            final TableRecordSaveReqDTO saveReqDTO = new TableRecordSaveReqDTO(itemReqDTOList);
            biTableApiService.updateBatchRecords(dataTable.getAppId(), dataTable.getAppTableId(), saveReqDTO);
        }
        return new TableRecordSyncRespDTO(recordInsertList.size(), recordUpdateList.size(), 0);
    }

    /**
     * 构建少量记录
     * 少量记录时，循环一条条与飞书表格中的记录进行比对
     *
     * @param itemReqDTOList
     */
    private Pair<List<BiRecordSaveItemReqDTO>, List<BiRecordSaveItemReqDTO>> buildSmallRecord(final DataTable dataTable,
                                                                                              final List<BiRecordSaveItemReqDTO> itemReqDTOList,
                                                                                              final List<DataField> primaryFieldList) {
        final List<BiRecordSaveItemReqDTO> recordInsertList = new ArrayList<>(), recordUpdateList = new ArrayList<>();
        for (final BiRecordSaveItemReqDTO itemReqDTO : itemReqDTOList) {
            // 主键条件拼接与过滤
            final List<String> conditionList = new ArrayList<>();
            for (final DataField primaryField : primaryFieldList) {
                // 查询记录是否存在
                final Object primaryValue = itemReqDTO.getFields().get(primaryField.getFieldName());
                if (primaryValue == null) {
                    break;
                }
                if (primaryValue instanceof Number) {
                    conditionList.add(String.format("CurrentValue.[%s]=%d", primaryField.getFieldName(), primaryValue));
                } else {
                    conditionList.add(String.format("CurrentValue.[%s]=\"%s\"", primaryField.getFieldName(), primaryValue));
                }
            }
            if (!Objects.equals(conditionList.size(), primaryFieldList.size())) {
                log.warn("主键值缺失，忽略同步记录", JSONUtil.toJsonStr(itemReqDTO));
                continue;
            }
            final String filter = String.format("AND(%s)", Strings.join(",", conditionList));
            final BiTableRecordQueryReqDTO queryReqDTO = new BiTableRecordQueryReqDTO();
            queryReqDTO.setPageSize(2);
            queryReqDTO.setFilter(filter);
            final Page<BiRecordItemRespDTO> existPage = biTableApiService.pageRecordList(dataTable.getAppId(), dataTable.getAppTableId(), queryReqDTO);
            if (existPage == null || Lists.isEmpty(existPage.getItems())) {
                // 新增
                recordInsertList.add(itemReqDTO);
            } else {
                // 更新
                itemReqDTO.setRecordId(existPage.getItems().get(0).getRecordId());
                recordUpdateList.add(itemReqDTO);
            }
        }
        return Pair.of(recordInsertList, recordUpdateList);
    }

    /**
     * 构建较多记录
     * 记录较多时，直接将飞书表格中的记录全部拉出来，在程序中进行比对记录
     *
     * @param itemReqDTOList
     */
    private Pair<List<BiRecordSaveItemReqDTO>, List<BiRecordSaveItemReqDTO>> buildMoreRecord(final DataTable dataTable,
                                                                                             final List<BiRecordSaveItemReqDTO> itemReqDTOList,
                                                                                             final List<DataField> primaryFieldList) {
        final List<BiRecordItemRespDTO> existRecordList = new ArrayList<>();
        final List<String> fieldNames = primaryFieldList.stream().map(e -> String.format("\"%s\"", e.getFieldName())).collect(Collectors.toList());
        // 先查询所有表格中的记录
        final BiTableRecordQueryReqDTO queryReqDTO = new BiTableRecordQueryReqDTO();
        // 主查询出主键字段信息
        queryReqDTO.setFieldNames(String.format("[%s]", Strings.join(",", fieldNames)));
        queryReqDTO.setPageSize(100);
        boolean hasMore;
        do {
            final Page<BiRecordItemRespDTO> existPage = biTableApiService.pageRecordList(dataTable.getAppId(), dataTable.getAppTableId(), queryReqDTO);
            if (existPage == null || Lists.isEmpty(existPage.getItems())) {
                break;
            }
            existRecordList.addAll(existPage.getItems());
            queryReqDTO.setPageToken(existPage.getPageToken());
            hasMore = Boolean.TRUE.equals(existPage.getHasMore()) && Strings.isNotBlank(existPage.getPageToken());
        } while (hasMore);
        // 按primary字段转换
        final Map<String, String> existRecordMap = existRecordList.stream()
                .collect(Collectors.toMap(e -> this.convertPrimaryKey(primaryFieldList, e.getFields()), BiRecordItemRespDTO::getRecordId, (k1, k2) -> k1));
        // 清空原始记录
        existRecordList.clear();

        final List<BiRecordSaveItemReqDTO> recordInsertList = new ArrayList<>(), recordUpdateList = new ArrayList<>();
        itemReqDTOList.forEach(itemReqDTO -> {
            final String primaryKey = this.convertPrimaryKey(primaryFieldList, itemReqDTO.getFields());
            final String recordId = existRecordMap.get(primaryKey);
            if (Strings.isEmpty(recordId)) {
                // 新增
                recordInsertList.add(itemReqDTO);
            } else {
                // 更新
                itemReqDTO.setRecordId(recordId);
                recordUpdateList.add(itemReqDTO);
            }
        });
        return Pair.of(recordInsertList, recordUpdateList);
    }

    /**
     * 主键转换
     *
     * @param primaryFieldList
     * @param fields
     * @return
     */
    private String convertPrimaryKey(final List<DataField> primaryFieldList, Map<String, Object> fields) {
        final StringBuilder stringBuilder = new StringBuilder();
        primaryFieldList.forEach(primary -> {
            stringBuilder.append(Strings.toString(fields.get(primary.getFieldName())));
        });
        return stringBuilder.toString();
    }

    /**
     * 记录值转换
     *
     * @param fieldList
     * @param originRecordList
     * @return
     */
    private List<BiRecordSaveItemReqDTO> convertRecordList(final List<DataField> fieldList,
                                                           final List<Map<String, Object>> originRecordList) {
        final Map<String, DataField> dataFieldMap = Lists.toMap(fieldList, DataField::getFieldKey);
        // 选项信息
        final Map<String, FieldPropertySaveReqDTO.Option> fieldOptionMap = new HashMap<>(8);
        fieldList.stream().filter(e -> Strings.isNotBlank(e.getProperty())).forEach(e -> {
            final String fieldKey = e.getFieldKey();
            final FieldPropertySaveReqDTO fieldProperty = JSONUtil.toBean(e.getProperty(), FieldPropertySaveReqDTO.class);
            if (Lists.isNotEmpty(fieldProperty.getOptions())) {
                fieldProperty.getOptions().forEach(option -> {
                    if (Strings.isNotBlank(option.getValue())) {
                        fieldOptionMap.put(fieldKey + "-" + option.getValue(), option);
                    }
                });
            }
        });
        // 飞书单选与多选，需要进行值转换
        final List<Integer> selectTypes = Lists.asList(3, 4);
        // 数据转换
        final List<BiRecordSaveItemReqDTO> recordList = new ArrayList<>();
        for (final Map<String, Object> tempRecord : originRecordList) {
            final Map<String, Object> record = new HashMap<>(tempRecord.size());
            tempRecord.entrySet().stream().forEach(entry -> {
                final DataField dataField = dataFieldMap.get(entry.getKey());
                if (entry.getValue() == null || dataField == null) {
                    return;
                }
                record.put(dataField.getFieldName(), entry.getValue());
                if (Objects.equals(dataField.getType(), 1)) {
                    // 转换为字符串
                    record.put(dataField.getFieldName(), Strings.toString(entry.getValue()));
                } else if (selectTypes.contains(dataField.getType())) {
                    // 选项值转换
                    final FieldPropertySaveReqDTO.Option option = fieldOptionMap.get(dataField.getFieldKey() + "-" + entry.getValue());
                    if (option != null) {
                        record.put(dataField.getFieldName(), option.getName());
                    }
                }

            });
            recordList.add(new BiRecordSaveItemReqDTO(record));
        }

        return recordList;
    }

}
