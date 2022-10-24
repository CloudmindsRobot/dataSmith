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
 * 任务作业日志
 * </p>
 *
 * @author Tao.liu
 * @since 2022-07-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_job_log")
@ApiModel(value="JobLog对象", description="任务作业日志")
public class JobLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "处理者ID")
    private Long handlerId;

    @ApiModelProperty(value = "处理者名称")
    private String handlerName;

    @ApiModelProperty(value = "新增数量")
    private Integer insertNum;

    @ApiModelProperty(value = "修改数量")
    private Integer updateNum;

    @ApiModelProperty(value = "删除数量")
    private Integer deleteNum;

    @ApiModelProperty(value = "触发类型，1手动触发，2自动触发")
    private Integer triggerType;

    @ApiModelProperty(value = "执行结果，0执行中，1成功，2失败")
    private Integer result;

    @ApiModelProperty(value = "耗时(ms)")
    private Integer costTime;

    @ApiModelProperty(value = "异常原因")
    private String cause;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "更新时间")
    private Long updateTime;


}
