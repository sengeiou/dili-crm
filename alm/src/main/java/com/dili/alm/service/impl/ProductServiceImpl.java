package com.dili.alm.service.impl;

import com.dili.alm.dao.ProductMapper;
import com.dili.alm.domain.Product;
import com.dili.alm.service.ProductService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-06-28 15:51:42.
 */
@Service
public class ProductServiceImpl extends BaseServiceImpl<Product, Long> implements ProductService {

    public ProductMapper getActualDao() {
        return (ProductMapper)getDao();
    }
}