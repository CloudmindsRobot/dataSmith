package com.cloudminds.data.smith.external.feishu.bitable.dto.resp;

import com.cloudminds.data.smith.external.feishu.bitable.dto.FieldProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * 飞书字段列表项响应数据
 *
 * @author Tao.Liu
 * @date 2022/6/29 19:50
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BiFieldItemRespDTO {

    /**
     * 字段ID
     */
    private String fieldId;

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

}
