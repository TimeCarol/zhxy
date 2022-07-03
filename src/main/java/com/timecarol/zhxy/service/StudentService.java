package com.timecarol.zhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.timecarol.zhxy.pojo.Admin;
import com.timecarol.zhxy.pojo.LoginForm;
import com.timecarol.zhxy.pojo.Student;

public interface StudentService extends IService<Student> {
    public abstract Student login(LoginForm loginForm);

    public abstract IPage<Student> getStudentByOpr(Page<Student> page, String clazzName, String name);
}
