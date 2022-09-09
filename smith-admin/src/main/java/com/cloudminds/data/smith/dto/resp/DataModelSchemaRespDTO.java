package com.cloudminds.data.smith.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据模型数据库响应数据
 *
 * @author Tao.Liu
 * @date 2022/8/11 18:19
 */
@Data
@NoArgsConstructor
public class DataModelSchemaRespDTO {

    @ApiModelProperty("库名")
    private String schema;

    @ApiModelProperty("表名")
    private List<String> tables;

    public DataModelSchemaRespDTO(final String schema, final String table) {
        this.schema = schema;
        this.tables = new ArrayList<>();
        this.tables.add(table);
    }


}
