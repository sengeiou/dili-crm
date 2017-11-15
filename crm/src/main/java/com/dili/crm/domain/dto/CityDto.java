package com.dili.crm.domain.dto;

import javax.persistence.Table;

import com.dili.crm.domain.City;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2017-11-14 15:11:53.
 */
@Table(name = "`city`")
public interface CityDto extends City {
    
    String getState();

    void setState(String state);

}