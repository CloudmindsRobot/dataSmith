package com.cloudminds.data.smith.service;

import com.cloudminds.data.smith.dto.PageResult;
import com.cloudminds.data.smith.dto.req.DataModelQueryReqDTO;
import com.cloudminds.data.smith.dto.req.DataModelSaveReqDTO;
import com.cloudminds.data.smith.dto.req.DataModelValidReqDTO;
import com.cloudminds.data.smith.dto.resp.DataModelDetailRespDTO;
import com.cloudminds.data.smith.dto.resp.DataModelItemRespDTO;

import java.util.List;
import java.util.Map;

/**
 * 数据模型操作接口
 *
 * @author Tao.Liu
 * @date 2022/6/29 14:55
 */
public interface DataModelService {

    /**
     * 查询数据模型分页列表
     *
     * @param queryReqDTO
     * @return
     */
    PageResult<DataModelItemRespDTO> findPageList(DataModelQueryReqDTO queryReqDTO);

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    DataModelDetailRespDTO findById(Integer id);

    /**
     * 保存数据模型
     *
     * @param saveReqDTO
     * @return
     */
    Integer saveDataModel(DataModelSaveReqDTO saveReqDTO);

    /**
     * 验证数据模型配置
     *
     * @param validReqDTO
     * @return
     */
    List<Map<String, Object>> validateConfig(DataModelValidReqDTO validReqDTO);

    /**
     * 执行语句，获取列表数据
     *
     * @param dataModelId
     * @param startTime
     * @param limit
     * @return
     */
    List<Map<String, Object>> queryForList(Integer dataModelId, Long startTime, Integer limit);

    /**
     * 删除数据模型
     *
     * @param id
     */
    void deleteById(Integer id);

}
