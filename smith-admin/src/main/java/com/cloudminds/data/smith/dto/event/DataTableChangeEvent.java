package com.cloudminds.data.smith.dto.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 数据表格定义变更事件
 *
 * @author Tao.Liu
 * @date 2022/7/4 17:26
 */
@Getter
@Setter
public class DataTableChangeEvent extends ApplicationEvent {

    /**
     * 表格ID
     */
    private Long tableId;

    public DataTableChangeEvent(Long tableId) {
        super(tableId);
        this.tableId = tableId;
    }


}
