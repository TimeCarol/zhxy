package com.timecarol.zhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.timecarol.zhxy.pojo.Admin;
import com.timecarol.zhxy.pojo.LoginForm;
import com.timecarol.zhxy.pojo.Teacher;

public interface TeacherService extends IService<Teacher> {
    public abstract Teacher login(LoginForm loginForm);

    public abstract IPage<Teacher> getTeachers(Page<Teacher> page, String name, String clazzName);
}
