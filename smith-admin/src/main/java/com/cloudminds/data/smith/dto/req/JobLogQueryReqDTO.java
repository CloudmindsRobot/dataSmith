package com.cloudminds.data.smith.dto.req;

import com.cloudminds.data.smith.dto.PageReqDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务作业日志查询请求参数
 *
 * @author Tao.Liu
 * @date 2022/6/29 15:17
 */
@Data
@ApiModel(description = "任务作业日志查询请求参数")
public class JobLogQueryReqDTO extends PageReqDTO {

    @ApiModelProperty(value = "处理者ID")
    private Long handlerId;

    @ApiModelProperty(value = "处理者名称")
    private String handlerName;

    @ApiModelProperty(value = "触发类型，1手动触发，2自动触发")
    private Integer triggerType;

    @ApiModelProperty(value = "执行结果，1成功，2失败")
    private Integer result;

    @ApiModelProperty(value = "创建开始时间")
    private Long createStartTime;

    @ApiModelProperty(value = "创建结束时间")
    private Long createEndTime;

}
