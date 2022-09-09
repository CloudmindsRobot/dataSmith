package com.cloudminds.data.smith.external.feishu.common.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 飞书分页查询请求参数
 *
 * @author Tao.Liu
 * @date 2022/6/29 19:45
 */
@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PageReqDTO {

    /**
     * 分页标记，第一次请求不填，表示从头开始遍历；分页查询结果还有更多项时会同时返回新的 page_token，下次遍历可采用该 page_token 获取查询结果
     */
    private String pageToken;

    /**
     * 分页大小, 最大值：100
     */
    private Integer pageSize;

    public PageReqDTO(final Integer pageSize) {
        this.pageSize = pageSize;
        this.pageToken = "";
    }

}
