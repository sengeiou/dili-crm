package com.dili.crm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dili.crm.dao.AddressMapper;
import com.dili.crm.dao.CityMapper;
import com.dili.crm.domain.Address;
import com.dili.crm.domain.City;
import com.dili.crm.rpc.MapRpc;
import com.dili.crm.service.AddressService;
import com.dili.crm.service.CityService;
import com.dili.crm.utils.PinYinUtil;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;
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
    
    @Resource
    private CityMapper cityMapper;

    @Resource
    private CityService cityService;

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
     * 获取地理编码
     *
     * @param address
     * @return
     */
    @Override
    public BaseOutput locationAddress(String address) throws Exception {
    	Map<String,String> params = Maps.newHashMap();
        params.put("address",address);
        params.put("output", "json");
        String geocoder = mapRpc.geocoder(params);
        JSONObject object = JSONObject.parseObject(geocoder);
        if (0==object.getIntValue("status")){
            JSONObject location = object.getJSONObject("result").getJSONObject("location");
            return BaseOutput.success().setData(location);
            
        }
        return BaseOutput.failure("访问出错");
    }
    /**
     * 获取逆地理编码
     *
     * @param lat 纬度
     * @param lng 经度
     * @return
     */
    @Override
    public BaseOutput locationReverse(String lat,String lng) throws Exception {
    	City city=getCityByLatAndLng(lat,lng);
    	if(city!=null) {
        	return BaseOutput.success().setData(city);
        }else {
        	return BaseOutput.failure("地理信息在系统中不存在，请先维护");
        }
    	
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
        params.put("location", lat+","+lng);
        params.put("output", "json");
        params.put("latest_admin","1");
        String geocoder = mapRpc.geocoder(params);
        JSONObject object = JSONObject.parseObject(geocoder);
        if (0==object.getIntValue("status")){
            JSONObject addressJson = object.getJSONObject("result").getJSONObject("addressComponent");
            Integer adcode = addressJson.getInteger("adcode");
            City city = cityMapper.selectByPrimaryKey(adcode.longValue());
            //出现根据code找不到对应的城市数据时，再通过城市文本，进行检索
            if(null == city){
                String province = addressJson.getString("province");
                String cityName = addressJson.getString("city");
                String district = addressJson.getString("district");
                city = queryByMergerNameAndInsert(province, cityName, district,true,adcode);
            }
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

    /**
     * 根据合并名称查询城市信息,根据条件，是否新增，返回最终结果的城市信息
     * 当市都不存在的情况下，不会新增数据
     * @param province 省
     * @param city  市
     * @param district 行政区
     * @param insert 如果根据省市区还是检索不出数据，是否自动新增城市信息 true-新增;false-不新增
     * @param adcode 做新增操作时的行政code
     * @return 城市信息
     */
    private City queryByMergerNameAndInsert(String province,String city,String district,Boolean insert,Integer adcode){
        City data = queryByMergerName(province,city,district);
        //如果检索的城市为空，并需要做城市新增操作
        if (null == data && insert && StringUtils.isNotBlank(district)){
            City temp = queryByMergerName(province,city,null);
            //根据省市检索不为空
            if (null != temp){
                data = DTOUtils.newDTO(City.class);
                data.setId(adcode.longValue());
                data.setCreated(new Date());
                data.setModified(data.getCreated());
                data.setName(district);
                data.setParentId(temp.getId());
                //规则：行政区字符长度大于2的时候，简称 拼音  都去掉最后一个字，最后一个字 大部分是"市"、"县" 之类的
                String d_temp = district;
                if (district.length() > 2) {
                     d_temp = district.substring(0,2);
                }
                data.setShortName(d_temp);
                data.setLevelType(3);
                data.setCityCode(temp.getCityCode());
                data.setMergerName(province+","+city+","+district);
                data.setLng(temp.getLng());
                data.setLat(temp.getLat());
                data.setPinyin(PinYinUtil.getFullPinYin(d_temp,"",true));
                data.setShortPy(PinYinUtil.getSimplePinYin(d_temp).toUpperCase());
                data.setYn(true);
                cityService.insert(data);
            }
        }
        return data;

    }
    /**
     * 根据合并名称查询城市信息
     * @param province 省
     * @param city  市
     * @param district 行政区
     * @return 城市信息
     */
    private City queryByMergerName(String province,String city,String district){
        StringBuilder strb = new StringBuilder(exchangeProvince(province)).append(",").append(city);
        if (StringUtils.isNotBlank(district)){
            strb.append(",").append(district);
        }
        City condition = DTOUtils.newDTO(City.class);
        condition.setMergerName(strb.toString());
        List<City> cities = cityMapper.select(condition);
        if (CollectionUtils.isNotEmpty(cities)){
            return cities.get(0);
        }
        return null;

    }


    /**
     * 百度地图返回的直辖市中，直辖市名称带有"市"字，本地库中没有，所以需要转换
     * @param province
     * @return
     */
    private String exchangeProvince(String province){
        if (StringUtils.isBlank(province)){
            return null;
        }
        switch (province){
            case "北京市":
                return "北京";
            case "天津市":
                return "天津";
            case "重庆市":
                return "重庆";
            case "上海市":
                return "上海";
            default:
        }
        return province;
    }
}