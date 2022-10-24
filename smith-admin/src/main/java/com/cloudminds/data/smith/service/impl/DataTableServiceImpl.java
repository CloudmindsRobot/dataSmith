package com.cloudminds.data.smith.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.cloudminds.data.smith.constant.DataStatusEnum;
import com.cloudminds.data.smith.constant.DataSyncStatusEnum;
import com.cloudminds.data.smith.dao.entity.DataField;
import com.cloudminds.data.smith.dao.entity.DataModel;
import com.cloudminds.data.smith.dao.entity.DataTable;
import com.cloudminds.data.smith.dao.mapper.DataFieldMapper;
import com.cloudminds.data.smith.dao.mapper.DataModelMapper;
import com.cloudminds.data.smith.dao.mapper.DataTableMapper;
import com.cloudminds.data.smith.dto.PageResult;
import com.cloudminds.data.smith.dto.event.DataTableChangeEvent;
import com.cloudminds.data.smith.dto.req.DataFieldSaveReqDTO;
import com.cloudminds.data.smith.dto.req.DataTableQueryReqDTO;
import com.cloudminds.data.smith.dto.req.DataTableSaveReqDTO;
import com.cloudminds.data.smith.dto.resp.DataFieldSyncRespDTO;
import com.cloudminds.data.smith.dto.resp.DataTableDetailRespDTO;
import com.cloudminds.data.smith.dto.resp.DataTableItemRespDTO;
import com.cloudminds.data.smith.exception.ParameterException;
import com.cloudminds.data.smith.service.DataTableService;
import com.cloudminds.data.smith.service.DataTableSyncService;
import com.cloudminds.data.smith.service.JobExecutorService;
import com.cloudminds.data.smith.util.Check;
import com.cloudminds.data.smith.util.Lists;
import com.cloudminds.data.smith.util.Springs;
import com.cloudminds.data.smith.util.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 数据表格操作接口实现
 *
 * @author Tao.Liu
 * @date 2022/6/29 17:08
 */
@Slf4j
@Service
public class DataTableServiceImpl implements DataTableService {

    @Resource
    private DataTableMapper dataTableMapper;
    @Resource
    private DataFieldMapper dataFieldMapper;
    @Resource
    private DataModelMapper dataModelMapper;
    @Resource
    private DataTableSyncService dataTableSyncService;
    @Resource
    private JobExecutorService jobExecutorService;

    @Override
    public PageResult<DataTableItemRespDTO> findPageList(final DataTableQueryReqDTO queryReqDTO) {
        final PageResult<DataTable> pageResult = dataTableMapper.selectPageList(queryReqDTO);
        if (pageResult == null || Lists.isEmpty(pageResult.getRecords())) {
            return PageResult.empty();
        }
        final List<Integer> modelIdList = Lists.toList(pageResult.getRecords(), DataTable::getDataModelId);
        final List<DataModel> dataModelList = dataModelMapper.selectBatchIds(modelIdList);
        final Map<Integer, DataModel> dataModelMap = Lists.toMap(dataModelList, DataModel::getId);

        return pageResult.convert(e -> {
            final DataTableItemRespDTO itemRespDTO = BeanUtil.copyProperties(e, DataTableItemRespDTO.class);
            final DataModel dataModel = dataModelMap.get(itemRespDTO.getDataModelId());
            if (dataModel != null) {
                itemRespDTO.setDataModelName(dataModel.getName());
            }
            return itemRespDTO;
        });
    }

