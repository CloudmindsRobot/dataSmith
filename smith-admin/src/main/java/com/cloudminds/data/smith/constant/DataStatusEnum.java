package com.cloudminds.data.smith.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据状态枚举
 *
 * @author Tao.Liu
 * @date 2022/6/29 16:11
 */
@Getter
@AllArgsConstructor
public enum DataStatusEnum {

    NORMAL(1, "正常"),
    DELETE(9, "删除"),
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
