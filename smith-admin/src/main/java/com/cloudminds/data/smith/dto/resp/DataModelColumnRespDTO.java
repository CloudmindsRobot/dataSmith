package com.cloudminds.data.smith.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据模型数据库字段响应数据
 *
 * @author Tao.Liu
 * @date 2022/8/17 14:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataModelColumnRespDTO {

    @ApiModelProperty("库名")
    private String name;

    @ApiModelProperty("数据类型")
    private String dataType;

    @ApiModelProperty("注释")
    private String remarks;

}
