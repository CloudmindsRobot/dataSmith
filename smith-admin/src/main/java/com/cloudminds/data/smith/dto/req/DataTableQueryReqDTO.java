package com.cloudminds.data.smith.dto.req;

import com.cloudminds.data.smith.dto.PageReqDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据表格查询请求参数
 *
 * @author Tao.Liu
 * @date 2022/6/29 15:17
 */
@Data
@ApiModel(description = "数据表格查询请求参数")
public class DataTableQueryReqDTO extends PageReqDTO {

    @ApiModelProperty(value = "名称")
    private String tableName;

    @ApiModelProperty(value = "外部应用类型,1飞书多维表格")
    private Integer appType;

}
