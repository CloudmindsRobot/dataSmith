package com.cloudminds.data.smith.service;

import com.cloudminds.data.smith.dao.entity.DataField;
import com.cloudminds.data.smith.dao.entity.DataTable;
import com.cloudminds.data.smith.dto.resp.DataFieldSyncRespDTO;
import com.cloudminds.data.smith.external.feishu.bitable.dto.resp.TableRecordSyncRespDTO;

import java.util.List;
import java.util.Map;

/**
 * 表格数据同步接口
 *
 * @author Tao.Liu
 * @date 2022/6/30 15:18
 */
public interface DataTableSyncService {

    /**
     * 同步数据表格
     *
     * @param dataTable
     * @return
     */
    String syncDataTable(DataTable dataTable);

    /**
     * 删除数据表格
     *
     * @param dataTable
     * @return
     */
    boolean deleteDataTable(DataTable dataTable);

    /**
     * 批量同步字段
     *
     * @param dataTable
     * @param dataFields
     * @return
     */
    List<DataFieldSyncRespDTO> syncBatchDataField(DataTable dataTable, List<DataField> dataFields);

    /**
     * 删除数据表格字段
     *
     * @param dataTable
     * @param dataField
     * @return
     */
    boolean deleteField(DataTable dataTable, DataField dataField);

    /**
     * 批量新增记录
     *
     * @param dataTable
     * @param fieldList
     * @param recordList
     * @return
     */
    Integer insertRecords(final DataTable dataTable, List<DataField> fieldList, List<Map<String, Object>> recordList);

    /**
     * 同步记录
     *
     * @param dataTable
     * @param fieldList
     * @param recordList
     * @return
     */
    TableRecordSyncRespDTO syncRecords(final DataTable dataTable, List<DataField> fieldList, List<Map<String, Object>> recordList);

}
