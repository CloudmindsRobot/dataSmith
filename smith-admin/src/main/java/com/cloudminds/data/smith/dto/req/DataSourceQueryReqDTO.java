package com.cloudminds.data.smith.dto.req;

import com.cloudminds.data.smith.dto.PageReqDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据源查询请求参数
 *
 * @author Tao.Liu
 * @date 2022/6/29 15:17
 */
@Data
@ApiModel(description = "数据源查询请求参数")
public class DataSourceQueryReqDTO extends PageReqDTO {

    @ApiModelProperty(value = "数据源类型，1: MySQL 2: http")
    private Integer type;

    @ApiModelProperty(value = "名称")
    private String name;

}
