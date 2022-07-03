package com.timecarol.zhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.timecarol.zhxy.pojo.Clazz;

import java.util.List;

public interface ClazzService extends IService<Clazz> {
    public abstract IPage<Clazz> getClazzsByOpr(Page<Clazz> page, String gradeName, String name);
}
