package com.cloudminds.data.smith.external.feishu.bitable.service;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.cloudminds.data.smith.external.feishu.auth.service.AuthApiService;
import com.cloudminds.data.smith.external.feishu.bitable.dto.req.*;
import com.cloudminds.data.smith.external.feishu.bitable.dto.resp.BiFieldItemRespDTO;
import com.cloudminds.data.smith.external.feishu.bitable.dto.resp.BiRecordItemRespDTO;
import com.cloudminds.data.smith.external.feishu.bitable.dto.resp.BiTableItemRespDTO;
import com.cloudminds.data.smith.external.feishu.common.constant.FeishuErrorCodeEnum;
import com.cloudminds.data.smith.external.feishu.common.dto.Page;
import com.cloudminds.data.smith.external.feishu.common.dto.PageReqDTO;
import com.cloudminds.data.smith.external.feishu.common.dto.Resp;
import com.cloudminds.data.smith.external.feishu.common.exception.FeishuException;
import com.cloudminds.data.smith.util.Lists;
import com.cloudminds.data.smith.util.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 飞书多维表格API操作接口
 *
 * @author Tao.Liu
 * @date 2022/6/22 13:44
 */
@Slf4j
public class BiTableApiService {

    /**
     * 请求地址
     */
    public static final String BASE_URL = "https://open.feishu.cn/open-apis/bitable";

    /**
     * 超过2KB，则不输出响应
     */
    private final int MAX_IGNORE_RESP_BYTES = 2 * 1024;

    /**
     * 请求模板
     */
    private RestTemplate restTemplate;

    /**
     * 认证请求模板
     */
    private AuthApiService authApiService;


    public BiTableApiService(final String appId, final String appSecret) {
        this.authApiService = new AuthApiService(appId, appSecret);
        this.buildRestTemplate();
    }

