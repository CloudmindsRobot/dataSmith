package com.cloudminds.data.smith.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据同步状态枚举
 *
 * @author Tao.Liu
 * @date 2022/6/29 16:11
 */
@Getter
@AllArgsConstructor
public enum DataSyncStatusEnum {

    UN_SYNCHRONIZED(0, "未同步"),
    SYNCHRONIZED(1, "已同步"),
    ;

    /**
     * 值
     */
    private Integer value;

    /**
     * 名称
     */
    private String name;


}
