package com.cloudminds.data.smith.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.cloudminds.data.smith.constant.DataSourceTypeEnum;
import com.cloudminds.data.smith.dao.entity.DataModel;
import com.cloudminds.data.smith.dao.entity.DataSource;
import com.cloudminds.data.smith.dao.mapper.DataModelMapper;
import com.cloudminds.data.smith.dao.mapper.DataSourceMapper;
import com.cloudminds.data.smith.dto.PageResult;
import com.cloudminds.data.smith.dto.req.DataModelQueryReqDTO;
import com.cloudminds.data.smith.dto.req.DataModelSaveReqDTO;
import com.cloudminds.data.smith.dto.req.DataModelValidReqDTO;
import com.cloudminds.data.smith.dto.req.HttpModelParam;
import com.cloudminds.data.smith.dto.resp.DataModelDetailRespDTO;
import com.cloudminds.data.smith.dto.resp.DataModelItemRespDTO;
import com.cloudminds.data.smith.exception.ParameterException;
import com.cloudminds.data.smith.external.feishu.auth.service.AuthApiService;
import com.cloudminds.data.smith.service.DataModelService;
import com.cloudminds.data.smith.service.DataSourceService;
import com.cloudminds.data.smith.util.Check;
import com.cloudminds.data.smith.util.Lists;
import com.cloudminds.data.smith.util.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据模型操作接口实现
 *
 * @author Tao.Liu
 * @date 2022/6/29 17:08
 */
@Slf4j
@Service
public class DataModelServiceImpl implements DataModelService {

    @Resource
    private DataModelMapper dataModelMapper;
    @Resource
    private DataSourceMapper dataSourceMapper;
    @Resource
    private DataSourceService dataSourceService;

    /**
     * http模型执行模板
     */
    private RestTemplate restTemplate = new RestTemplate(AuthApiService.REQUEST_FACTORY);

    @Override
    public PageResult<DataModelItemRespDTO> findPageList(final DataModelQueryReqDTO queryReqDTO) {
        final PageResult<DataModel> pageResult = dataModelMapper.findPageList(queryReqDTO);
        if (pageResult == null || Lists.isEmpty(pageResult.getRecords())) {
            return PageResult.empty();
        }
        final List<Integer> sourceIdList = Lists.toList(pageResult.getRecords(), DataModel::getSourceId);
        final List<DataSource> dataSourceList = dataSourceMapper.selectBatchIds(sourceIdList);
        final Map<Integer, DataSource> dataSourceMap = Lists.toMap(dataSourceList, DataSource::getId);

        return pageResult.convert(e -> {
            final DataModelItemRespDTO itemRespDTO = BeanUtil.copyProperties(e, DataModelItemRespDTO.class);
            final DataSource dataSource = dataSourceMap.get(itemRespDTO.getSourceId());
            if (dataSource != null) {
                itemRespDTO.setSourceName(dataSource.getName());
                itemRespDTO.setSourceType(dataSource.getType());
            }
            return itemRespDTO;
        });
    }

    @Override
    public DataModelDetailRespDTO findById(final Integer id) {
        final DataModel dataModel = dataModelMapper.selectById(id);
        if (dataModel == null) {
            return null;
        }
        final DataModelDetailRespDTO detailRespDTO = BeanUtil.copyProperties(dataModel, DataModelDetailRespDTO.class);
        return detailRespDTO;
    }

