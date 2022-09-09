package com.cloudminds.data.smith.dto.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据模型详情响应数据
 *
 * @author Tao.Liu
 * @date 2022/8/11 17:51
 */
@Data
@ApiModel(description = "数据模型详情响应数据")
public class DataModelDetailRespDTO {

    @ApiModelProperty(value = "主键")
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

}
