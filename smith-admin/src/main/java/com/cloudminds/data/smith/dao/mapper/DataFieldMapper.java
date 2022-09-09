package com.cloudminds.data.smith.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cloudminds.data.smith.constant.DataStatusEnum;
import com.cloudminds.data.smith.constant.DataSyncStatusEnum;
import com.cloudminds.data.smith.dao.entity.DataField;
import com.cloudminds.data.smith.util.Check;
import com.cloudminds.data.smith.util.Lists;

import java.util.List;

/**
 * <p>
 * 数据表格字段 Mapper 接口
 * </p>
 *
 * @author Tao.liu
 * @since 2022-06-29
 */
public interface DataFieldMapper extends BaseMapper<DataField> {

    /**
     * 根据表格ID查询列表
     *
     * @param tableId
     * @return
     */
    default List<DataField> findListByTableId(final Long tableId) {
        Check.notNull(tableId, "数据表格ID不能为空", tableId);
        final LambdaQueryWrapper<DataField> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(DataField::getStatus, DataStatusEnum.DELETE.getValue())
                .eq(DataField::getTableId, tableId);
        wrapper.orderByAsc(DataField::getSortNo).orderByAsc(DataField::getId);
        final List<DataField> fieldList = this.selectList(wrapper);
        return fieldList;
    }

    /**
     * 查询未同步的字段列表
     *
     * @param tableId
     * @return
     */
    default List<DataField> findUnSyncListByTableId(final Long tableId) {
        Check.notNull(tableId, "数据表格ID不能为空", tableId);
        final LambdaQueryWrapper<DataField> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DataField::getSyncStatus, DataSyncStatusEnum.UN_SYNCHRONIZED.getValue())
                .eq(DataField::getTableId, tableId);
        wrapper.orderByAsc(DataField::getSortNo).orderByAsc(DataField::getId);
        final List<DataField> fieldList = this.selectList(wrapper);
        return fieldList;
    }

    /**
     * 更新字段为删除状态
     *
     * @param tableId
     * @return
     */
    default Integer updateDeleteStatusByTableId(final Long tableId) {
        Check.notNull(tableId, "数据表格ID不能为空", tableId);
        final DataField updateDataField = new DataField();
        updateDataField.setStatus(DataStatusEnum.DELETE.getValue());
        updateDataField.setSyncStatus(DataSyncStatusEnum.UN_SYNCHRONIZED.getValue());

        // 更新条件
        final LambdaQueryWrapper<DataField> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(DataField::getStatus, DataStatusEnum.DELETE.getValue())
                .eq(DataField::getTableId, tableId);
        final int updateNum = this.update(updateDataField, wrapper);
        return updateNum;
    }

    /**
     * 根据表格ID删除未同步的字段
     *
     * @param tableId
     * @return
     */
    default Integer deleteUnSyncListByTableId(final Long tableId) {
        Check.notNull(tableId, "数据表格ID不能为空", tableId);
        final LambdaQueryWrapper<DataField> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DataField::getSyncStatus, DataSyncStatusEnum.UN_SYNCHRONIZED.getValue())
                .eq(DataField::getTableId, tableId)
                .eq(DataField::getStatus, DataStatusEnum.DELETE.getValue());
        final int deleteNum = this.delete(wrapper);
        return deleteNum;
    }

    /**
     * 更新多个字段为删除状态
     *
     * @param fieldIds
     * @return
     */
    default Integer updateDeleteStatusByIds(final List<Long> fieldIds) {
        if (Lists.isEmpty(fieldIds)) {
            return 0;
        }
        final DataField updateDataField = new DataField();
        updateDataField.setStatus(DataStatusEnum.DELETE.getValue());
        updateDataField.setSyncStatus(DataSyncStatusEnum.UN_SYNCHRONIZED.getValue());

        // 更新条件
        final LambdaQueryWrapper<DataField> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(DataField::getStatus, DataStatusEnum.DELETE.getValue())
                .in(DataField::getId, fieldIds);
        final int updateNum = this.update(updateDataField, wrapper);
        return updateNum;
    }

}
