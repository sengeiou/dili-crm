package com.dili.sysadmin.domain.dto;

import java.io.Serializable;

import com.dili.ss.constant.ResultCode;
import com.dili.sysadmin.domain.User;

public class UserLoginResultDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3870661253081877293L;

	private User userInfo;
	private String sessionId;
	private String status;
	private String message;

	public static UserLoginResultDto success(String message, User userInfo, String sessionId) {
		UserLoginResultDto result = new UserLoginResultDto();
		result.setMessage(message);
		result.setStatus(ResultCode.OK);
		result.setSessionId(sessionId);
		result.setUserInfo(userInfo);
		return result;
	}

	public static UserLoginResultDto failure(String message) {
		UserLoginResultDto result = new UserLoginResultDto();
		result.setMessage(message);
		result.setStatus(ResultCode.DATA_ERROR);
		return result;
	}

	public User getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(User userInfo) {
		this.userInfo = userInfo;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
