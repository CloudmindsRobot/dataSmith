package com.cloudminds.data.smith.service.support;

import com.cloudminds.data.smith.constant.DataSourceTypeEnum;
import com.cloudminds.data.smith.exception.ParameterException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.util.Strings;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据源缓存
 *
 * @author Tao.Liu
 * @date 2022/9/8 18:52
 */
public class DataSourceCache {

    /**
     * 数据源缓存
     */
    private final static Map<Integer, HikariDataSource> DATA_SOURCE_CACHE = new ConcurrentHashMap<>();

    /**
     * 验证数据源
     *
     * @param dataSourceInfo
     * @return
     */
    public static boolean validateDataSource(com.cloudminds.data.smith.dao.entity.DataSource dataSourceInfo) {
        if (Objects.equals(DataSourceTypeEnum.HTTP.getValue(), dataSourceInfo.getType())) {
            throw new ParameterException("暂不支持HTTP数据源验证");
        }
        final HikariConfig hikariConfig = buildConfig(dataSourceInfo);
        try (HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig)) {
            hikariDataSource.validate();
        } catch (Exception e) {
            throw new ParameterException(e.getMessage());
        }
        return true;
    }

    /**
     * 创建数据源
     *
     * @param dataSourceInfo
     * @return
     */
    public static DataSource createDataSource(com.cloudminds.data.smith.dao.entity.DataSource dataSourceInfo) {
        final HikariConfig hikariConfig = buildConfig(dataSourceInfo);
        final HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
        final HikariDataSource oldDataSource = DATA_SOURCE_CACHE.put(dataSourceInfo.getId(), hikariDataSource);
        if (oldDataSource != null) {
            // 关闭旧数据源
            oldDataSource.close();
        }
        return hikariDataSource;
    }

    /**
     * 获取数据源
     *
     * @param dataSourceId
     * @return
     */
    public static DataSource getDataSource(final Integer dataSourceId) {
        return DATA_SOURCE_CACHE.get(dataSourceId);
    }

    /**
     * 移除数据源
     *
     * @param dataSourceId
     */
    public static void removeDataSource(final Integer dataSourceId) {
        final HikariDataSource dataSource = DATA_SOURCE_CACHE.remove(dataSourceId);
        if (dataSource != null) {
            dataSource.close();
        }
    }

    /**
     * 数据源配置
     *
     * @param dataSourceInfo
     * @return
     */
    private static HikariConfig buildConfig(final com.cloudminds.data.smith.dao.entity.DataSource dataSourceInfo) {
        final DataSourceTypeEnum sourceTypeEnum = DataSourceTypeEnum.getByValue(dataSourceInfo.getType());
        if (sourceTypeEnum == null || Strings.isBlank(sourceTypeEnum.getDriverName())) {
            throw new ParameterException("无效的数据源配置");
        }
        final String sourceName = sourceTypeEnum.name().toLowerCase();
        final String jdbcUrl = String.format("jdbc:%s://%s:%d", sourceName, dataSourceInfo.getHost(), dataSourceInfo.getPort());
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setUsername(dataSourceInfo.getAccount());
        hikariConfig.setPassword(dataSourceInfo.getPassword());
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setDriverClassName(sourceTypeEnum.getDriverName());
        hikariConfig.setIdleTimeout(30000);
        hikariConfig.setMaximumPoolSize(5);
        hikariConfig.setMinimumIdle(1);
        return hikariConfig;
    }

}
