package com.timecarol.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.timecarol.zhxy.pojo.Teacher;
import com.timecarol.zhxy.service.TeacherService;
import com.timecarol.zhxy.util.MD5;
import com.timecarol.zhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "教师控制器")
@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @ApiOperation("分页模糊查询教师信息")
    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result<Object> getTeachers(@ApiParam("页码数") @PathVariable("pageNo") Long pageNo,
                                      @ApiParam("页大小") @PathVariable("pageSize") Long pageSize,
                                      @ApiParam("教师名称(可选)") @RequestParam(value = "name", required = false) String name,
                                      @ApiParam("班级名称(可选)") @RequestParam(value = "clazzName", required = false) String clazzName) {
        Page<Teacher> page = new Page<>(pageNo, pageSize);
        IPage<Teacher> iPage = teacherService.getTeachers(page, name, clazzName);
        return Result.ok(iPage);
    }

    @ApiOperation("添加或删除教师信息")
    @PostMapping("/saveOrUpdateTeacher")
    public Result<Object> saveOrUpdateTeacher(
            @ApiParam("Teacher对象的JSON格式字符串") @RequestBody Teacher teacher) {
        if (teacher.getId() == null || teacher.getId() <= 0) {
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }
        if (teacherService.saveOrUpdate(teacher))
            return Result.ok();
        else
            return Result.fail();
    }

    @ApiOperation("删除教师信息")
    @DeleteMapping("/deleteTeacher")
    public Result<Object> deleteTeacher(
            @ApiParam("需要删除的教师信息的id的JSON格式的集合") @RequestBody List<Long> ids) {
        if (teacherService.removeByIds(ids))
            return Result.ok();
        else
            return Result.fail();
    }
}
