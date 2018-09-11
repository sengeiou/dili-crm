package com.dili.dp.service.impl;

import com.dili.dp.dao.CategoryMapper;
import com.dili.dp.domain.Category;
import com.dili.dp.service.CategoryService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 14:29:31.
 */
@Service
public class CategoryServiceImpl extends BaseServiceImpl<Category, Long> implements CategoryService {

    public CategoryMapper getActualDao() {
        return (CategoryMapper)getDao();
    }
}