    /**
     * 分页获取多维表格列表
     *
     * @param appToken
     * @param pageReqDTO
     * @return
     */
    public Page<BiTableItemRespDTO> pageTableList(final String appToken, final PageReqDTO pageReqDTO) {
        final String url = String.format(BASE_URL + "/v1/apps/%s/tables", appToken);
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url);
        uriBuilder.queryParam("page_token", pageReqDTO.getPageToken())
                .queryParam("page_size", pageReqDTO.getPageSize());
        final ParameterizedTypeReference<Resp<Page<BiTableItemRespDTO>>> reference = new ParameterizedTypeReference<Resp<Page<BiTableItemRespDTO>>>() {
        };
        final ResponseEntity<Resp<Page<BiTableItemRespDTO>>> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, HttpEntity.EMPTY, reference);
        return this.getSuccessData(response.getBody());
    }

    /**
     * 创建数据表格
     *
     * @param appToken
     * @param name
     * @return
     */
    public BiTableItemRespDTO createTable(final String appToken, final String name) {
        final String url = String.format(BASE_URL + "/v1/apps/%s/tables", appToken);
        final Map<String, Object> requestBody = MapUtil.of("table", MapUtil.of("name", name));

        final ParameterizedTypeReference<Resp<BiTableItemRespDTO>> reference = new ParameterizedTypeReference<Resp<BiTableItemRespDTO>>() {
        };
        final HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody);
        final ResponseEntity<Resp<BiTableItemRespDTO>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, reference);
        return this.getSuccessData(response.getBody());
    }

    /**
     * 删除单个数据表
     *
     * @param appToken
     * @param tableId
     */
    public void deleteTable(final String appToken, String tableId) {
        final String url = String.format(BASE_URL + "/v1/apps/%s/tables/%s", appToken, tableId);
        final ResponseEntity<Resp> response = restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity.EMPTY, Resp.class);
        this.getSuccessData(response.getBody());
    }

    /**
     * 查询字段分页列表
     *
     * @param appToken
     * @param queryReqDTO
     * @return
     */
    public Page<BiFieldItemRespDTO> pageFieldList(final String appToken, final String tableId, final BiFieldQueryReqDTO queryReqDTO) {
        final String url = String.format(BASE_URL + "/v1/apps/%s/tables/%s/fields", appToken, tableId);
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url);
        uriBuilder.queryParam("view_id", queryReqDTO.getViewId())
                .queryParam("page_token", queryReqDTO.getPageToken())
                .queryParam("page_size", queryReqDTO.getPageSize());
        final ParameterizedTypeReference<Resp<Page<BiFieldItemRespDTO>>> reference = new ParameterizedTypeReference<Resp<Page<BiFieldItemRespDTO>>>() {
        };
        final ResponseEntity<Resp<Page<BiFieldItemRespDTO>>> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, HttpEntity.EMPTY, reference);
        return this.getSuccessData(response.getBody());
    }

    /**
     * 获取表格所有字段列表
     *
     * @param appToken
     * @param tableId
     * @return
     */
    public List<BiFieldItemRespDTO> getFieldList(final String appToken, final String tableId) {
        final List<BiFieldItemRespDTO> fieldList = new ArrayList<>();
        // 查询当前表格所有字段数据,最多100个字段
        final BiFieldQueryReqDTO queryReqDTO = new BiFieldQueryReqDTO(100);
        boolean hasMore;
        do {
            final Page<BiFieldItemRespDTO> existPage = this.pageFieldList(appToken, tableId, queryReqDTO);
            if (existPage == null || Lists.isEmpty(existPage.getItems())) {
                break;
            }
            fieldList.addAll(existPage.getItems());
            queryReqDTO.setPageToken(existPage.getPageToken());
            hasMore = Boolean.TRUE.equals(existPage.getHasMore()) && Strings.isNotBlank(existPage.getPageToken());
        } while (hasMore);
        return fieldList;
    }

    /**
     * 创建字段
     *
     * @param appToken
     * @param saveReqDTO
     * @return
     */
    public BiFieldItemRespDTO createField(final String appToken, final BiFieldSaveReqDTO saveReqDTO) {
        final String url = String.format(BASE_URL + "/v1/apps/%s/tables/%s/fields", appToken, saveReqDTO.getTableId());
        final ParameterizedTypeReference<Resp<Map<String, BiFieldItemRespDTO>>> reference = new ParameterizedTypeReference<Resp<Map<String, BiFieldItemRespDTO>>>() {
        };
        final HttpEntity<BiFieldSaveBodyReqDTO> httpEntity = new HttpEntity<>(saveReqDTO.getSaveBody());
        final ResponseEntity<Resp<Map<String, BiFieldItemRespDTO>>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, reference);
        final Map<String, BiFieldItemRespDTO> resultMap = this.getSuccessData(response.getBody());
        return resultMap.get("field");
    }

    /**
     * 更新字段
     *
     * @param appToken
     * @param saveReqDTO
     * @return
     */
    public BiFieldItemRespDTO updateField(final String appToken, final BiFieldSaveReqDTO saveReqDTO) {
        final String url = String.format(BASE_URL + "/v1/apps/%s/tables/%s/fields/%s", appToken, saveReqDTO.getTableId(), saveReqDTO.getFieldId());
        final ParameterizedTypeReference<Resp<Map<String, BiFieldItemRespDTO>>> reference = new ParameterizedTypeReference<Resp<Map<String, BiFieldItemRespDTO>>>() {
        };
        final HttpEntity<BiFieldSaveBodyReqDTO> httpEntity = new HttpEntity<>(saveReqDTO.getSaveBody());
        final ResponseEntity<Resp<Map<String, BiFieldItemRespDTO>>> response = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, reference);
        try {
            final Map<String, BiFieldItemRespDTO> resultMap = this.getSuccessData(response.getBody());
            return resultMap.get("field");
        } catch (FeishuException e) {
            log.warn("同步飞书字段出错, message={}", e.getMessage());
        }
        final BiFieldItemRespDTO itemRespDTO = new BiFieldItemRespDTO();
        itemRespDTO.setFieldId(saveReqDTO.getFieldId());
        itemRespDTO.setFieldName(saveReqDTO.getSaveBody().getFieldName());
        itemRespDTO.setType(saveReqDTO.getSaveBody().getType());
        itemRespDTO.setProperty(saveReqDTO.getSaveBody().getProperty());
        return itemRespDTO;
    }

    /**
     * 删除字段
     *
     * @param appToken
     * @param tableId
     * @param fieldId
     * @return
     */
    public Boolean deleteField(final String appToken, final String tableId, final String fieldId) {
        final String url = String.format(BASE_URL + "/v1/apps/%s/tables/%s/fields/%s", appToken, tableId, fieldId);
        final ResponseEntity<Resp> response = restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity.EMPTY, Resp.class);
        try {
            final Map<String, Object> resultMap = (Map<String, Object>) this.getSuccessData(response.getBody());
            return (Boolean) resultMap.get("deleted");
        } catch (FeishuException e) {
            log.warn("删除飞书字段出错, message={}", e.getMessage());
        }
        return false;
    }

    /**
     * 分页获取多维表格记录
     *
     * @param appToken
     * @param queryReqDTO
     * @return
     */
    public Page<BiRecordItemRespDTO> pageRecordList(final String appToken, final String tableId, final BiTableRecordQueryReqDTO queryReqDTO) {
        final String url = String.format(BASE_URL + "/v1/apps/%s/tables/%s/records", appToken, tableId);
        final ParameterizedTypeReference<Resp<Page<BiRecordItemRespDTO>>> reference = new ParameterizedTypeReference<Resp<Page<BiRecordItemRespDTO>>>() {
        };
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url);
        uriBuilder.queryParam("view_id", queryReqDTO.getViewId())
                .queryParam("filter", queryReqDTO.getFilter())
                .queryParam("sort", queryReqDTO.getSort())
                .queryParam("field_names", queryReqDTO.getFieldNames())
                .queryParam("text_field_as_array", queryReqDTO.getTextFieldAsArray())
                .queryParam("page_token", queryReqDTO.getPageToken())
                .queryParam("page_size", queryReqDTO.getPageSize());
        final String requestUrl = uriBuilder.build().toString();
        final ResponseEntity<Resp<Page<BiRecordItemRespDTO>>> response = restTemplate.exchange(requestUrl, HttpMethod.GET, HttpEntity.EMPTY, reference);
        return this.getSuccessData(response.getBody());
    }

    /**
     * 批量新增记录
     *
     * @param appToken
     * @param tableId
     * @param saveReqDTO
     * @return
     */
    public List<BiRecordItemRespDTO> insertBatchRecords(final String appToken, final String tableId, final TableRecordSaveReqDTO saveReqDTO) {
        final String url = String.format(BASE_URL + "/v1/apps/%s/tables/%s/records/batch_create", appToken, tableId);
        return saveBatchRecords(saveReqDTO, url);
    }

    /**
     * 批量更新
     *
     * @param appToken
     * @param tableId
     * @param saveReqDTO
     * @return
     */
    public List<BiRecordItemRespDTO> updateBatchRecords(final String appToken, final String tableId, final TableRecordSaveReqDTO saveReqDTO) {
        final String url = String.format(BASE_URL + "/v1/apps/%s/tables/%s/records/batch_update", appToken, tableId);

        /**
         *飞书更新数据内部逻辑是异步操作，下次更新时会触发同步处理，可能会产生上次数据未处理完的问题，产生try again later
         * 加上重试处理
         */

        // 最大重试次数
        final int maxRetry = 3;
        // 间隔时间3秒
        int interval = 3000;
        for (int i = 0; i <= maxRetry; i++) {
            try {
                return this.saveBatchRecords(saveReqDTO, url);
            } catch (FeishuException e) {
                if (i == maxRetry || Strings.isNotEquals(FeishuErrorCodeEnum.TRY_AGAIN_LATER.getCode(), e.getCode())) {
                    throw e;
                }
                log.info("飞书更新记录异常，进行重试处理, tableId={}", tableId);
                ThreadUtil.sleep(interval * (i + 1));
            }
        }
        return saveBatchRecords(saveReqDTO, url);
    }

    /**
     * 批量保存记录
     *
     * @param saveReqDTO
     * @param url
     * @return
     */
    private List<BiRecordItemRespDTO> saveBatchRecords(final TableRecordSaveReqDTO saveReqDTO, final String url) {
        final ParameterizedTypeReference<Resp<Map<String, List<BiRecordItemRespDTO>>>> reference = new ParameterizedTypeReference<Resp<Map<String, List<BiRecordItemRespDTO>>>>() {
        };
        final HttpEntity<TableRecordSaveReqDTO> httpEntity = new HttpEntity<>(saveReqDTO);
        final ResponseEntity<Resp<Map<String, List<BiRecordItemRespDTO>>>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, reference);
        final Map<String, List<BiRecordItemRespDTO>> responseBody = this.getSuccessData(response.getBody());
        return responseBody.get("records");
    }


    /**
     * 获取成功数据
     *
     * @param resp
     * @param <T>
     * @return
     */
    private <T> T getSuccessData(final Resp<T> resp) {
        if (!Resp.isSuccess(resp)) {
            log.warn("飞书接口响应 -->  {}", JSONUtil.toJsonStr(resp));
            throw new FeishuException(FeishuErrorCodeEnum.TABLE_ERROR, "飞书接口响应异常：" + resp.getMsg());
        }
        return resp.getData();
    }

    /**
     * 构建restTemplate
     */
    private void buildRestTemplate() {
        this.restTemplate = new RestTemplate(AuthApiService.REQUEST_FACTORY);
        this.restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(final ClientHttpResponse response) throws IOException {
                return response.getStatusCode() != HttpStatus.OK;
            }

            @Override
            public void handleError(final ClientHttpResponse response) throws IOException {
                final String body = IoUtil.read(response.getBody(), StandardCharsets.UTF_8);
                final String logId = response.getHeaders().getFirst("x-tt-logid");
                final int statusCode = response.getRawStatusCode();
                log.warn("飞书接口响应异常 --> status:{}, body: {}, x-tt-logid:{}", statusCode, body, logId);
                if (HttpStatus.FORBIDDEN == response.getStatusCode()) {
                    throw new FeishuException(FeishuErrorCodeEnum.TABLE_ERROR, "无权限操作，请设置多维表格应用权限");
                }
                // 504响应码转换
                if (HttpStatus.GATEWAY_TIMEOUT == response.getStatusCode() && Strings.isNotBlank(body)) {
                    final Resp<Void> resp = JSONUtil.toBean(body, Resp.class);
                    if (Objects.equals(1255040, resp.getCode())) {
                        // 飞书接口响应Request timed out, please try again later
                        throw new FeishuException(FeishuErrorCodeEnum.TRY_AGAIN_LATER, "飞书接口响应出错：" + resp.getMsg());
                    }
                }
                if (HttpStatus.OK != response.getStatusCode()) {
                    throw new FeishuException(FeishuErrorCodeEnum.TABLE_ERROR, "飞书接口响应出错：" + body);
                }
            }
        });
        // 拦截器
        final ClientHttpRequestInterceptor interceptor = (request, body, execution) -> {
            final String tenantAccessToken = "Bearer " + this.authApiService.getTenantAccessToken();
            final String requestBody = body.length > MAX_IGNORE_RESP_BYTES ? "ignore..." : new String(body, StandardCharsets.UTF_8);
            log.info("请求飞书接口，{} url={}, token={}, body={}", request.getMethod().name(), request.getURI().getRawPath(), tenantAccessToken, requestBody);
            // 添加请求头
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, tenantAccessToken);
            final ClientHttpResponse response = execution.execute(request, body);
            return response;
        };
        this.restTemplate.setInterceptors(Lists.asList(interceptor));
    }


}
