package com.cloudminds.data.smith.dto.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户登录信息响应数据
 *
 * @author Tao.Liu
 * @date 2022/8/4 10:55
 */
@Data
@ApiModel(description = "用户登录信息响应数据")
public class UserLoginInfoRespDTO {

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("登录时间")
    private Long loginTime;
}
