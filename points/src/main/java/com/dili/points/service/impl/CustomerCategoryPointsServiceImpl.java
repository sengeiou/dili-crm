package com.dili.points.service.impl;

import com.dili.points.dao.CustomerCategoryPointsMapper;
import com.dili.points.domain.CustomerCategoryPoints;
import com.dili.points.domain.dto.CustomerCategoryPointsDTO;
import com.dili.points.service.CustomerCategoryPointsService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;

import java.util.Collections;
import java.util.List;

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
    @Override
    public EasyuiPageOutput listEasyuiPageByExample(CustomerCategoryPointsDTO customerCategoryPoints, boolean useProvider,List<String>firmCodes) throws Exception{
    	if(firmCodes.isEmpty()) {
    		return new EasyuiPageOutput(0,Collections.emptyList());
    	}
    	customerCategoryPoints.setFirmCodes(firmCodes);
    	return super.listEasyuiPageByExample(customerCategoryPoints, useProvider);
    }
    @Override
    public int[] batchSaveCustomerCategoryPointsDTO(List<CustomerCategoryPointsDTO> dtoList) {
    	int[]result=new int[dtoList.size()];
    	for(int i=0;i<dtoList.size();i++) {
    		result[i]=this.saveCustomerCategoryPointsDTO(dtoList.get(i));
    	}
    	return result;
    }
    @Override
    public int saveCustomerCategoryPointsDTO(CustomerCategoryPointsDTO dto) {
    	CustomerCategoryPoints condition=DTOUtils.newDTO(CustomerCategoryPoints.class);
		condition.setCategory3Id(dto.getCategory3Id());
		condition.setCertificateNumber(dto.getCertificateNumber());
		condition.setFirmCode(dto.getFirmCode());
		CustomerCategoryPoints item=this.getActualDao().selectOne(condition);
		if(item==null) {
			item=DTOUtils.clone(dto, CustomerCategoryPoints.class);
			item.setId(null);
			item.setAvailable(0);
			item.setBuyerPoints(0);
			item.setSellerPoints(0);
		}
		if(dto.isBuyer()) {
			item.setBuyerPoints(item.getBuyerPoints()+dto.getActualPoints());
		}else {
			item.setSellerPoints(item.getSellerPoints()+dto.getActualPoints());
		}
		item.setAvailable(item.getBuyerPoints()+item.getSellerPoints());
		
		if(item.getId()==null) {
			return this.insertExactSimple(item);
		}else {
			return this.updateExactSimple(item);
		}
    }
}