package com.dili.sysadmin.domain;

import com.dili.ss.domain.BaseDomain;
import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2017-07-10 17:58:29.
 */
@Table(name = "`resource`")
public class Resource extends BaseDomain {
    /**
     * 主键
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 名称
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 描述
     */
    @Column(name = "`description`")
    private String description;

    /**
     * 外键，关联menu表
     */
    @Column(name = "`menu_id`")
    private Long menuId;

    /**
     * 编码##原resource URL
     */
    @Column(name = "`code`")
    private String code;

    /**
     * 状态##{data:[{value:1, text:"启用"},{value:0, text:"停用"}]}
     */
    @Column(name = "`status`")
    private Integer status;

    /**
     * 创建时间
     */
    @Column(name = "`created`")
    private Date created;

    /**
     * 修改时间
     */
    @Column(name = "`modified`")
    private Date modified;

    /**
     * 有效性##数据有效性，用于逻辑删除##{data:[{value:0, text:"无效"},{value:1, text:"有效"}]}
     */
    @Column(name = "`yn`")
    private Integer yn;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取名称
     *
     * @return resource_name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取描述
     *
     * @return description - 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取外键，关联menu表
     *
     * @return menu_id - 外键，关联menu表
     */
    public Long getMenuId() {
        return menuId;
    }

    /**
     * 设置外键，关联menu表
     *
     * @param menuId 外键，关联menu表
     */
    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    /**
     * 获取编码##原resource URL
     *
     * @return code - 编码##原resource URL
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置编码##原resource URL
     *
     * @param code 编码##原resource URL
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取状态##{data:[{value:1, text:"启用"},{value:0, text:"停用"}]}
     *
     * @return status - 状态##{data:[{value:1, text:"启用"},{value:0, text:"停用"}]}
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态##{data:[{value:1, text:"启用"},{value:0, text:"停用"}]}
     *
     * @param status 状态##{data:[{value:1, text:"启用"},{value:0, text:"停用"}]}
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取创建时间
     *
     * @return created - 创建时间
     */
    public Date getCreated() {
        return created;
    }

    /**
     * 设置创建时间
     *
     * @param created 创建时间
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * 获取修改时间
     *
     * @return modified - 修改时间
     */
    public Date getModified() {
        return modified;
    }

    /**
     * 设置修改时间
     *
     * @param modified 修改时间
     */
    public void setModified(Date modified) {
        this.modified = modified;
    }

    /**
     * 获取有效性##数据有效性，用于逻辑删除##{data:[{value:0, text:"无效"},{value:1, text:"有效"}]}
     *
     * @return yn - 有效性##数据有效性，用于逻辑删除##{data:[{value:0, text:"无效"},{value:1, text:"有效"}]}
     */
    public Integer getYn() {
        return yn;
    }

    /**
     * 设置有效性##数据有效性，用于逻辑删除##{data:[{value:0, text:"无效"},{value:1, text:"有效"}]}
     *
     * @param yn 有效性##数据有效性，用于逻辑删除##{data:[{value:0, text:"无效"},{value:1, text:"有效"}]}
     */
    public void setYn(Integer yn) {
        this.yn = yn;
    }
}