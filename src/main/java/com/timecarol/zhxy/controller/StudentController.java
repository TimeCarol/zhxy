package com.timecarol.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.timecarol.zhxy.pojo.Student;
import com.timecarol.zhxy.service.StudentService;
import com.timecarol.zhxy.util.MD5;
import com.timecarol.zhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "学生控制器")
@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @ApiOperation("带条件分页模糊查询学生信息")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result<Object> getStudentByOpr(@ApiParam("分页页码") @PathVariable("pageNo") Long pageNo,
                                          @ApiParam("每页数量") @PathVariable("pageSize") Long pageSize,
                                          @ApiParam("班级名称(可选)") @RequestParam(value = "clazzName", required = false) String clazzName,
                                          @ApiParam("学生名称(可选)") @RequestParam(value = "name", required = false) String name) {
        Page<Student> page = new Page<>(pageNo, pageSize);
        IPage<Student> iPage = studentService.getStudentByOpr(page, clazzName, name);
        return Result.ok(iPage);
    }

    @ApiOperation("增加或修改学生信息")
    @PostMapping("/addOrUpdateStudent")
    public Result<Object> addOrUpdateStudent(
            @ApiParam("Student对象的JSON字符串") @RequestBody Student student) {
        //如果是增加操作需要对密码进行加密
        if (student.getId() == null || student.getId() <= 0) {
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        boolean isSuccess = studentService.saveOrUpdate(student);
        if (isSuccess)
            return Result.ok();
        else
            return Result.fail();
    }

    @ApiOperation("删除学生信息")
    @DeleteMapping("delStudentById")
    public Result<Object> delStudentById(
            @ApiParam("要删除的JSON格式的学生id") @RequestBody List<Long> ids) {
        if (studentService.removeByIds(ids))
            return Result.ok();
        else
            return Result.fail();
    }
}
