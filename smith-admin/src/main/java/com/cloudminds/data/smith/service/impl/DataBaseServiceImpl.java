package com.cloudminds.data.smith.service.impl;

import com.cloudminds.data.smith.dto.resp.DataModelColumnRespDTO;
import com.cloudminds.data.smith.dto.resp.DataModelSchemaRespDTO;
import com.cloudminds.data.smith.service.DataBaseService;
import com.cloudminds.data.smith.service.DataSourceService;
import com.cloudminds.data.smith.util.Lists;
import com.cloudminds.data.smith.util.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 数据结构逻辑处理接口实现
 *
 * @author Tao.Liu
 * @date 2022/8/10 16:32
 */
@Slf4j
@Service
public class DataBaseServiceImpl implements DataBaseService {

    @Resource
    private DataSourceService dataSourceService;

    @Override
    public List<String> getSchemas(final Integer dataSourceId) {
        final List<String> schemas = new ArrayList<>();
        this.executeMetaData(dataSourceId, metaData -> {
            // ClickHouse
            try (final ResultSet resultSet = metaData.getSchemas()) {
                while (resultSet.next()) {
                    schemas.add(resultSet.getString(1));
                }
            } catch (Exception e) {
                log.error("获取数据源库列表出错, message={}", e.getMessage(), e);
            }

            // Mysql
            if (Lists.isEmpty(schemas)) {
                try (final ResultSet resultSet = metaData.getCatalogs()) {
                    while (resultSet.next()) {
                        schemas.add(resultSet.getString(1));
                    }
                } catch (Exception e) {
                    log.error("获取数据源库列表出错, message={}", e.getMessage(), e);
                }
            }
        });
        return schemas;
    }

    @Override
    public List<String> getTableList(final Integer dataSourceId, final String schema) {
        final List<String> tables = new ArrayList<>();
        this.executeMetaData(dataSourceId, metaData -> {
            try (final ResultSet resultSet = metaData.getTables(schema, schema, null, new String[]{"TABLE"})) {
                while (resultSet.next()) {
                    tables.add(resultSet.getString("TABLE_NAME"));
                }
            } catch (Exception e) {
                log.error("获取数据源表名列表出错, message={}", e.getMessage(), e);
            }
        });
        return tables;
    }

    @Override
    public List<DataModelColumnRespDTO> getTableColumns(final Integer dataSourceId, final String schema, final String tableName) {
        final List<DataModelColumnRespDTO> columns = new ArrayList<>();
        this.executeMetaData(dataSourceId, metaData -> {
            try (final ResultSet resultSet = metaData.getColumns(schema, schema, tableName, null)) {
                while (resultSet.next()) {
                    final String name = resultSet.getString("COLUMN_NAME");
                    final String dataType = resultSet.getString("TYPE_NAME");
                    final String remark = resultSet.getString("REMARKS");
                    final DataModelColumnRespDTO column = new DataModelColumnRespDTO(name, dataType, remark);
                    columns.add(column);
                }
            } catch (Exception e) {
                log.error("获取数据源字段列表出错, message={}", e.getMessage(), e);
            }
        });
        return columns;
    }

    @Override
    public List<DataModelSchemaRespDTO> getAllTableList(final Integer dataSourceId) {
        final List<DataModelSchemaRespDTO> tables = new ArrayList<>();
        this.executeMetaData(dataSourceId, metaData -> {
            try (final ResultSet resultSet = metaData.getTables(null, null, null, new String[]{"TABLE"})) {
                DataModelSchemaRespDTO lastSchema = null;
                while (resultSet.next()) {
                    String schema = resultSet.getString("TABLE_CAT");
                    if (Strings.isBlank(schema)) {
                        schema = resultSet.getString("TABLE_SCHEM");
                    }
                    final String tableName = resultSet.getString("TABLE_NAME");
                    if (lastSchema == null || Strings.isNotEquals(schema, lastSchema.getSchema())) {
                        lastSchema = new DataModelSchemaRespDTO(schema, tableName);
                        tables.add(lastSchema);
                    } else {
                        lastSchema.getTables().add(tableName);
                    }
                }
            } catch (Exception e) {
                log.error("获取数据源表名列表出错, message={}", e.getMessage(), e);
            }
        });
        return tables;
    }

    /**
     * 执行操作
     *
     * @param dataSourceId
     * @param consumer
     */
    private void executeMetaData(final Integer dataSourceId, final Consumer<DatabaseMetaData> consumer) {
        final DataSource dataSource = dataSourceService.getSqlDataSource(dataSourceId);
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            final DatabaseMetaData metaData = connection.getMetaData();
            consumer.accept(metaData);
        } catch (Exception e) {
            log.info("获取数据库metaData出错, message={}", e.getMessage(), e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

}
