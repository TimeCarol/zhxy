package com.timecarol.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.timecarol.zhxy.pojo.Grade;
import com.timecarol.zhxy.service.GradeService;
import com.timecarol.zhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "年级控制器")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @ApiOperation("查询出所有年级信息")
    @GetMapping("/getGrades")
    public Result<Object> getGrades() {
        List<Grade> list = gradeService.list();
        return Result.ok(list);
    }

    @ApiOperation("根据年级名称模糊查询, 带分页")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result<Object> getGrades(@ApiParam("分页查询的页码数") @PathVariable(value = "pageNo") Long pageNo,
                                    @ApiParam("分页查询的页大小") @PathVariable(value = "pageSize") Long pageSize,
                                    @ApiParam("分页查询的年级名称(可选)") @RequestParam(value = "gradeName", required = false) String gradeName) {

        //分页带条件查询
        Page<Grade> page = new Page<>(pageNo, pageSize);
        //通过服务层查询
        IPage<Grade> pageRs = gradeService.getGradeByOpr(page, gradeName);
        //封装Result对象并返回
        return Result.ok(pageRs);
    }

    @ApiOperation("新增或修改Grade, 如果有id属性, 则进行修改操作, 没有则进行增加")
    @PostMapping("/saveOrUpdateGrade")
    public Result<Object> saveOrUpdateGrade(
            @ApiParam("JSON格式的Grade对象") @RequestBody Grade grade) {
        boolean isSuccess;
        if (grade.getId() != null) { //更新操作
            isSuccess = gradeService.updateById(grade);
        } else { //添加操作
            isSuccess = gradeService.save(grade);
        }
        if (isSuccess)
            return Result.ok();
        else
            return Result.fail();
    }

    @ApiOperation("删除Grade信息")
    @DeleteMapping("/deleteGrade")
    public Result<Object> deleteGrade(
            @ApiParam("要删除的所有的Grade的id的JSON集合") @RequestBody List<Long> ids) {
        boolean isRemove = gradeService.removeByIds(ids);
        if (isRemove)
            return Result.ok();
        else
            return Result.fail();
    }
}
