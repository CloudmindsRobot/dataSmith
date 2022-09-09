package com.cloudminds.data.smith.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务作业日志列表项响应数据
 *
 * @author Tao.Liu
 * @date 2022/7/6 13:54
 */
@Data
public class JobLogItemRespDTO {

    @ApiModelProperty(value = "主键")
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

    @ApiModelProperty(value = "执行结果，1成功，2失败")
    private Integer result;

    @ApiModelProperty(value = "耗时(ms)")
    private Integer costTime;

    @ApiModelProperty(value = "异常原因")
    private String cause;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "结束时间")
    private Long updateTime;

}
