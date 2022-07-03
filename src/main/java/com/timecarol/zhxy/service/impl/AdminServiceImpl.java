package com.timecarol.zhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.timecarol.zhxy.mapper.AdminMapper;
import com.timecarol.zhxy.pojo.Admin;
import com.timecarol.zhxy.pojo.LoginForm;
import com.timecarol.zhxy.service.AdminService;
import com.timecarol.zhxy.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Override
    public Admin login(LoginForm loginForm) {
        loginForm.setPassword(MD5.encrypt(loginForm.getPassword()));
        return adminMapper.selectOne(new QueryWrapper<Admin>()
                .eq("name", loginForm.getUsername())
                .eq("password", loginForm.getPassword())
        );
    }

    @Override
    public IPage<Admin> getAllAdmin(Page<Admin> page, String adminName) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(adminName)) {
            queryWrapper.like("name", adminName);
        }
        queryWrapper.orderByDesc("id");
        return adminMapper.selectPage(page, queryWrapper);
    }
}
