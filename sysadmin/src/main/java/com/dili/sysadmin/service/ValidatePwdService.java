package com.dili.sysadmin.service;

/**
 * Created by Administrator on 2016/10/14.
 */
public interface ValidatePwdService {

    /**
     * 验证密码
     * @param pwd
     */
    public void validatePwd(String pwd) throws IllegalArgumentException;

    public String[] definition();
}
