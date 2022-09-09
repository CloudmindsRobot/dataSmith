package com.cloudminds.data.smith.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页请求参数
 *
 * @author Tao.Liu
 * @date 2022/6/29 15:15
 */
@Data
@ApiModel(description = "分页请求参数")
public class PageReqDTO {

    @ApiModelProperty("当前页")
    private Integer current;

    @ApiModelProperty("每页显示数量")
    private Integer size;

    /**
     * 默认值
     */
    public PageReqDTO() {
        this.current = 1;
        this.size = 10;
    }

}
