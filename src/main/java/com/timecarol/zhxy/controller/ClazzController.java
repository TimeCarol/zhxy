package com.timecarol.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.timecarol.zhxy.pojo.Clazz;
import com.timecarol.zhxy.service.ClazzService;
import com.timecarol.zhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "班级控制器")
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    @ApiOperation("获得所有班级信息")
    @GetMapping("/getClazzs")
    public Result<Object> getClazzs() {
        List<Clazz> list = clazzService.list();
        return Result.ok(list);
    }

    @ApiOperation("分页带条件模糊查询班级信息")
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result<Object> getClazzsByOpr(@ApiParam("分页查询的页码数") @PathVariable("pageNo") Long pageNo,
                                         @ApiParam("分页查询的页大小") @PathVariable("pageSize") Long pageSize,
                                         @ApiParam("年级名称(可选)") @RequestParam(value = "gradeName", required = false) String gradeName,
                                         @ApiParam("班级名称(可选)") @RequestParam(value = "name", required = false) String name) {
        Page<Clazz> page = new Page<>(pageNo, pageSize);
        IPage<Clazz> iPage = clazzService.getClazzsByOpr(page, gradeName, name);
        return Result.ok(iPage);
    }

    @ApiOperation("增加或者修改班级信息, 带id是修改, 不带是增加")
    @PostMapping("/saveOrUpdateClazz")
    public Result<Object> saveOrUpdateClazz(
            @ApiParam("Clazz对象的JSON格式") @RequestBody Clazz clazz) {
        boolean isSuccess = clazzService.saveOrUpdate(clazz);
        if (isSuccess)
            return Result.ok();
        else
            return Result.fail();
    }

    @ApiOperation("删除班级信息")
    @DeleteMapping("/deleteClazz")
    public Result<Object> deleteClazz(
            @ApiParam("需要删除的id的JSON格式集合") @RequestBody List<Long> ids) {
        boolean isRemove = clazzService.removeByIds(ids);
        if (isRemove)
            return Result.ok();
        else
            return Result.fail();
    }
}
