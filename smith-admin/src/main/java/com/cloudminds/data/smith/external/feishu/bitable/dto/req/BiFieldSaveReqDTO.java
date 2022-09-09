package com.cloudminds.data.smith.external.feishu.bitable.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 飞书字段保存请求参数
 *
 * @author Tao.Liu
 * @date 2022/6/29 19:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BiFieldSaveReqDTO {

    /**
     * 字段ID
     */
    private String fieldId;

    /**
     * 表格ID
     */
    private String tableId;

    /**
     * 请求体
     */
    private BiFieldSaveBodyReqDTO saveBody;

}
