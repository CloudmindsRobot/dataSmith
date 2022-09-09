package com.cloudminds.data.smith.external.feishu.bitable.dto.req;

import com.cloudminds.data.smith.external.feishu.bitable.dto.FieldProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据表字段保存请求数据
 *
 * @author Tao.Liu
 * @date 2022/6/22 16:32
 */
@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BiFieldSaveBodyReqDTO {

    /**
     * 多维表格字段名
     */
    private String fieldName;

    /**
     * 1：多行文本
     * 2：数字
     * 3：单选
     * 4：多选
     * 5：日期
     * 7：复选框
     * 11：人员
     * 15：超链接
     * 17：附件
     * 18：关联
     * 20：公式
     * 21：双向关联
     * 1001：创建时间
     * 1002：最后更新时间
     * 1003：创建人
     * 1004：修改人
     */
    private Integer type;

    /**
     * 字段属性
     */
    private FieldProperty property;

    public BiFieldSaveBodyReqDTO(final String fieldName) {
        // 默认多行文本
        this.fieldName = fieldName;
        this.type = 1;
    }

    public BiFieldSaveBodyReqDTO(final String fieldName, final Integer type) {
        this.fieldName = fieldName;
        this.type = type;
    }


}
