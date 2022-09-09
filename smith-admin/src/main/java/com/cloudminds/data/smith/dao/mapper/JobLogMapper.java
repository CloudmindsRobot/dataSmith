package com.cloudminds.data.smith.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.cloudminds.data.smith.dao.entity.JobLog;
import com.cloudminds.data.smith.dto.PageResult;
import com.cloudminds.data.smith.dto.req.JobLogQueryReqDTO;
import com.cloudminds.data.smith.util.Strings;

/**
 * <p>
 * 任务作业日志 Mapper 接口
 * </p>
 *
 * @author Tao.liu
 * @since 2022-07-06
 */
public interface JobLogMapper extends BaseMapper<JobLog> {

    /**
     * 查询分页列表
     *
     * @param queryReqDTO
     * @return
     */
    default PageResult<JobLog> selectPageList(final JobLogQueryReqDTO queryReqDTO) {
        final LambdaQueryWrapper<JobLog> wrapper = Wrappers.lambdaQuery();
        if (queryReqDTO.getHandlerId() != null) {
            wrapper.eq(JobLog::getHandlerId, queryReqDTO.getHandlerId());
        }
        if (Strings.isNotBlank(queryReqDTO.getHandlerName())) {
            wrapper.like(JobLog::getHandlerName, queryReqDTO.getHandlerName());
        }
        if (queryReqDTO.getTriggerType() != null) {
            wrapper.eq(JobLog::getTriggerType, queryReqDTO.getTriggerType());
        }
        if (queryReqDTO.getResult() != null) {
            wrapper.eq(JobLog::getResult, queryReqDTO.getResult());
        }
        if (queryReqDTO.getCreateStartTime() != null) {
            wrapper.ge(JobLog::getCreateTime, queryReqDTO.getCreateStartTime());
        }
        if (queryReqDTO.getCreateEndTime() != null) {
            wrapper.le(JobLog::getCreateTime, queryReqDTO.getCreateEndTime());
        }
        wrapper.orderByDesc(JobLog::getCreateTime);
        final PageDTO<JobLog> page = this.selectPage(new PageDTO<>(queryReqDTO.getCurrent(), queryReqDTO.getSize()), wrapper);
        return PageResult.of(page);
    }

}
