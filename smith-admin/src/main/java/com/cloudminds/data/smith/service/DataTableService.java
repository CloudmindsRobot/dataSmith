package com.cloudminds.data.smith.service;

import com.cloudminds.data.smith.dto.PageResult;
import com.cloudminds.data.smith.dto.event.DataTableChangeEvent;
import com.cloudminds.data.smith.dto.req.DataTableQueryReqDTO;
import com.cloudminds.data.smith.dto.req.DataTableSaveReqDTO;
import com.cloudminds.data.smith.dto.resp.DataTableDetailRespDTO;
import com.cloudminds.data.smith.dto.resp.DataTableItemRespDTO;

import java.util.List;

/**
 * 数据表格操作接口
 *
 * @author Tao.Liu
 * @date 2022/6/29 14:55
 */
public interface DataTableService {

    /**
     * 查询数据表格分页列表
     *
     * @param queryReqDTO
     * @return
     */
    PageResult<DataTableItemRespDTO> findPageList(DataTableQueryReqDTO queryReqDTO);

    /**
     * 查询表格详情
     *
     * @param tableId
     * @return
     */
    DataTableDetailRespDTO findDetailById(Long tableId);

    /**
     * 保存数据表格
     *
     * @param saveReqDTO
     * @return
     */
    Long saveDataTable(DataTableSaveReqDTO saveReqDTO);

    /**
     * 删除数据表格
     *
     * @param dataTableId
     */
    void deleteById(Long dataTableId);

    /**
     * 批量删除字段
     *
     * @param dataTableId
     * @param fieldIds
     */
    void deleteFields(Long dataTableId, List<Long> fieldIds);

    /**
     * 表格定义变更事件通知
     *
     * @param changeEvent
     */
    void notifyTableChangeEvent(DataTableChangeEvent changeEvent);

}
