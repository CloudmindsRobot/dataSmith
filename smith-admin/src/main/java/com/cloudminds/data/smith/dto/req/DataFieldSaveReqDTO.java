package com.cloudminds.data.smith.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 数据表格保存请求参数
 *
 * @author Tao.Liu
 * @date 2022/6/29 14:57
 */
@Data
@ApiModel(description = "数据表格字段保存请求参数")
public class DataFieldSaveReqDTO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "字段键值")
    private String fieldKey;

    @NotBlank
    @ApiModelProperty(value = "名称")
    private String fieldName;

    @ApiModelProperty(value = "主键标识")
    private Boolean primaryFlag;

    @NotNull
    @ApiModelProperty(value = "类型，1：多行文本 2：数字 3：单选 4：多选 5：日期")
    private Integer type;

    @ApiModelProperty(value = "字段属性")
    private String property;

    @ApiModelProperty(value = "同步状态，0未同步，1已同步, 2同步失败")
    private Integer syncStatus;

    @ApiModelProperty("同步失败原因")
    private String syncCause;

    @NotNull
    @ApiModelProperty(value = "排序号")
    private Integer sortNo;

}
