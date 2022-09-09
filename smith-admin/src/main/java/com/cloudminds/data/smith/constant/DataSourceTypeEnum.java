package com.cloudminds.data.smith.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 数据源类型
 *
 * @author Tao.Liu
 * @date 2022/6/30 18:16
 */
@Getter
@AllArgsConstructor
public enum DataSourceTypeEnum {

    MYSQL(1, "com.mysql.cj.jdbc.Driver"),
    HTTP(2, ""),
    CLICK_HOUSE(3, "com.clickhouse.jdbc.ClickHouseDriver"),
    ;

    /**
     * 值
     */
    private Integer value;

    /**
     * 驱动名称
     */
    private String driverName;

    /**
     * 根据值获取枚举
     *
     * @param value
     * @return
     */
    public static DataSourceTypeEnum getByValue(final Integer value) {
        return Stream.of(DataSourceTypeEnum.values())
                .filter(e -> Objects.equals(e.getValue(), value)).findFirst().orElse(null);
    }

}
