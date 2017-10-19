package com.dili.sysadmin.utils;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 
 * <B>Copyright</B> Copyright (c) 2014 www.diligrp.com All rights reserved.
 * <br />
 * 本软件源代码版权归地利集团,未经许可不得任意复制与传播.<br />
 * <B>Company</B> 地利集团
 * 
 * @createTime 2014年5月28日 下午3:36:24
 * @author Wang22
 */
@Component
public class SecurityUtil {

	/**
	 * 图片验证码校验地址
	 */
	private String verifyImgCodeURL;

	/**
	 * 
	 * 检查验证码是否正确
	 * 
	 * @param uuid
	 * @param token
	 * @return true == 验证成功 ； false == 验证失败
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @createTime 2014年5月28日 下午4:49:05
	 * @author Wang22
	 */
	public boolean verifyImgCode(String uuid, String token) {

		// String url = verifyImgCodeURL + uuid;
		// Boolean flag = false;
		// try {
		// // 创建post请求
		// HttpPost post = new HttpPost(url);
		// List<BasicNameValuePair> postData = new
		// ArrayList<BasicNameValuePair>(
		// 1);
		// // 设置token参数
		// postData.add(new BasicNameValuePair(uuid, token));
		// post.setEntity(new UrlEncodedFormEntity(postData, HTTP.UTF_8));
		// // 发送验证请求
		// HttpResponse response = HttpClientManager.getHttpClient().execute(
		// post);
		// if (response.getStatusLine().getStatusCode() != 200) {
		// throw new IllegalStateException(response.getStatusLine()
		// .toString());
		// }
		// // 返回结果
		// HttpEntity entity = response.getEntity();
		// String result = EntityUtils.toString(entity);
		// JSONObject obj = JSON.parseObject(result);
		// flag = obj.getBoolean("isValid");
		// } catch (Exception e) {
		// // 异常，直接返回验证失败
		// }
		// return flag;
		return true;
	}

	/**
	 * 
	 * 过滤邮箱地址 abcdefg@163.com --> ab*****@163.com
	 * 
	 * @param email
	 * @return
	 * @createTime 2014年5月29日 下午3:45:59
	 * @author Wang22
	 */
	public String filterEmail(String mail) {
		if (StringUtils.isEmpty(mail)) {
			return StringUtils.EMPTY;
		}
		// 去空格
		mail = mail.trim();
		String mailRegex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		if (!mail.matches(mailRegex)) {
			// 不符合邮件格式
			return StringUtils.EMPTY;
		}
		int index = mail.indexOf("@");
		String preStr = mail.substring(0, index);
		String website = mail.substring(index);
		String result = StringUtils.EMPTY;
		if (preStr.length() > 4) {
			int position = preStr.length() - 4;
			result = preStr.substring(0, position) + "****" + website;
		} else {
			int position = preStr.length() == 1 ? 1 : preStr.length() - 1;
			StringBuffer x = new StringBuffer(4);
			for (int i = 0; i < position; i++) {
				x.append("*");
			}
			result = preStr.substring(position) + x + website;
		}
		return result;
	}

	/**
	 * 
	 * 过滤手机号码 13899999999 --> 138****9999
	 * 
	 * @param phone
	 * @return
	 * @createTime 2014年5月29日 下午3:47:02
	 * @author Wang22
	 */
	public String filterMobilePhone(String phone) {
		if (StringUtils.isEmpty(phone)) {
			return StringUtils.EMPTY;
		}
		// 去空格
		phone = phone.trim();
		if (!phone.matches("\\d{11}")) {
			// 不符合手机格式
			return StringUtils.EMPTY;
		}
		String preStr = phone.substring(0, 3);
		String sufStr = phone.substring(7);
		return preStr + "****" + sufStr;
	}

	/**
	 * 
	 * 过滤身份证 将后四位替换成 ****
	 * 
	 * @param idcard
	 * @return
	 * @createTime 2014年7月9日 下午3:32:48
	 * @author Wang22
	 */
	public String filterIdCard(String idcard) {
		if (StringUtils.isEmpty(idcard)) {
			return StringUtils.EMPTY;
		}
		String card = idcard.trim();
		if (card.length() < 4) {
			return StringUtils.EMPTY;
		}
		return card.substring(0, card.length() - 4) + "****";
	}

	/**
	 * set value of SecurityUtils.verifyImgCodeURL
	 * 
	 * @param verifyImgCodeURL
	 *            the verifyImgCodeURL to set
	 * @createTime 2014年5月28日 下午5:02:01
	 * @author Wang22
	 */
	public void setVerifyImgCodeURL(String verifyImgCodeURL) {
		this.verifyImgCodeURL = verifyImgCodeURL;
	}

}
