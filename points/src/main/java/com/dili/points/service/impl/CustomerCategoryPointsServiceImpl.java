package com.dili.points.service.impl;

import com.dili.points.dao.CustomerCategoryPointsMapper;
import com.dili.points.domain.CustomerCategoryPoints;
import com.dili.points.service.CustomerCategoryPointsService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-09 17:35:45.
 */
@Service
public class CustomerCategoryPointsServiceImpl extends BaseServiceImpl<CustomerCategoryPoints, Long> implements CustomerCategoryPointsService {

    public CustomerCategoryPointsMapper getActualDao() {
        return (CustomerCategoryPointsMapper) getDao();
    }

    @Override
    public int updateCustomerName(String name, Long id) {
        CustomerCategoryPoints customerCategoryPoints = DTOUtils.newDTO(CustomerCategoryPoints.class);
        customerCategoryPoints.setName(name);

        Example example = new Example(CustomerCategoryPoints.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("customerId", id);
        getActualDao().updateByExampleSelective(customerCategoryPoints, example);
        return 1;
    }
}