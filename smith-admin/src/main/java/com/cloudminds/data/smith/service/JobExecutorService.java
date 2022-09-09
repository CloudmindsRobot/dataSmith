package com.cloudminds.data.smith.service;

import com.cloudminds.data.smith.dao.entity.DataTable;
import com.cloudminds.data.smith.dto.PageResult;
import com.cloudminds.data.smith.dto.req.JobLogQueryReqDTO;
import com.cloudminds.data.smith.dto.resp.JobLogItemRespDTO;

import java.util.List;

/**
 * 任务执行
 *
 * @author Tao.Liu
 * @date 2022/7/1 17:13
 */
public interface JobExecutorService {

    /**
     * 查询执行日志分页列表
     *
     * @param queryReqDTO
     * @return
     */
    PageResult<JobLogItemRespDTO> pageJobLogList(JobLogQueryReqDTO queryReqDTO);

    /**
     * 同步表格数据
     *
     * @param tableId     表格ID
     * @param triggerType 触发类型，1手动触发，2自动触发
     * @return
     */
    void executeJob(Long tableId, Integer triggerType);

    /**
     * 执行任务
     *
     * @param dataTable
     * @param triggerType
     */
    void executeJob(DataTable dataTable, Integer triggerType);

    /**
     * 验证cron表达式，并获取最近5次的执行时间
     *
     * @param cron
     * @return
     */
    List<Long> validateCron(String cron);

    /**
     * 添加调度任务
     *
     * @param tableId
     * @param cron
     */
    void addSchedule(Long tableId, String cron);

    /**
     * 移除调试任务
     *
     * @param tableId
     */
    void removeSchedule(Long tableId);


}
