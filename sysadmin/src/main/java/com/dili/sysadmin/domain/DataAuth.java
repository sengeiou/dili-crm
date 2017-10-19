package com.dili.sysadmin.domain;

import com.dili.ss.domain.BaseDomain;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2017-07-10 17:58:29.
 */
@Table(name = "`data_auth`")
public class DataAuth extends BaseDomain {
	@Id
	@Column(name = "`id`")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 数据权限分类##这个由具体的业务系统确定
	 */
	@Column(name = "`type`")
	private String type;

	/**
	 * 数据权限ID##业务编号
	 */
	@Column(name = "`data_id`")
	private String dataId;

	/**
	 * 名称
	 */
	@Column(name = "`name`")
	private String name;

	/**
	 * 父级数据ID##外键，关联父级数据权限
	 */
	@Column(name = "`parent_data_id`")
	private Long parentDataId;

	/**
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获取数据权限分类##这个由具体的业务系统确定
	 *
	 * @return type - 数据权限分类##这个由具体的业务系统确定
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置数据权限分类##这个由具体的业务系统确定
	 *
	 * @param type
	 *            数据权限分类##这个由具体的业务系统确定
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取数据权限ID##业务编号
	 *
	 * @return data_id - 数据权限ID##业务编号
	 */
	public String getDataId() {
		return dataId;
	}

	/**
	 * 设置数据权限ID##业务编号
	 *
	 * @param dataId
	 *            数据权限ID##业务编号
	 */
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	/**
	 * 获取名称
	 *
	 * @return name - 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 *
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取父级数据ID##外键，关联父级数据权限
	 *
	 * @return parent_data_id - 父级数据ID##外键，关联父级数据权限
	 */
	public Long getParentDataId() {
		return parentDataId;
	}

	/**
	 * 设置父级数据ID##外键，关联父级数据权限
	 *
	 * @param parentDataId
	 *            父级数据ID##外键，关联父级数据权限
	 */
	public void setParentDataId(Long parentDataId) {
		this.parentDataId = parentDataId;
	}
}