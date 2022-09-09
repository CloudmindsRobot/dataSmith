package com.cloudminds.data.smith.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 字段删除请求参数
 *
 * @author Tao.Liu
 * @date 2022/8/16 15:04
 */
@Data
@ApiModel(description = "数据表格字段保存请求参数")
public class DataFieldDeleteReqDTO {

    @NotNull
    @ApiModelProperty("表格ID")
    private Long tableId;

    @NotEmpty
    @ApiModelProperty("字段ID列表")
    private List<Long> fieldIds;
}
