package com.cloudminds.data.smith.controller;

import com.cloudminds.data.smith.dto.PageResult;
import com.cloudminds.data.smith.dto.R;
import com.cloudminds.data.smith.dto.req.DataSourceQueryReqDTO;
import com.cloudminds.data.smith.dto.req.DataSourceSaveReqDTO;
import com.cloudminds.data.smith.dto.req.DataSourceValidateReqDTO;
import com.cloudminds.data.smith.dto.resp.DataModelColumnRespDTO;
import com.cloudminds.data.smith.dto.resp.DataModelSchemaRespDTO;
import com.cloudminds.data.smith.dto.resp.DataSourceItemRespDTO;
import com.cloudminds.data.smith.service.DataBaseService;
import com.cloudminds.data.smith.service.DataSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据源操作
 *
 * @author Tao.Liu
 * @date 2022/6/29 14:55
 */
@Api(tags = "数据源接口")
@RestController
@RequestMapping("v1/dataSource")
public class DataSourceController {

    @Resource
    private DataSourceService dataSourceService;
    @Resource
    private DataBaseService dataBaseService;

    /**
     * 查询数据源分页列表
     *
     * @param queryReqDTO
     * @return
     */
    @ApiOperation("查询数据源分页列表")
    @GetMapping("pageList")
    public R<PageResult<DataSourceItemRespDTO>> pageList(@Validated final DataSourceQueryReqDTO queryReqDTO) {
        final PageResult<DataSourceItemRespDTO> pageResult = dataSourceService.findPageList(queryReqDTO);
        return R.success(pageResult);
    }

    /**
     * 数据源验证
     *
     * @param validateReqDTO
     * @return
     */
    @ApiOperation("验证数据源")
    @PostMapping("validate")
    public R<Boolean> validate(@RequestBody @Validated final DataSourceValidateReqDTO validateReqDTO) {
        dataSourceService.validateDataSource(validateReqDTO);
        return R.success(true);
    }


    /**
     * 保存数据源
     *
     * @param saveReqDTO
     * @return
     */
    @ApiOperation("保存数据源")
    @PostMapping("save")
    public R<Integer> save(@RequestBody @Validated final DataSourceSaveReqDTO saveReqDTO) {
        final Integer dataSourceId = dataSourceService.saveDataSource(saveReqDTO);
        return R.success(dataSourceId);
    }

    /**
     * 删除数据源
     *
     * @param id
     */
    @ApiOperation("删除数据源")
    @PostMapping("deleteById")
    public R<Void> deleteById(@RequestParam Integer id) {
        dataSourceService.deleteById(id);
        return R.success();
    }

    /**
     * 获取数据源库列表
     *
     * @param dataSourceId
     * @return
     */
    @ApiOperation("获取数据源库名列表")
    @GetMapping("getSchemas")
    @ApiImplicitParam(name = "dataSourceId", value = "数据源ID", dataType = "query", paramType = "Integer")
    public R<List<String>> getSchemas(@RequestParam Integer dataSourceId) {
        final List<String> databaseList = dataBaseService.getSchemas(dataSourceId);
        return R.success(databaseList);
    }

    /**
     * 获取数据库表名
     *
     * @param dataSourceId
     * @param catalog
     * @return
     */
    @ApiOperation("获取数据源表名列表")
    @GetMapping("getTables")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSourceId", value = "数据源ID", dataType = "query", paramType = "Integer"),
            @ApiImplicitParam(name = "schema", value = "数据库名", dataType = "query", paramType = "String"),
    })
    public R<List<String>> getTables(@RequestParam Integer dataSourceId, @RequestParam String schema) {
        final List<String> tableList = dataBaseService.getTableList(dataSourceId, schema);
        return R.success(tableList);
    }

    /**
     * 获取数据库表名
     *
     * @param dataSourceId
     * @param catalog
     * @return
     */
    @ApiOperation("获取数据源表字段列表")
    @GetMapping("getTableColumns")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSourceId", value = "数据源ID", dataType = "query", paramType = "Integer"),
            @ApiImplicitParam(name = "schema", value = "数据库名", dataType = "query", paramType = "String"),
            @ApiImplicitParam(name = "table", value = "表名", dataType = "query", paramType = "String"),
    })
    public R<List<DataModelColumnRespDTO>> getTableColumns(@RequestParam Integer dataSourceId, @RequestParam String schema, @RequestParam String table) {
        final List<DataModelColumnRespDTO> columns = dataBaseService.getTableColumns(dataSourceId, schema, table);
        return R.success(columns);
    }


    /**
     * 获取数据源全部表名列表
     *
     * @param dataSourceId
     * @return
     */
    @ApiOperation("获取数据源全部表名列表")
    @GetMapping("getAllTables")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSourceId", value = "数据源ID", dataType = "query", paramType = "Integer"),
    })
    public R<List<DataModelSchemaRespDTO>> getAllTables(@RequestParam Integer dataSourceId) {
        final List<DataModelSchemaRespDTO> tableList = dataBaseService.getAllTableList(dataSourceId);
        return R.success(tableList);
    }

}
