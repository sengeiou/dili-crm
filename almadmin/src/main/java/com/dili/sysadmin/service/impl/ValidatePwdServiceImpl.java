package com.dili.sysadmin.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dili.sysadmin.service.ValidatePwdService;

/**
 * Created by Administrator on 2016/10/14.
 */
@Service
public class ValidatePwdServiceImpl implements ValidatePwdService {
	@Value("${sysadmin.pwd.level:pwd26}")
	private String level;

	private Map<String, ValidatePwdService> map;

	public ValidatePwdServiceImpl() {
		map = new HashMap<>();
		map.put("pwd26", new Pwd26());
		map.put("pwd38", new Pwd38());
	}

	@Override
	public void validatePwd(String pwd) throws IllegalArgumentException {
		map.get(level).validatePwd(pwd);
	}

	@Override
	public String[] definition() {
		return map.get(level).definition();
	}
}
