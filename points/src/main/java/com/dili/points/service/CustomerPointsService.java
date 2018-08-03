package com.dili.points.service;

import java.util.List;

import com.dili.points.domain.CustomerPoints;
import com.dili.points.domain.dto.CustomerApiDTO;
import com.dili.points.domain.dto.CustomerPointsDTO;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.EasyuiPageOutput;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-03-20 11:29:30.
 */
public interface CustomerPointsService extends BaseService<CustomerPoints, Long> {
	public EasyuiPageOutput listCustomerPointsByCustomer(CustomerApiDTO customer);

	public CustomerPointsDTO findCustomerPointsByCertificateNumber(String certificateNumber);
	
	
    /**
     * 批量保存CustomerPointsDTO
     * @param dtoList
     * @return
     */
	public int[] batchSaveCustomerPointsDTO(List<CustomerPointsDTO> dtoList);
	
	/**
	 * 保存CustomerPointsDTO
	 * @param customerPointsDTO
	 * @return
	 */
	public int saveCustomerPointsDTO(CustomerPointsDTO customerPointsDTO);
}