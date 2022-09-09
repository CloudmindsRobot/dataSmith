package com.cloudminds.data.smith.controller;

import com.cloudminds.data.smith.dto.PageResult;
import com.cloudminds.data.smith.dto.R;
import com.cloudminds.data.smith.dto.req.DataModelQueryReqDTO;
import com.cloudminds.data.smith.dto.req.DataModelSaveReqDTO;
import com.cloudminds.data.smith.dto.req.DataModelValidReqDTO;
import com.cloudminds.data.smith.dto.resp.DataModelDetailRespDTO;
import com.cloudminds.data.smith.dto.resp.DataModelItemRespDTO;
import com.cloudminds.data.smith.service.DataModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 数据模型操作
 *
 * @author Tao.Liu
 * @date 2022/6/29 14:55
 */
@Api(tags = "数据模型接口")
@RestController
@RequestMapping("v1/dataModel")
public class DataModelController {

    @Resource
    private DataModelService DataModelService;

    /**
     * 查询数据模型分页列表
     *
     * @param queryReqDTO
     * @return
     */
    @ApiOperation("查询数据模型分页列表")
    @GetMapping("pageList")
    public R<PageResult<DataModelItemRespDTO>> pageList(@Validated final DataModelQueryReqDTO queryReqDTO) {
        final PageResult<DataModelItemRespDTO> pageResult = DataModelService.findPageList(queryReqDTO);
        return R.success(pageResult);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @ApiOperation("查询详情")
    @GetMapping("getDetailById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "模型ID", dataType = "query", paramType = "Integer"),
    })
    public R<DataModelDetailRespDTO> getDetailById(@Validated final Integer id) {
        final DataModelDetailRespDTO detailRespDTO = DataModelService.findById(id);
        return R.success(detailRespDTO);
    }

    @ApiOperation("验证数据模型配置")
    @PostMapping("validateConfig")
    public R<List<Map<String, Object>>> validateConfig(@RequestBody @Validated final DataModelValidReqDTO validReqDTO) {
        final List<Map<String, Object>> respDTOList = DataModelService.validateConfig(validReqDTO);
        return R.success(respDTOList);
    }

    /**
     * 保存数据模型
     *
     * @param saveReqDTO
     * @return
     */
    @ApiOperation("保存数据模型")
    @PostMapping("save")
    public R<Integer> save(@RequestBody @Validated final DataModelSaveReqDTO saveReqDTO) {
        final Integer DataModelId = DataModelService.saveDataModel(saveReqDTO);
        return R.success(DataModelId);
    }

    /**
     * 删除数据模型
     *
     * @param id
     */
    @ApiOperation("删除数据模型")
    @PostMapping("deleteById")
    public R<Void> deleteById(@RequestParam Integer id) {
        DataModelService.deleteById(id);
        return R.success();
    }

}
