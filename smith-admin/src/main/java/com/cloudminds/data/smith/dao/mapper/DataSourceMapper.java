package com.cloudminds.data.smith.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.cloudminds.data.smith.constant.DataStatusEnum;
import com.cloudminds.data.smith.dao.entity.DataSource;
import com.cloudminds.data.smith.dto.PageResult;
import com.cloudminds.data.smith.dto.req.DataSourceQueryReqDTO;
import com.cloudminds.data.smith.util.Strings;

/**
 * <p>
 * 数据源 Mapper 接口
 * </p>
 *
 * @author Tao.liu
 * @since 2022-06-29
 */
public interface DataSourceMapper extends BaseMapper<DataSource> {

    /**
     * 查询数据源分页列表
     *
     * @param queryReqDTO
     * @return
     */
    default PageResult<DataSource> findPageList(final DataSourceQueryReqDTO queryReqDTO) {
        final LambdaQueryWrapper<DataSource> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(DataSource::getStatus, DataStatusEnum.DELETE.getValue());

        if (Strings.isNotBlank(queryReqDTO.getName())) {
            wrapper.like(DataSource::getName, queryReqDTO.getName());
        }
        if (queryReqDTO.getType() != null) {
            wrapper.eq(DataSource::getType, queryReqDTO.getType());
        }
        wrapper.orderByDesc(DataSource::getUpdateTime);
        final PageDTO<DataSource> page = this.selectPage(new PageDTO<>(queryReqDTO.getCurrent(), queryReqDTO.getSize()), wrapper);
        return PageResult.of(page);
    }

}
