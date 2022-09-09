package com.cloudminds.data.smith.external.feishu.bitable.dto.resp;

import com.cloudminds.data.smith.external.feishu.bitable.dto.Person;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Map;

/**
 * 字段列表项响应数据
 *
 * @author Tao.Liu
 * @date 2022/6/23 11:10
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BiRecordItemRespDTO {

    /**
     * 记录ID
     */
    private String recordId;

    /**
     * 字段集合
     */
    Map<String, Object> fields;

    /**
     * 创建人
     */
    private Person createBy;

    /**
     * 创建时间
     */
    private Long createdTime;

    /**
     * 修改人
     */
    private Person lastModifiedBy;

    /**
     * 最近更新时间
     */
    private Long lastModifiedTime;

}
