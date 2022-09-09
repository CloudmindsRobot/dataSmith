package com.cloudminds.data.smith.external.feishu.bitable.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 同步数据响应数据
 *
 * @author Tao.Liu
 * @date 2022/7/4 11:50
 */
@Data
@AllArgsConstructor
public class TableRecordSyncRespDTO {

    /**
     * 新增数量
     */
    private Integer insertNum;

    /**
     * 更新数量
     */
    private Integer updateNum;

    /**
     * 删除数量
     */
    private Integer deleteNum;

    public TableRecordSyncRespDTO() {
        this.insertNum = 0;
        this.updateNum = 0;
        this.deleteNum = 0;
    }

}
