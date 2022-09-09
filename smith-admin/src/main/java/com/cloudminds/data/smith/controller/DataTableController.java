package com.cloudminds.data.smith.controller;

import com.cloudminds.data.smith.dto.PageResult;
import com.cloudminds.data.smith.dto.R;
import com.cloudminds.data.smith.dto.req.DataFieldDeleteReqDTO;
import com.cloudminds.data.smith.dto.req.DataTableQueryReqDTO;
import com.cloudminds.data.smith.dto.req.DataTableSaveReqDTO;
import com.cloudminds.data.smith.dto.req.JobLogQueryReqDTO;
import com.cloudminds.data.smith.dto.resp.DataTableDetailRespDTO;
import com.cloudminds.data.smith.dto.resp.DataTableItemRespDTO;
import com.cloudminds.data.smith.dto.resp.JobLogItemRespDTO;
import com.cloudminds.data.smith.service.DataTableService;
import com.cloudminds.data.smith.service.JobExecutorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据表格操作
 *
 * @author Tao.Liu
 * @date 2022/6/29 14:55
 */
@Api(tags = "数据表格接口")
@RestController
@RequestMapping("v1/dataTable")
public class DataTableController {

    @Resource
    private DataTableService dataTableService;
    @Resource
    private JobExecutorService jobExecutorService;

    /**
     * 查询数据表格分页列表
     *
     * @param queryReqDTO
     * @return
     */
    @ApiOperation("查询数据表格分页列表")
    @GetMapping("pageList")
    public R<PageResult<DataTableItemRespDTO>> pageList(@Validated final DataTableQueryReqDTO queryReqDTO) {
        final PageResult<DataTableItemRespDTO> pageResult = dataTableService.findPageList(queryReqDTO);
        return R.success(pageResult);
    }

    /**
     * 查询详情
     *
     * @param tableId
     * @return
     */
    @ApiOperation("查询详情")
    @GetMapping("getDetailById")
    public R<DataTableDetailRespDTO> getDetailById(@RequestParam Long tableId) {
        final DataTableDetailRespDTO detailRespDTO = dataTableService.findDetailById(tableId);
        return R.success(detailRespDTO);
    }

    /**
     * 保存数据表格
     *
     * @param saveReqDTO
     * @return
     */
    @ApiOperation("保存数据表格")
    @PostMapping("save")
    public R<Long> save(@RequestBody @Validated final DataTableSaveReqDTO saveReqDTO) {
        final Long tableId = dataTableService.saveDataTable(saveReqDTO);
        return R.success(tableId);
    }

    /**
     * 删除数据表格
     *
     * @param id
     */
    @ApiOperation("删除数据表格")
    @PostMapping("deleteById")
    public R<Void> deleteById(@RequestParam Long id) {
        dataTableService.deleteById(id);
        return R.success();
    }

    /**
     * 删除数据表格
     *
     * @param deleteReqDTO
     * @return
     */
    @ApiOperation("批量删除字段")
    @PostMapping("deleteFields")
    public R<Void> deleteFields(@RequestBody @Validated DataFieldDeleteReqDTO deleteReqDTO) {
        dataTableService.deleteFields(deleteReqDTO.getTableId(), deleteReqDTO.getFieldIds());
        return R.success();
    }

    /**
     * 验证cron表达式, 并获取最近5次的执行时间
     *
     * @param cron
     * @return
     */
    @ApiOperation("验证cron表达式, 并获取最近5次的执行时间")
    @GetMapping("validateCron")
    public R<List<Long>> validateCron(@RequestParam String cron) {
        final List<Long> timeList = jobExecutorService.validateCron(cron);
        return R.success(timeList);
    }

    /**
     * 同步数据表格记录
     *
     * @param tableId
     * @return
     */
    @ApiOperation("手动执行表格数据同步")
    @PostMapping("executeJob")
    public R<Void> executeJob(@RequestParam Long tableId) {
        jobExecutorService.executeJob(tableId, 1);
        return R.success();
    }

    /**
     * 查询数据模型分页列表
     *
     * @param queryReqDTO
     * @return
     */
    @ApiOperation("查询执行日志分页列表")
    @GetMapping("pageJobLogList")
    public R<PageResult<JobLogItemRespDTO>> pageJobLogList(@Validated final JobLogQueryReqDTO queryReqDTO) {
        final PageResult<JobLogItemRespDTO> pageResult = jobExecutorService.pageJobLogList(queryReqDTO);
        return R.success(pageResult);
    }


}