    @Override
    public List<Map<String, Object>> validateConfig(final DataModelValidReqDTO validReqDTO) {
        try {
            final List<Map<String, Object>> resultList = this.executeDataModel(validReqDTO, 0L, 1);
            log.debug("SQL=[{}], 返回结果为：{}", validReqDTO.getContent(), JSONUtil.toJsonStr(resultList));
            return resultList;
        } catch (Exception e) {
            throw new ParameterException(e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> queryForList(final Integer dataModelId, final Long startTime, final Integer limit) {
        final DataModel dataModel = dataModelMapper.selectById(dataModelId);
        Check.notNull(dataModel, "数据模型不存在");
        final DataModelValidReqDTO validReqDTO = BeanUtil.copyProperties(dataModel, DataModelValidReqDTO.class);
        final List<Map<String, Object>> resultList = this.executeDataModel(validReqDTO, startTime, limit);
        return resultList;
    }

    @Override
    public Integer saveDataModel(final DataModelSaveReqDTO saveReqDTO) {
        final DataModel saveDataModel = BeanUtil.copyProperties(saveReqDTO, DataModel.class);
        saveDataModel.setUpdateTime(System.currentTimeMillis());
        if (saveDataModel.getId() == null) {
            saveDataModel.setCreateTime(System.currentTimeMillis());
            dataModelMapper.insert(saveDataModel);
        } else {
            // 更新
            dataModelMapper.updateById(saveDataModel);
        }
        return saveDataModel.getId();
    }

    @Override
    public void deleteById(final Integer id) {
        Check.notNull(id, "ID不能为空");
        final int updateNum = dataModelMapper.deleteById(id);
        Check.isTrue(updateNum > 0, "记录不存在");
    }

    /**
     * 执行数据模型
     *
     * @param validReqDTO
     * @param startTime
     * @param limit
     * @return
     */
    private List<Map<String, Object>> executeDataModel(final DataModelValidReqDTO validReqDTO,
                                                       final Long startTime, final int limit) {
        final DataSource dataSourceInfo = dataSourceService.getById(validReqDTO.getSourceId());
        Check.notNull(dataSourceInfo, "无效的数据源ID");
        final DataSourceTypeEnum sourceTypeEnum = DataSourceTypeEnum.getByValue(dataSourceInfo.getType());
        if (sourceTypeEnum == DataSourceTypeEnum.HTTP) {
            return this.executeHttpDataModel(dataSourceInfo, validReqDTO, startTime, limit);
        } else {
            return this.executeSqlDataModel(validReqDTO, startTime, limit);
        }
    }

    /**
     * SQL执行器
     *
     * @param validReqDTO
     * @param startTime
     * @param limit
     * @return
     */
    private List<Map<String, Object>> executeSqlDataModel(final DataModelValidReqDTO validReqDTO,
                                                          final Long startTime,
                                                          final Integer limit) {
        final int paramNum = Strings.count(validReqDTO.getContent(), "?");
        final Long[] args = new Long[paramNum];
        for (int i = 0; i < args.length; i++) {
            args[i] = startTime;
        }

        final javax.sql.DataSource sqlDataSource = dataSourceService.getSqlDataSource(validReqDTO.getSourceId());
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(sqlDataSource);
        jdbcTemplate.setFetchSize(limit);
        jdbcTemplate.setMaxRows(limit);
        log.info("execute SQL: {}, Parameters: {}", validReqDTO.getContent(), args);
        final List<Map<String, Object>> resultList = jdbcTemplate.queryForList(validReqDTO.getContent(), args);
        log.info("Total {}", resultList.size());
        return resultList;
    }

    /**
     * http执行模型
     *
     * @param validReqDTO
     * @param startTime
     * @param limit
     * @return
     */
    private List<Map<String, Object>> executeHttpDataModel(final DataSource dataSource,
                                                           final DataModelValidReqDTO validReqDTO,
                                                           final Long startTime,
                                                           final Integer limit) {
        final String jsonPath = validReqDTO.getJsonPath();
        Check.hasText(jsonPath, "jsonPath参数不能为空");

        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(dataSource.getUrl() + validReqDTO.getContent());
        // 请求头
        final HttpHeaders headers = new HttpHeaders();
        if (Strings.isNotBlank(dataSource.getHeaders())) {
            final Map<String, String> headerMap = JSONUtil.toBean(dataSource.getHeaders(), Map.class);
            headerMap.entrySet().forEach(entry -> headers.put(entry.getKey(), Lists.asList(entry.getValue())));
        }

        // 配置参数解析
        HttpModelParam modelParam = new HttpModelParam();
        if (Strings.isNotBlank(validReqDTO.getParameter())) {
            modelParam = JSONUtil.toBean(validReqDTO.getParameter(), HttpModelParam.class);
        }
        // 加入固定参数
        modelParam.getBody().put("startTime", Strings.toString(startTime));
        modelParam.getBody().put("size", Strings.toString(limit));


        final HttpEntity httpEntity;
        final HttpMethod httpMethod = HttpMethod.resolve(modelParam.getMethod());
        if (httpMethod == HttpMethod.GET) {
            modelParam.getBody().entrySet().forEach(entry -> uriBuilder.queryParam(entry.getKey(), entry.getValue()));
            httpEntity = new HttpEntity(headers);
        } else {
            // 放body中
            httpEntity = new HttpEntity(modelParam.getBody(), headers);
        }
        final String requestUrl = uriBuilder.toUriString();
        final ParameterizedTypeReference<Map<String, Object>> reference = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        log.info("execute http request: {}, Parameters: {}", requestUrl, JSONUtil.toJsonStr(modelParam));
        final ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(requestUrl, httpMethod, httpEntity, reference);
        log.info("execute http request: {}, statusCode={}", requestUrl, responseEntity.getStatusCodeValue());
        final List<Map<String, Object>> resultList = this.getHttpResult(responseEntity, jsonPath);
        // 限制数量
        return resultList.size() > limit ? ListUtil.sub(resultList, 0, limit) : resultList;
    }

    /**
     * 获取http请求响应结果
     *
     * @param responseEntity
     * @param jsonPath
     * @return
     */
    private List<Map<String, Object>> getHttpResult(final ResponseEntity<Map<String, Object>> responseEntity, final String jsonPath) {
        if (responseEntity == null || responseEntity.getBody() == null) {
            return new ArrayList<>();
        }
        final Map<String, Object> bodyMap = responseEntity.getBody();
        final JSON jsonBody = JSONUtil.parse(bodyMap);

        /**
         * 通用数据结构 {code:0, message:"success", data: [...]}
         * redash数据结构 {query_result: { data: { columns: [...], rows: [...] } }, retrieved_at: 'xxx' }
         */
        final Object resultData = JSONUtil.getByPath(jsonBody, jsonPath);
        if (resultData != null && resultData.getClass().isAssignableFrom(JSONArray.class)) {
            // 转换为结果
            return (List<Map<String, Object>>) resultData;
        }
        return new ArrayList<>();
    }


}
