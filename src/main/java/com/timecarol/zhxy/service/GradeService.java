package com.timecarol.zhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.timecarol.zhxy.pojo.Clazz;
import com.timecarol.zhxy.pojo.Grade;

public interface GradeService extends IService<Grade> {
    public abstract IPage<Grade> getGradeByOpr(Page<Grade> page, String gradeName);
}
