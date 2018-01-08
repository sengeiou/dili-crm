package com.dili.crm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dili.crm.dao.AddressMapper;
import com.dili.crm.dao.CityMapper;
import com.dili.crm.domain.Address;
import com.dili.crm.domain.City;
import com.dili.crm.rpc.MapRpc;
import com.dili.crm.service.AddressService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-15 11:16:14.
 */
@Service
public class AddressServiceImpl extends BaseServiceImpl<Address, Long> implements AddressService {

    public AddressMapper getActualDao() {
        return (AddressMapper)getDao();
    }

    @Resource
    private MapRpc mapRpc;

    @Value("${map.ak}")
    private String ak;

    @Resource
    private CityMapper cityMapper;

    /**
     * 新增地址信息
     *
     * @param address
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput insertSelectiveWithOutput(Address address) throws Exception{
        City city = this.getCityByLatAndLng(address.getLat(),address.getLng());
        if (null == city){
            return BaseOutput.failure("地理信息在系统中不存在，请先维护");
        }
        address.setCityId(String.valueOf(city.getParentId()));
        setNotDefault(address);
        this.insertSelective(address);
        return BaseOutput.success("修改成功");
    }

    /**
     * 修改地址信息
     *
     * @param address
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput updateSelectiveWithOutput(Address address) throws Exception {
        City city = this.getCityByLatAndLng(address.getLat(),address.getLng());
        if (null == city){
            return BaseOutput.failure("地理信息在系统中不存在，请先维护");
        }
        address.setCityId(String.valueOf(city.getParentId()));
        setNotDefault(address);
        this.updateSelective(address);
        return BaseOutput.success("修改成功");
    }

    /**
     * 获取城市信息
     * @param lat 纬度
     * @param lng 经度
     * @return  城市信息
     * @throws IOException
     */
    private City getCityByLatAndLng(String lat,String lng) throws IOException{
        Map params = Maps.newHashMap();
        params.put("ak", ak);
        params.put("location", lat+","+lng);
        params.put("output", "json");
        String geocoder = mapRpc.geocoder(params);
        JSONObject object = JSONObject.parseObject(geocoder);
        if (0==object.getIntValue("status")){
            JSONObject addressJson = object.getJSONObject("result").getJSONObject("addressComponent");
            Integer adcode = addressJson.getInteger("adcode");
            City city = cityMapper.selectByPrimaryKey(adcode.longValue());
            return city;
        }
        return null;
    }

    /**
     * 设置用户下的所有地址为非默认
     * @param address 地址信息
     */
    private void setNotDefault(Address address){
        if (1==address.getIsDefault()){
            Address setData = DTOUtils.newDTO(Address.class);
            setData.setIsDefault(0);
            Address conditionData = DTOUtils.newDTO(Address.class);
            conditionData.setCustomerId(address.getCustomerId());
            updateSelectiveByExample(setData,conditionData);
        }
    }
}