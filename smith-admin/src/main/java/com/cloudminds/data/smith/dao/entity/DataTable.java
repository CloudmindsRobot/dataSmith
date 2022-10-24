package com.cloudminds.data.smith.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 数据表格定义
 * </p>
 *
 * @author Tao.liu
 * @since 2022-08-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_data_table")
@ApiModel(value="DataTable对象", description="数据表格定义")
public class DataTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "名称")
    private String tableName;

    @ApiModelProperty(value = "数据模型ID")
    private Integer dataModelId;

    @ApiModelProperty(value = "外部应用ID")
    private String appId;

    @ApiModelProperty(value = "外部应用名称")
    private String appName;

    @ApiModelProperty(value = "外部应用类型,1飞书多维表格")
    private Integer appType;

    @ApiModelProperty(value = "外部业务表ID")
    private String appTableId;

    @ApiModelProperty(value = "同步状态，0未同步，1已同步, 2同步失败")
    private Integer syncStatus;

    @ApiModelProperty("同步失败原因")
    private String syncCause;

    @ApiModelProperty(value = "1正常, 9删除")
    private Integer status;

    @ApiModelProperty(value = "cron表达式")
    private String cron;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "数据同步执行时间")
    private Long executeTime;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "更新时间")
    private Long updateTime;


}
