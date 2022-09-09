package com.cloudminds.data.smith.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.cloudminds.data.smith.dao.entity.DataModel;
import com.cloudminds.data.smith.dto.PageResult;
import com.cloudminds.data.smith.dto.req.DataModelQueryReqDTO;
import com.cloudminds.data.smith.util.Strings;

/**
 * <p>
 * 数据模型 Mapper 接口
 * </p>
 *
 * @author Tao.liu
 * @since 2022-06-29
 */
public interface DataModelMapper extends BaseMapper<DataModel> {

    /**
     * 查询据模型分页列表
     *
     * @param queryReqDTO
     * @return
     */
    default PageResult<DataModel> findPageList(final DataModelQueryReqDTO queryReqDTO) {
        final LambdaQueryWrapper<DataModel> wrapper = Wrappers.lambdaQuery();
        if (Strings.isNotBlank(queryReqDTO.getName())) {
            wrapper.like(DataModel::getName, queryReqDTO.getName());
        }
        if (queryReqDTO.getSourceId() != null) {
            wrapper.eq(DataModel::getSourceId, queryReqDTO.getSourceId());
        }
        wrapper.orderByDesc(DataModel::getUpdateTime);
        final PageDTO<DataModel> page = this.selectPage(new PageDTO<>(queryReqDTO.getCurrent(), queryReqDTO.getSize()), wrapper);
        return PageResult.of(page);
    }

}
