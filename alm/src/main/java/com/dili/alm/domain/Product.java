package com.dili.alm.domain;

import com.dili.ss.domain.BaseDomain;
import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2017-06-28 15:51:42.
 */
@Table(name = "`product`")
public class Product extends BaseDomain {
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
     * 价格(单位分)
     */
    @Column(name = "`price`")
    private Long price;

    /**
     * 产品类型##{data:[{value:10, text:"蔬菜"},{value:20, text:"水果"},{value:30, text:"日杂"}]}
     */
    @Column(name = "`type`")
    private Integer type;

    /**
     * 产品状态##产品状态描述##{table:"data_dictionary_value",valueField:"value", textField:"code", orderByClause:"sort asc", queryParams:{dd_id:1}}
     */
    @Column(name = "`state`")
    private Integer state;

    /**
     * 创建日期
     */
    @Column(name = "`created`")
    private Date created;

    /**
     * 修改时间
     */
    @Column(name = "`modified`")
    private Date modified;

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
     * @return name - 名称
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
     * 获取价格(单位分)
     *
     * @return price - 价格(单位分)
     */
    public Long getPrice() {
        return price;
    }

    /**
     * 设置价格(单位分)
     *
     * @param price 价格(单位分)
     */
    public void setPrice(Long price) {
        this.price = price;
    }

    /**
     * 获取产品类型##{data:[{value:10, text:"蔬菜"},{value:20, text:"水果"},{value:30, text:"日杂"}]}
     *
     * @return type - 产品类型##{data:[{value:10, text:"蔬菜"},{value:20, text:"水果"},{value:30, text:"日杂"}]}
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置产品类型##{data:[{value:10, text:"蔬菜"},{value:20, text:"水果"},{value:30, text:"日杂"}]}
     *
     * @param type 产品类型##{data:[{value:10, text:"蔬菜"},{value:20, text:"水果"},{value:30, text:"日杂"}]}
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取产品状态##产品状态描述##{table:"data_dictionary_value",valueField:"value", textField:"code", orderByClause:"sort asc", queryParams:{dd_id:1}}
     *
     * @return state - 产品状态##产品状态描述##{table:"data_dictionary_value",valueField:"value", textField:"code", orderByClause:"sort asc", queryParams:{dd_id:1}}
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置产品状态##产品状态描述##{table:"data_dictionary_value",valueField:"value", textField:"code", orderByClause:"sort asc", queryParams:{dd_id:1}}
     *
     * @param state 产品状态##产品状态描述##{table:"data_dictionary_value",valueField:"value", textField:"code", orderByClause:"sort asc", queryParams:{dd_id:1}}
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取创建日期
     *
     * @return created - 创建日期
     */
    public Date getCreated() {
        return created;
    }

    /**
     * 设置创建日期
     *
     * @param created 创建日期
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
}