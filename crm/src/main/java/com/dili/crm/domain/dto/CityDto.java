package com.dili.crm.domain.dto;

import com.dili.crm.domain.City;
import com.dili.ss.domain.annotation.Operator;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2017-11-14 15:11:53.
 */
@Table(name = "`city`")
public interface CityDto extends City {
    
    String getState();

    void setState(String state);

    @Column(name = "`level_type`")
    @Operator(Operator.IN)
    List<Integer> getLevelTypes();

    void setLevelTypes(List<Integer> levelTypes);

}