package com.cloudminds.data.smith.dto.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 字段同步结果响应实体
 *
 * @author Tao.Liu
 * @date 2022/9/21 15:09
 */
@Data
@NoArgsConstructor
@ApiModel("字段同步结果响应实体")
public class DataFieldSyncRespDTO {

    @ApiModelProperty("字段ID")
    private Long fieldId;

    @ApiModelProperty("外部应用字段ID")
    private String appFieldId;

    @ApiModelProperty("true成功，false失败")
    private Boolean flag;

    @ApiModelProperty("失败原因")
    private String cause;

    public DataFieldSyncRespDTO(final Long fieldId, final String appFieldId) {
        this.fieldId = fieldId;
        this.appFieldId = appFieldId;
        this.flag = Boolean.TRUE;
    }

    public DataFieldSyncRespDTO(final Long fieldId, final String appFieldId, final String cause) {
        this.fieldId = fieldId;
        this.appFieldId = appFieldId;
        this.flag = Boolean.FALSE;
        this.cause = cause;
    }
}
