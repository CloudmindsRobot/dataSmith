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
 * 数据表格字段
 * </p>
 *
 * @author Tao.liu
 * @since 2022-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_data_field")
@ApiModel(value="DataField对象", description="数据表格字段")
public class DataField implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "数据表格ID")
    private Long tableId;

    @ApiModelProperty(value = "字段键值")
    private String fieldKey;

    @ApiModelProperty(value = "名称")
    private String fieldName;

    @ApiModelProperty(value = "主键标识")
    private Boolean primaryFlag;

    @ApiModelProperty(value = "类型，1：多行文本 2：数字 3：单选 4：多选 5：日期")
    private Integer type;

    @ApiModelProperty(value = "外部应用字段ID")
    private String appFieldId;

    @ApiModelProperty(value = "字段属性")
    private String property;

    @ApiModelProperty(value = "同步状态，0未同步，1已同步")
    private Integer syncStatus;

    @ApiModelProperty(value = "1正常, 9删除")
    private Integer status;

    @ApiModelProperty(value = "排序号")
    private Integer sortNo;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "更新时间")
    private Long updateTime;


}
