package com.cloudminds.data.smith.dto.resp;

import com.cloudminds.data.smith.dto.req.DataTableSaveReqDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 数据表格详情响应数据
 *
 * @author Tao.Liu
 * @date 2022/6/29 17:36
 */
@Data
@ApiModel(description = "数据表格详情响应数据")
public class DataTableDetailRespDTO extends DataTableSaveReqDTO {

}
