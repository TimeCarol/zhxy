package com.timecarol.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.timecarol.zhxy.pojo.Admin;
import com.timecarol.zhxy.service.AdminService;
import com.timecarol.zhxy.util.MD5;
import com.timecarol.zhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "管理员控制器")
@RestController
@RequestMapping("/sms/adminController")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @ApiOperation("分页模糊查询管理员信息")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result<Object> getAllAdmin(@ApiParam("页码数") @PathVariable("pageNo") Long pageNo,
                                      @ApiParam("页大小") @PathVariable("pageSize") Long pageSize,
                                      @ApiParam("管理员名称(可选)") @RequestParam(value = "adminName", required = false) String adminName) {
        Page<Admin> page = new Page<>(pageNo, pageSize);
        IPage<Admin> iPage = adminService.getAllAdmin(page, adminName);
        return Result.ok(iPage);
    }

    @ApiOperation("添加或删除管理员")
    @PostMapping("/saveOrUpdateAdmin")
    public Result<Object> saveOrUpdateAdmin(
            @ApiParam("Admin对象的JSON字符串") @RequestBody Admin admin) {
        if (admin.getId() == null || admin.getId() <= 0) {
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        if (adminService.saveOrUpdate(admin))
            return Result.ok();
        else
            return Result.fail();
    }

    @ApiOperation("删除管理员")
    @DeleteMapping("/deleteAdmin")
    public Result<Object> deleteAdmin(
            @ApiParam("需要删除的管理员id的JSON格式集合") @RequestBody List<Long> ids) {
        if (adminService.removeByIds(ids))
            return Result.ok();
        else
            return Result.fail();
    }
}
