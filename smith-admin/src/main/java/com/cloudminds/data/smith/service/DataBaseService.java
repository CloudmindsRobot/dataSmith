package com.cloudminds.data.smith.service;

import com.cloudminds.data.smith.dto.resp.DataModelColumnRespDTO;
import com.cloudminds.data.smith.dto.resp.DataModelSchemaRespDTO;

import java.util.List;

/**
 * 数据结构逻辑处理接口
 *
 * @author Tao.Liu
 * @date 2022/8/10 16:26
 */
public interface DataBaseService {

    /**
     * 获取数据源库列表
     *
     * @param dataSourceId
     * @return
     */
    List<String> getSchemas(Integer dataSourceId);

    /**
     * 获取数据源表名列表
     *
     * @param dataSourceId
     * @param schema
     * @return
     */
    List<String> getTableList(Integer dataSourceId, String schema);

    /**
     * 获取数据库表字段列表
     *
     * @param dataSourceId
     * @param schema
     * @param tableName
     * @return
     */
    List<DataModelColumnRespDTO> getTableColumns(Integer dataSourceId, String schema, final String tableName);

    /**
     * 获取所有数据表列表
     *
     * @param dataSourceId
     * @return
     */
    List<DataModelSchemaRespDTO> getAllTableList(Integer dataSourceId);

}
