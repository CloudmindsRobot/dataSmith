package com.cloudminds.data.smith.service;

import com.cloudminds.data.smith.dao.entity.DataSource;
import com.cloudminds.data.smith.dto.PageResult;
import com.cloudminds.data.smith.dto.req.DataSourceQueryReqDTO;
import com.cloudminds.data.smith.dto.req.DataSourceSaveReqDTO;
import com.cloudminds.data.smith.dto.req.DataSourceValidateReqDTO;
import com.cloudminds.data.smith.dto.resp.DataSourceItemRespDTO;

/**
 * 数据源操作接口
 *
 * @author Tao.Liu
 * @date 2022/6/29 14:55
 */
public interface DataSourceService {

    /**
     * 查询数据源分页列表
     *
     * @param queryReqDTO
     * @return
     */
    PageResult<DataSourceItemRespDTO> findPageList(DataSourceQueryReqDTO queryReqDTO);

    /**
     * 数据源验证
     *
     * @param validateReqDTO
     */
    void validateDataSource(DataSourceValidateReqDTO validateReqDTO);

    /**
     * 保存数据源
     *
     * @param saveReqDTO
     * @return
     */
    Integer saveDataSource(DataSourceSaveReqDTO saveReqDTO);

    /**
     * 根据ID查询
     *
     * @param sourceId
     * @return
     */
    DataSource getById(Integer sourceId);

    /**
     * 获取SQL数据源
     *
     * @param sourceId
     * @return
     */
    javax.sql.DataSource getSqlDataSource(Integer sourceId);

    /**
     * 删除数据源
     *
     * @param id
     */
    void deleteById(Integer id);

}
