package com.timecarol.zhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.timecarol.zhxy.mapper.TeacherMapper;
import com.timecarol.zhxy.pojo.LoginForm;
import com.timecarol.zhxy.pojo.Teacher;
import com.timecarol.zhxy.service.TeacherService;
import com.timecarol.zhxy.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;
    @Override
    public Teacher login(LoginForm loginForm) {
        loginForm.setPassword(MD5.encrypt(loginForm.getPassword()));
        return teacherMapper.selectOne(new QueryWrapper<Teacher>()
                .eq("name", loginForm.getUsername())
                .eq("password", loginForm.getPassword()));
    }

    @Override
    public IPage<Teacher> getTeachers(Page<Teacher> page, String name, String clazzName) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(clazzName)) {
            queryWrapper.like("clazz_name", clazzName);
        }
        queryWrapper.orderByDesc("id");
        return teacherMapper.selectPage(page, queryWrapper);
    }
}
