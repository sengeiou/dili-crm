package com.dili.crm.service;

import com.dili.crm.domain.Customer;
import com.dili.crm.domain.dto.CustomerAddressDto;
import com.dili.crm.domain.dto.CustomerChartDTO;
import com.dili.crm.domain.dto.MembersDto;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import org.springframework.ui.ModelMap;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-13 17:11:22.
 */
public interface CustomerService extends BaseService<Customer, Long> {

	/**
	 * 按需插入客户，返回BaseOutput
	 * @param customer
	 * @return
	 */
	BaseOutput insertSelectiveWithOutput(Customer customer);

	/**
	 * 按需修改客户，返回BaseOutput
	 * @param customer
	 * @return
	 */
	BaseOutput updateSelectiveWithOutput(Customer customer);

	/**
	 * 修改客户，返回BaseOutput
	 * @param customer
	 * @return
	 */
	BaseOutput updateWithOutput(Customer customer);

	/**
	 * 逻辑删除客户，返回BaseOutput
	 * @param aLong
	 * @return
	 */
	BaseOutput deleteWithOutput(Long aLong) throws Exception;
	
	/**基于用户类型分组查询统计数据
	 * @return 用户数量,用户类型数据列表
	 */
	BaseOutput<List<CustomerChartDTO>> selectCustomersGroupByType();
	
	/**基于用户所属市场分组查询统计数据
	 * @return 用户数量,市场数据列表
	 */
	BaseOutput<List<CustomerChartDTO>> selectCustomersGroupByMarket();

	/**基于用户行业分组查询统计数据
	 * @return 用户数量,用户行业 数据列表
	 */
	BaseOutput<List<CustomerChartDTO>> selectCustomersGroupByProfession();

	/**
	 * 处理客户详情
	 * @param modelMap
	 * @param id
	 */
	void handleDetail(ModelMap modelMap, Long id) throws Exception;

	/**
	 * 根据客户名称(模糊)和客户id查询可以添加的成员客户，
	 * 过滤掉自己、所有的子id和所有父id
	 * @param membersDto
	 * @return
	 */
	String listMembersPage(MembersDto membersDto) throws Exception;

	/**
	 * 删除成员客户关系
	 * @param id
	 * @return
	 */
	BaseOutput deleteMembers(Long id);

	/**
	 *  根据客户名称(模糊)和客户ID查询可用添加的父客户
	 *  过滤掉自己、所有的子id
	 * @param membersDto
	 * @return
	 * @throws Exception
	 */
	String listParentCustomerPage(MembersDto membersDto) throws Exception;

	/**
	 * 根据客户父id展开客户
	 * @param parentId
	 * @return
	 */
	String expandEasyuiPageByParentId(Long parentId) throws Exception;

	/**
	 * 获取客户坐标
	 * @return
	 */
	List<CustomerAddressDto> getCustomerAddress();

	/**
	 * 根据客户类型获取客户的经营地区相关信息
	 * @param type 客户类型：采购、销售、代买等
	 * @return
	 */
	List<Customer> listCustomerOperating(String type);
}