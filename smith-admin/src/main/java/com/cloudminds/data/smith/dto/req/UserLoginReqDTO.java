package com.cloudminds.data.smith.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录请求参数
 *
 * @author Tao.Liu
 * @date 2022/8/4 10:54
 */
@Data
@ApiModel(description = "用户登录请求参数")
public class UserLoginReqDTO {

    @NotBlank
    @ApiModelProperty("账号名")
    private String account;

    @NotBlank
    @ApiModelProperty("密码")
    private String password;

}
