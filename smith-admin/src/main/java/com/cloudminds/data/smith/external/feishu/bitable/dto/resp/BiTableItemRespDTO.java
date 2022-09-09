package com.cloudminds.data.smith.external.feishu.bitable.dto.resp;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * 飞书多维数据表格列表项
 *
 * @author Tao.Liu
 * @date 2022/6/29 20:03
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BiTableItemRespDTO {

    /**
     * 数据表ID
     */
    private String tableId;

    /**
     * 数据表的版本号
     */
    private Integer revision;

    /**
     * 数据表名字
     */
    private String name;

}
