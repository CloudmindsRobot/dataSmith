package com.cloudminds.data.smith.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.cron.CronException;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.pattern.CronPatternUtil;
import com.cloudminds.data.smith.constant.DataStatusEnum;
import com.cloudminds.data.smith.dao.entity.DataField;
import com.cloudminds.data.smith.dao.entity.DataTable;
import com.cloudminds.data.smith.dao.entity.JobLog;
import com.cloudminds.data.smith.dao.mapper.DataFieldMapper;
import com.cloudminds.data.smith.dao.mapper.DataTableMapper;
import com.cloudminds.data.smith.dao.mapper.JobLogMapper;
import com.cloudminds.data.smith.dto.PageResult;
import com.cloudminds.data.smith.dto.req.JobLogQueryReqDTO;
import com.cloudminds.data.smith.dto.resp.JobLogItemRespDTO;
import com.cloudminds.data.smith.exception.ParameterException;
import com.cloudminds.data.smith.external.feishu.bitable.dto.resp.TableRecordSyncRespDTO;
import com.cloudminds.data.smith.service.DataModelService;
import com.cloudminds.data.smith.service.DataTableSyncService;
import com.cloudminds.data.smith.service.JobExecutorService;
import com.cloudminds.data.smith.util.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 任务执行实现
 *
 * @author Tao.Liu
 * @date 2022/7/1 17:15
 */
@Slf4j
@Service
public class JobExecutorServiceImpl implements JobExecutorService, CommandLineRunner {

    @Resource
    private DataTableMapper dataTableMapper;
    @Resource
    private DataFieldMapper dataFieldMapper;
    @Resource
    private DataTableSyncService dataTableSyncService;
    @Resource
    private DataModelService dataModelService;
    @Resource
    private JobLogMapper jobLogMapper;
    @Lazy
    @Resource
    private JobExecutorService jobExecutorService;

    @Override
    public void run(final String... args) throws Exception {
        final List<DataTable> dataTableList = dataTableMapper.selectCronTableList(200);
        log.info("=================>>>>>>>>>> start同步数据表格定时器");
        for (final DataTable dataTable : dataTableList) {
            if (Strings.isBlank(dataTable.getCron())) {
                continue;
            }
            this.addSchedule(dataTable.getId(), dataTable.getCron());
        }
        CronUtil.setMatchSecond(Boolean.TRUE);
        CronUtil.start();
    }

    @Override
    public PageResult<JobLogItemRespDTO> pageJobLogList(final JobLogQueryReqDTO queryReqDTO) {
        final PageResult<JobLog> pageResult = jobLogMapper.selectPageList(queryReqDTO);
        return pageResult.convert(JobLogItemRespDTO.class);
    }

    @Override
    public void executeJob(final Long tableId, final Integer triggerType) {
        final DataTable dataTable = this.checkExecuteTable(tableId);
        jobExecutorService.executeJob(dataTable, triggerType);
    }

    @Async
    @Override
    public void executeJob(final DataTable dataTable, final Integer triggerType) {
        // 上次执行时间
        final Long lastExecuteTime = dataTable.getExecuteTime() == null ? 0 : dataTable.getExecuteTime();

        final Long startTime = System.currentTimeMillis();
        // 先更新执行时间
        final DataTable updateDataTable = new DataTable();
        updateDataTable.setId(dataTable.getId());
        updateDataTable.setExecuteTime(startTime);
        updateDataTable.setUpdateTime(System.currentTimeMillis());
        dataTableMapper.updateById(updateDataTable);

        final String appName = Strings.isBlank(dataTable.getAppName()) ? "" : (" - " + dataTable.getAppName());
        // 保存执行日志
        final JobLog jobLog = new JobLog();
        jobLog.setHandlerId(dataTable.getId());
        jobLog.setHandlerName(dataTable.getTableName() + appName);
        jobLog.setTriggerType(triggerType);
        jobLog.setUpdateTime(startTime);
        jobLog.setCreateTime(startTime);
        jobLog.setResult(0);
        jobLogMapper.insert(jobLog);

        // 更新记录
        final long executeTime = System.currentTimeMillis();
        final JobLog updateJobLog = new JobLog();
        updateJobLog.setId(jobLog.getId());
        try {
            final TableRecordSyncRespDTO syncRespDTO = this.execute(dataTable, lastExecuteTime);
            updateJobLog.setInsertNum(syncRespDTO.getInsertNum());
            updateJobLog.setUpdateNum(syncRespDTO.getUpdateNum());
            updateJobLog.setDeleteNum(syncRespDTO.getDeleteNum());
            updateJobLog.setResult(1);
        } catch (Exception e) {
            updateJobLog.setResult(2);
            updateJobLog.setCause(getStackTraceAsString(e));
            throw e;
        } finally {
            final long now = System.currentTimeMillis();
            updateJobLog.setCostTime((int) (now - executeTime));
            updateJobLog.setUpdateTime(now);
            jobLogMapper.updateById(updateJobLog);
        }
    }

