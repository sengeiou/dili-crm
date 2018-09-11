package com.dili.dp.service;

import java.util.List;

import com.dili.dp.domain.CustomerCategoryPoints;
import com.dili.dp.domain.dto.CustomerCategoryPointsDTO;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.EasyuiPageOutput;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-09 17:35:45.
 */
public interface CustomerCategoryPointsService extends BaseService<CustomerCategoryPoints, Long> {

    int updateCustomerName(String name,Long id);
    
    public EasyuiPageOutput listEasyuiPageByExample(CustomerCategoryPointsDTO customerCategoryPoints, boolean useProvider,List<String>firmCodes) throws Exception;
    
    /**
     * 批量保存CustomerCategoryPointsDTO
     * @param categoryPointsDTO
     * @return
     */
	public int[] batchSaveCustomerCategoryPointsDTO(List<CustomerCategoryPointsDTO> dtoList);
	
	/**
	 * 保存CustomerCategoryPointsDTO
	 * @param categoryPointsDTO
	 * @return
	 */
	public int saveCustomerCategoryPointsDTO(CustomerCategoryPointsDTO categoryPointsDTO);
}