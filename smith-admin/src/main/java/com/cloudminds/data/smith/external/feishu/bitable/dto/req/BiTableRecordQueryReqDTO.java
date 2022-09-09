package com.cloudminds.data.smith.external.feishu.bitable.dto.req;

import com.cloudminds.data.smith.external.feishu.common.dto.PageReqDTO;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据表记录查询请求参数
 *
 * @author Tao.Liu
 * @date 2022/6/23 11:04
 */
@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BiTableRecordQueryReqDTO extends PageReqDTO {

    /**
     * 视图 id
     */
    private String viewId;

    /**
     * 筛选参数
     */
    private String filter;

    /**
     * 排序参数
     */
    private String sort;

    /**
     * 字段名称
     */
    private String fieldNames;

    /**
     * 控制多行文本字段数据的返回格式，true 表示以数组形式返回
     */
    private Boolean textFieldAsArray;

    public BiTableRecordQueryReqDTO(final String filter, final Integer pageSize) {
        super(pageSize);
        this.filter = filter;
    }
}
