package com.cloudminds.data.smith.external.feishu.bitable.dto.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 数据表记录保存请求项
 *
 * @author Tao.Liu
 * @date 2022/6/22 16:32
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BiRecordSaveItemReqDTO {

    /**
     * 记录ID
     */
    private String recordId;

    /**
     * 字段集合
     */
    Map<String, Object> fields;

    public BiRecordSaveItemReqDTO(final Map<String, Object> fields) {
        this.fields = fields;
    }

}
