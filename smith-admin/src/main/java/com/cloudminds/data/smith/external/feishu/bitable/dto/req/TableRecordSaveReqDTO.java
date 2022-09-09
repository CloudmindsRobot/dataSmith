package com.cloudminds.data.smith.external.feishu.bitable.dto.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 数据表字段保存请求数据
 *
 * @author Tao.Liu
 * @date 2022/6/22 16:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TableRecordSaveReqDTO {

    /**
     * 记录
     */
    public List<BiRecordSaveItemReqDTO> records;


}
