package com.timecarol.zhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.timecarol.zhxy.mapper.StudentMapper;
import com.timecarol.zhxy.pojo.LoginForm;
import com.timecarol.zhxy.pojo.Student;
import com.timecarol.zhxy.service.StudentService;
import com.timecarol.zhxy.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    private StudentMapper studentMapper;
    @Override
    public Student login(LoginForm loginForm) {
        loginForm.setPassword(MD5.encrypt(loginForm.getPassword()));
        return studentMapper.selectOne(new QueryWrapper<Student>()
                .eq("name", loginForm.getUsername())
                .eq("password", loginForm.getPassword()));
    }

    @Override
    public IPage<Student> getStudentByOpr(Page<Student> page, String clazzName, String name) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(clazzName)) {
            queryWrapper.like("clazz_name", clazzName);
        }
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByDesc("id");
        return studentMapper.selectPage(page, queryWrapper);
    }
}