    @Override
    public List<Long> validateCron(final String cron) {
        try {
            final List<Date> dateList = CronPatternUtil.matchedDates(cron, DateTime.now(), 5, true);
            final List<Long> resultList = dateList.stream().map(Date::getTime).collect(Collectors.toList());
            return resultList;
        } catch (CronException e) {
            throw new ParameterException(e.getMessage());
        }
    }


    /**
     * 执行任务
     *
     * @param dataTable
     * @param lastExecuteTime
     * @return
     */
    private TableRecordSyncRespDTO execute(final DataTable dataTable, final Long lastExecuteTime) {
        log.info("===========>>>>>>> start execute sync datable records task, tableId={}, name={}", dataTable.getId(), dataTable.getTableName());
        final List<DataField> fieldList = dataFieldMapper.findListByTableId(dataTable.getId());
        if (Lists.isEmpty(fieldList)) {
            throw new ParameterException("表格未配置字段");
        }
        // 同步飞书表格
        if (Objects.equals(dataTable.getAppType(), 1)) {
            final TableRecordSyncRespDTO syncRespDTO = this.syncFeishuRecords(dataTable, fieldList, lastExecuteTime);
            return syncRespDTO;
        } else {
            throw new ParameterException("未知应用类型【%s】", dataTable.getAppType());
        }
    }

    @Override
    public void addSchedule(final Long tableId, final String cron) {
        // 移除调度
        this.removeSchedule(tableId);
        if (Strings.isBlank(cron)) {
            return;
        }
        // 更新调度
        final String schedulerId = "job-" + tableId;
        CronUtil.schedule(schedulerId, cron, () -> {
            final DataTable table = this.checkExecuteTable(tableId);
            this.executeJob(table, 2);
        });
        log.info("加入执行任务，schedulerId: {}, cron=[{}]", schedulerId, cron);
    }

    @Override
    public void removeSchedule(final Long tableId) {
        final String schedulerId = "job-" + tableId;
        final boolean flag = CronUtil.remove(schedulerId);
        if (flag) {
            log.info("移除执行任务，schedulerId: {}", schedulerId);
        }
    }

    /**
     * 同步飞书记录
     *
     * @param dataTable
     * @param fieldList
     * @param lastExecuteTime
     * @return
     */
    private TableRecordSyncRespDTO syncFeishuRecords(final DataTable dataTable, final List<DataField> fieldList, final Long lastExecuteTime) {
        // 主键标识判定，如果没有主键，则每次都是全量新增数据
        final long primaryKeyCount = fieldList.stream().filter(e -> e.getPrimaryFlag()).count();
        // 执行SQL, 最多2W条
        final List<Map<String, Object>> recordList = dataModelService.queryForList(dataTable.getDataModelId(), lastExecuteTime, 20000);
        if (primaryKeyCount < 1 || lastExecuteTime == null || lastExecuteTime <= 0) {
            // 全量新增
            final int count = dataTableSyncService.insertRecords(dataTable, fieldList, recordList);
            return new TableRecordSyncRespDTO(count, 0, 0);
        }
        // 数据同步
        final TableRecordSyncRespDTO syncRespDTO = dataTableSyncService.syncRecords(dataTable, fieldList, recordList);
        return syncRespDTO;
    }

    /**
     * 校验需要执行的表格
     *
     * @param tableId
     * @return
     */
    private DataTable checkExecuteTable(final Long tableId) {
        // 查询表、字段定义
        final DataTable dataTable = dataTableMapper.selectById(tableId);
        if (dataTable == null || Objects.equals(dataTable.getStatus(), DataStatusEnum.DELETE.getValue())) {
            throw new ParameterException("无效的表格ID");
        }
        if (Strings.isBlank(dataTable.getAppTableId())) {
            throw new ParameterException("未同步飞书表格定义，请重新编辑目的地！");
        }
        return dataTable;
    }

    /**
     * 将ErrorStack转化为String.
     *
     * @param throwable
     * @return
     */
    private String getStackTraceAsString(final Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        final StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

}
