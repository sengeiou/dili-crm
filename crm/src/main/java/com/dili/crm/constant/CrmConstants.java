package com.dili.crm.constant;

/**
 * CRM系统常量
 * Created by asiamaster on 2017/12/8 0008.
 */
public class CrmConstants {

    /**
     * 需要扫描多长时间以前的数据(单位秒)
     * 默认24小时以前的数据
     */
    public static Integer scanTime = 60 * 60 * 24;
    /**
     * 查询客户时，market为ALL_MARKET时，查询所有市场(不限制数据权限)
     */
    public static String ALL_MARKET = "all";

}
