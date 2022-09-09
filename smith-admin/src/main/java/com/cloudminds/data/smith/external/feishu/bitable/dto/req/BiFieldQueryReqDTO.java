package com.cloudminds.data.smith.external.feishu.bitable.dto.req;

import com.cloudminds.data.smith.external.feishu.common.dto.PageReqDTO;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 飞书字段查询请求参数
 *
 * @author Tao.Liu
 * @date 2022/6/29 19:47
 */
@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BiFieldQueryReqDTO extends PageReqDTO {

    /**
     * 视图ID
     */
    private String viewId;

    public BiFieldQueryReqDTO(final Integer pageSize) {
        super(pageSize);
    }
}
