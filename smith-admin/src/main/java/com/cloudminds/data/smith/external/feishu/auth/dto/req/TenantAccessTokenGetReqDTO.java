package com.cloudminds.data.smith.external.feishu.auth.dto.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 企业自建应用token获取请求参数
 *
 * @author Tao.Liu
 * @date 2022/6/22 15:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TenantAccessTokenGetReqDTO {

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 密钥
     */
    private String appSecret;

}