    @Override
    public DataTableDetailRespDTO findDetailById(final Long tableId) {
        final DataTable table = dataTableMapper.selectById(tableId);
        if (table == null || Objects.equals(table.getStatus(), DataStatusEnum.DELETE.getValue())) {
            throw new ParameterException("无效的表格ID");
        }
        final DataTableDetailRespDTO detailRespDTO = BeanUtil.copyProperties(table, DataTableDetailRespDTO.class);

        final List<DataField> fieldList = dataFieldMapper.findListByTableId(tableId);
        final List<DataFieldSaveReqDTO> detailFieldList = BeanUtil.copyToList(fieldList, DataFieldSaveReqDTO.class);

        detailRespDTO.setFields(detailFieldList);
        return detailRespDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveDataTable(final DataTableSaveReqDTO saveReqDTO) {
        final DataTable existTable = dataTableMapper.findByNameAndAppId(saveReqDTO.getAppId(), saveReqDTO.getTableName());

        final DataTable saveTable = BeanUtil.copyProperties(saveReqDTO, DataTable.class);
        saveTable.setUpdateTime(System.currentTimeMillis());
        saveTable.setSyncStatus(DataSyncStatusEnum.PENDING.getValue());
        if (saveTable.getId() == null) {
            // 新增
            Check.isTrue(existTable == null, "应用已存在相同表格名称，请修改名称后提交");
            saveTable.setStatus(DataStatusEnum.NORMAL.getValue());
            saveTable.setCreateTime(System.currentTimeMillis());
            dataTableMapper.insert(saveTable);
        } else {
            // 修改
            final boolean notExistFlag = existTable == null || Objects.equals(saveTable.getId(), existTable.getId());
            Check.isTrue(notExistFlag, "应用已存在相同表格名称，请修改名称后提交");
            dataTableMapper.updateById(saveTable);
        }

        // 批量保存字段信息
        this.saveBatchFields(saveTable.getId(), saveReqDTO.getFields());
        // 同步数据表格
        Springs.publishEvent(new DataTableChangeEvent(saveTable.getId()));
        return saveTable.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(final Long dataTableId) {
        Check.notNull(dataTableId, "表格ID不能为空");
        dataTableMapper.updateDeleteStatus(dataTableId);
        dataFieldMapper.updateDeleteStatusByTableId(dataTableId);

        // 同步数据表格
        Springs.publishEvent(new DataTableChangeEvent(dataTableId));
    }

    @Override
    public void deleteFields(final Long dataTableId, final List<Long> fieldIds) {
        dataFieldMapper.updateDeleteStatusByIds(fieldIds);
        Springs.publishEvent(new DataTableChangeEvent(dataTableId));
    }

    @Override
    @Async
    @TransactionalEventListener(fallbackExecution = true)
    public void notifyTableChangeEvent(final DataTableChangeEvent changeEvent) {
        this.syncDataTable(changeEvent.getTableId());
        this.syncDataFields(changeEvent.getTableId());
    }

    /**
     * 批量保存字段信息
     * 主键标识判定，可以不设置主键，如果没有主键，则每次都是全量新增数据
     *
     * @param tableId
     * @param fields
     * @return
     */
    private List<Long> saveBatchFields(final Long tableId, final List<DataFieldSaveReqDTO> fields) {
        if (Lists.isEmpty(fields)) {
            return new ArrayList<>();
        }

        // 判断是否有重复的名称
        this.checkDuplicateFieldName(tableId, fields);

        final long now = System.currentTimeMillis();
        final List<Long> dataFieldIds = new ArrayList<>();
        for (final DataFieldSaveReqDTO saveReqDTO : fields) {
            final DataField saveDataField = BeanUtil.copyProperties(saveReqDTO, DataField.class);
            saveDataField.setSyncStatus(DataSyncStatusEnum.PENDING.getValue());
            saveDataField.setUpdateTime(now);
            if (saveDataField.getId() == null) {
                saveDataField.setTableId(tableId);
                saveDataField.setStatus(DataStatusEnum.NORMAL.getValue());
                saveDataField.setCreateTime(now);
                dataFieldMapper.insert(saveDataField);
            } else {
                dataFieldMapper.updateById(saveDataField);
            }
            dataFieldIds.add(saveDataField.getId());
        }
        return dataFieldIds;
    }

    /**
     * 检测重复字段
     *
     * @param tableId
     * @param fields
     */
    private void checkDuplicateFieldName(final Long tableId, final List<DataFieldSaveReqDTO> fields) {
        final List<DataField> dataFields = dataFieldMapper.findListByTableId(tableId);
        final Map<String, Long> existNameMap = dataFields.stream().collect(Collectors.toMap(e -> e.getFieldName(), e -> e.getId()));
        for (final DataFieldSaveReqDTO field : fields) {
            final Long saveFieldId = field.getId() == null ? 0L : field.getId();
            final String saveFieldName = field.getFieldName();
            final Long existFieldId = existNameMap.get(saveFieldName);
            if (existFieldId == null || Objects.equals(existFieldId, saveFieldId)) {
                existNameMap.put(saveFieldName, saveFieldId);
                continue;
            }
            throw new ParameterException("字段名【%s】重复，请检查后提交", saveFieldName);
        }
    }

    /**
     * 同步表格
     *
     * @param dataTableId
     */
    private void syncDataTable(final Long dataTableId) {
        final DataTable dataTable = dataTableMapper.selectById(dataTableId);
        if (dataTable == null || Objects.equals(dataTable.getSyncStatus(), DataSyncStatusEnum.SUCCESS.getValue())) {
            return;
        }
        if (Objects.equals(dataTable.getStatus(), DataStatusEnum.DELETE.getValue())) {
            // 移除执行任务
            jobExecutorService.removeSchedule(dataTableId);
            // 删除记录
            dataTableMapper.deleteById(dataTable.getId());
            // 删除表格, 不同步删除飞书表格
            // dataTableSyncService.deleteDataTable(dataTable);
            log.info("【同步删除表格】tableId={}, appTableId={}, tableName={}", dataTable.getId(), dataTable.getAppTableId(), dataTable.getTableName());
            return;
        }
        // 更新同步状态
        final DataTable updateDataTable = new DataTable();
        updateDataTable.setId(dataTable.getId());

        try {
            final String appTableId = dataTableSyncService.syncDataTable(dataTable);
            updateDataTable.setAppTableId(appTableId);
            updateDataTable.setSyncStatus(DataSyncStatusEnum.SUCCESS.getValue());
            updateDataTable.setSyncCause("");
        } catch (Exception e) {
            log.error("同步数据表格出错", e);
            final String cause = e.getMessage();
            updateDataTable.setSyncStatus(DataSyncStatusEnum.FAIL.getValue());
            updateDataTable.setSyncCause(Strings.maxLength(cause, 180));
        }
        updateDataTable.setUpdateTime(System.currentTimeMillis());
        dataTableMapper.updateById(updateDataTable);
        log.info("【同步表格】tableId={},  tableName={}", dataTable.getId(), dataTable.getTableName());

        // 更新执行任务队列
        jobExecutorService.addSchedule(dataTable.getId(), dataTable.getCron());
        return;
    }

    /**
     * 同步表格字段
     *
     * @param dataTableId
     */
    private void syncDataFields(final Long dataTableId) {
        // 查询未同步的字段列表
        final List<DataField> dataFields = dataFieldMapper.findUnSyncListByTableId(dataTableId);
        // 查询表格
        if (Lists.isEmpty(dataFields)) {
            return;
        }
        final DataTable dataTable = dataTableMapper.selectById(dataTableId);

        // 如果未查询出表格数据，说明表格已经被删除，直接将字段从数据库清除
        if (dataTable == null) {
            // 直接删除未同步的表格字段
            dataFieldMapper.deleteUnSyncListByTableId(dataTableId);
            log.info("【同步批量删除字段】tableId={}", dataTableId);
            return;
        }

        // 更新列表
        final List<DataField> dataFieldSyncList = new ArrayList<>();
        for (final DataField dataField : dataFields) {
            if (Objects.equals(dataField.getStatus(), DataStatusEnum.DELETE.getValue())) {
                dataTableSyncService.deleteField(dataTable, dataField);
                // 删除记录
                dataFieldMapper.deleteById(dataField.getId());
                log.info("【同步删除字段】fieldId={}, appFieldId={}, fieldName={}", dataField.getId(), dataField.getAppFieldId(), dataField.getFieldName());
            } else {
                dataFieldSyncList.add(dataField);
            }
        }
        final List<DataFieldSyncRespDTO> syncRespDTOList = dataTableSyncService.syncBatchDataField(dataTable, dataFieldSyncList);
        syncRespDTOList.forEach(syncRespDTO -> {
            // 更新为已同步
            final DataField dataField = new DataField();
            dataField.setId(syncRespDTO.getFieldId());
            dataField.setAppFieldId(syncRespDTO.getAppFieldId());
            dataField.setUpdateTime(System.currentTimeMillis());
            if (Boolean.TRUE.equals(syncRespDTO.getFlag())) {
                dataField.setSyncStatus(DataSyncStatusEnum.SUCCESS.getValue());
            } else {
                dataField.setSyncStatus(DataSyncStatusEnum.FAIL.getValue());
                dataField.setSyncCause(Strings.maxLength(syncRespDTO.getCause(), 180));
            }
            dataFieldMapper.updateById(dataField);
            log.info("【同步字段】fieldId={}, appFieldId={}", dataField.getId(), dataField.getAppFieldId());
        });
    }

}
