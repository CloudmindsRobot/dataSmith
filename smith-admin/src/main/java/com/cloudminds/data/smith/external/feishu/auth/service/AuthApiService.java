package com.cloudminds.data.smith.external.feishu.auth.service;

import com.cloudminds.data.smith.external.feishu.auth.dto.req.TenantAccessTokenGetReqDTO;
import com.cloudminds.data.smith.external.feishu.auth.dto.resp.TenantAccessTokenGetRespDTO;
import com.cloudminds.data.smith.external.feishu.common.constant.FeishuErrorCodeEnum;
import com.cloudminds.data.smith.external.feishu.common.exception.FeishuException;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 飞书认证api接口
 *
 * @author Tao.Liu
 * @date 2022/6/29 13:55
 */
@Data
public class AuthApiService {

    public final static String BASE_URL = "https://open.feishu.cn/open-apis/auth";

    /**
     * 请求
     */
    public final static ClientHttpRequestFactory REQUEST_FACTORY;

    /**
     * 请求模板
     */
    private final static RestTemplate REST_TEMPLATE;

    static {
        final OkHttp3ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory();
        factory.setConnectTimeout((int) TimeUnit.SECONDS.toMillis(5));
        // 300秒超时
        factory.setReadTimeout((int) TimeUnit.SECONDS.toMillis(300));
        REQUEST_FACTORY = factory;
        REST_TEMPLATE = new RestTemplate(factory);
    }

    /**
     * 过期时间
     */
    private AtomicLong EXPIRE_TIMES = new AtomicLong();

    /**
     * 企业自建应用token
     */
    private String tenantAccessToken = "";

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 应用名称
     */
    private String appSecret;


    public AuthApiService(final String appId, final String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
    }

    /**
     * 企业自建应用token
     *
     * @return
     */
    public String getTenantAccessToken() {
        if (Strings.isNotBlank(tenantAccessToken) && System.currentTimeMillis() < EXPIRE_TIMES.get()) {
            return this.tenantAccessToken;
        }

        synchronized (this) {
            final TenantAccessTokenGetReqDTO tokenGetReqDTO = new TenantAccessTokenGetReqDTO(appId, appSecret);
            final TenantAccessTokenGetRespDTO tokenRespDTO = REST_TEMPLATE.postForObject(BASE_URL + "/v3/tenant_access_token/internal", tokenGetReqDTO, TenantAccessTokenGetRespDTO.class);
            if (tokenGetReqDTO == null && Strings.isBlank(tokenRespDTO.getTenantAccessToken())) {
                throw new FeishuException(FeishuErrorCodeEnum.TABLE_ERROR, "获取飞书token出错");
            }

            // 设置过期时间，提前半小时失效
            final long expireMills = TimeUnit.SECONDS.toMillis(tokenRespDTO.getExpire() - 1800);
            EXPIRE_TIMES.set(System.currentTimeMillis() + expireMills);
            this.tenantAccessToken = tokenRespDTO.getTenantAccessToken();
            return this.tenantAccessToken;
        }
    }

}
