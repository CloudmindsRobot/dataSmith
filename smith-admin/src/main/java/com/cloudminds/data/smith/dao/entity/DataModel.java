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
 * 数据模型
 * </p>
 *
 * @author Tao.liu
 * @since 2022-08-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_data_model")
@ApiModel(value="DataModel对象", description="数据模型")
public class DataModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "数据源ID")
    private Integer sourceId;

    @ApiModelProperty(value = "执行内容，SQL语句/接口地址")
    private String content;

    @ApiModelProperty(value = "执行参数")
    private String parameter;

    @ApiModelProperty(value = "数据解析JSON路径")
    private String jsonPath;

    @ApiModelProperty(value = "字段数据 {key: name}")
    private String fieldData;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "更新时间")
    private Long updateTime;


}
