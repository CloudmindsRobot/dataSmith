package com.cloudminds.data.smith.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 数据表格保存请求参数
 *
 * @author Tao.Liu
 * @date 2022/6/29 14:57
 */
@Data
@ApiModel(description = "数据表格保存请求参数")
public class DataTableSaveReqDTO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "名称")
    private String tableName;

    @NotNull
    @ApiModelProperty(value = "数据模型ID")
    private Integer dataModelId;

    @NotBlank
    @ApiModelProperty(value = "外部应用ID")
    private String appId;

    @NotNull
    @ApiModelProperty(value = "外部应用类型,1飞书多维表格")
    private Integer appType;

    @ApiModelProperty(value = "cron表达式")
    private String cron;

    @ApiModelProperty(value = "备注")
    private String remark;

    @Valid
    @NotEmpty
    @ApiModelProperty("字段列表")
    private List<DataFieldSaveReqDTO> fields;

}
