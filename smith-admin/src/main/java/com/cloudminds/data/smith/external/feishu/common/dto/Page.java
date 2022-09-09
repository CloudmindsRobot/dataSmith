package com.cloudminds.data.smith.external.feishu.common.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

/**
 * 飞书分页结构
 *
 * @author Tao.Liu
 * @date 2022/6/22 16:25
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Page<T> {

    /**
     * 是否还有更多项
     */
    private Boolean hasMore;

    /**
     * 分页标记，当 has_more 为 true 时，会同时返回新的 page_token，否则不返回 page_token
     */
    private String pageToken;

    /**
     * 总数
     */
    private Integer total;

    /**
     * 列表项
     */
    private List<T> items;

}
