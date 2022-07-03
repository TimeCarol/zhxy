package com.timecarol.zhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.timecarol.zhxy.pojo.Admin;
import com.timecarol.zhxy.pojo.LoginForm;

public interface AdminService extends IService<Admin> {
    public abstract Admin login(LoginForm loginForm);

    public abstract IPage<Admin> getAllAdmin(Page<Admin> page, String adminName);
}
