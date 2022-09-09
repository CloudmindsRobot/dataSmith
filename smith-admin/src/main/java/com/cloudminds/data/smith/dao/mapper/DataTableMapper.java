package com.cloudminds.data.smith.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.cloudminds.data.smith.constant.DataStatusEnum;
import com.cloudminds.data.smith.constant.DataSyncStatusEnum;
import com.cloudminds.data.smith.dao.entity.DataTable;
import com.cloudminds.data.smith.dto.PageResult;
import com.cloudminds.data.smith.dto.req.DataTableQueryReqDTO;
import com.cloudminds.data.smith.util.Check;
import com.cloudminds.data.smith.util.Strings;

import java.util.List;

/**
 * <p>
 * 数据表格定义 Mapper 接口
 * </p>
 *
 * @author Tao.liu
 * @since 2022-06-29
 */
public interface DataTableMapper extends BaseMapper<DataTable> {

    /**
     * 查询据模型分页列表
     *
     * @param queryReqDTO
     * @return
     */
    default PageResult<DataTable> selectPageList(final DataTableQueryReqDTO queryReqDTO) {
        final LambdaQueryWrapper<DataTable> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(DataTable::getStatus, DataStatusEnum.DELETE.getValue());
        if (Strings.isNotBlank(queryReqDTO.getTableName())) {
            wrapper.like(DataTable::getTableName, queryReqDTO.getTableName());
        }
        if (queryReqDTO.getAppType() != null) {
            wrapper.eq(DataTable::getAppType, queryReqDTO.getAppType());
        }
        wrapper.orderByDesc(DataTable::getCreateTime);
        final PageDTO<DataTable> page = this.selectPage(new PageDTO<>(queryReqDTO.getCurrent(), queryReqDTO.getSize()), wrapper);
        return PageResult.of(page);
    }

    /**
     * 查询有执行任务的表格列表
     *
     * @param limit
     * @return
     */
    default List<DataTable> selectCronTableList(final Integer limit) {
        final LambdaQueryWrapper<DataTable> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(DataTable::getStatus, DataStatusEnum.DELETE.getValue())
                .isNotNull(DataTable::getCron).orderByDesc(DataTable::getUpdateTime)
                .last("limit " + limit);
        final List<DataTable> dataTableList = this.selectList(wrapper);
        return dataTableList;
    }

    /**
     * 根据名称与外部应用ID查询表格
     *
     * @param appId
     * @param tableName
     * @return
     */
    default DataTable findByNameAndAppId(final String appId, final String tableName) {
        Check.hasText(appId, "外部应用ID不能为空");
        Check.hasText(tableName, "数据表格名称不能为空");
        final LambdaQueryWrapper<DataTable> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DataTable::getTableName, tableName)
                .eq(DataTable::getAppId, appId)
                .last("limit 1");
        final DataTable dataTable = this.selectOne(wrapper);
        return dataTable;
    }

    /**
     * 更新为删除状态
     *
     * @param tableId
     * @return
     */
    default Integer updateDeleteStatus(final Long tableId) {
        Check.notNull(tableId, "数据表格ID不能为空");
        final DataTable updateDataTable = new DataTable();
        updateDataTable.setStatus(DataStatusEnum.DELETE.getValue());
        updateDataTable.setSyncStatus(DataSyncStatusEnum.UN_SYNCHRONIZED.getValue());

        // 更新条件
        final LambdaQueryWrapper<DataTable> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(DataTable::getStatus, DataStatusEnum.DELETE.getValue())
                .eq(DataTable::getId, tableId);
        final int updateNum = this.update(updateDataTable, wrapper);
        return updateNum;
    }

}
