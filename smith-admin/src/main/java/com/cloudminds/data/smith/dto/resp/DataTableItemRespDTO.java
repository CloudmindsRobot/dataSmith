package com.cloudminds.data.smith.dto.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据表格列表项响应数据
 *
 * @author Tao.Liu
 * @date 2022/6/29 15:19
 */
@Data
@ApiModel(description = "数据表格列表项响应数据")
public class DataTableItemRespDTO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String tableName;

    @ApiModelProperty(value = "数据模型ID")
    private Integer dataModelId;

    @ApiModelProperty(value = "数据模型名称")
    private String dataModelName;

    @ApiModelProperty(value = "外部应用ID")
    private String appId;

    @ApiModelProperty(value = "外部应用类型,1飞书多维表格")
    private Integer appType;

    @ApiModelProperty(value = "外部业务表ID")
    private String appTableId;

    @ApiModelProperty(value = "同步状态，0未同步，1已同步，2同步失败")
    private Integer syncStatus;

    @ApiModelProperty("同步失败原因")
    private String syncCause;

    @ApiModelProperty(value = "cron表达式")
    private String cron;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "数据同步执行时间")
    private Long executeTime;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

}
