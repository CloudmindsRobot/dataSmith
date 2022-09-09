package com.cloudminds.data.smith.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 数据源保存请求参数
 * @author Tao.Liu
 * @date 2022/6/29 14:57
 */
@Data
@ApiModel(description = "数据源保存请求参数")
public class DataSourceSaveReqDTO {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @NotNull
    @ApiModelProperty(value = "数据源类型，1: MySQL 2: http")
    private Integer type;

    @NotNull
    @ApiModelProperty(value = "名称")
    private String name;

    @NotNull
    @ApiModelProperty(value = "链接地址")
    private String url;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "请求头,数据JSON字符串，例：{Authorization: 'xxx'}")
    private String headers;

    @ApiModelProperty(value = "备注")
    private String remark;

}
