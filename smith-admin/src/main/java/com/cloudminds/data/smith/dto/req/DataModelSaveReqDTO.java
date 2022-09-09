package com.cloudminds.data.smith.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 数据模型保存请求参数
 * @author Tao.Liu
 * @date 2022/6/29 14:57
 */
@Data
@ApiModel(description = "数据模型保存请求参数")
public class DataModelSaveReqDTO {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @NotBlank
    @ApiModelProperty(value = "名称")
    private String name;

    @NotNull
    @ApiModelProperty(value = "数据源ID")
    private Integer sourceId;

    @NotBlank
    @ApiModelProperty(value = "执行内容，SQL语句/接口地址")
    private String content;

    @ApiModelProperty(value = "执行参数, 如果为http类型为JSON串，parameter={method:'GET', body={key1: value1, key2: value2}}")
    private String parameter;

    @NotBlank
    @ApiModelProperty(value = "字段数据 {key: name}")
    private String fieldData;

}
