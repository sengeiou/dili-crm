package com.dili.sysadmin.service.impl;

import com.dili.sysadmin.service.ValidatePwdService;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/10/14.
 */
public class Pwd26 implements ValidatePwdService {

    /**
     * 长度范围
     */
    private Pattern pwdLength = Pattern.compile(".{6,20}");

    /**
     * 特殊字符
     */
    private String chars = "`~!@#$%^&*()<>_+-=[]{};':\"\\|?,./";

    @Override
    public String[] definition() {
        return new String[]{"长度6-20位", "大写字母,小写字母,数字,特殊字符任意2种或以上组合"};
    }

    /**
     * 验证密码
     *
     * @param pwd
     */
    public void validatePwd(String pwd) throws IllegalArgumentException {
        if (!pwdLength.matcher(pwd).find()) {
            throw new IllegalArgumentException("密码长度不够!");
        }
        Set<Integer> dest = new HashSet<>();
        for (Character c : pwd.toCharArray()) {
            if (chars.indexOf(c) >= 0) {
                dest.add(1);
            }
            if (c >= '0' && c <= '9') {
                dest.add(2);
            }
            if (c >= 'A' && c <= 'Z') {
                dest.add(3);
            }
            if (c >= 'a' && c <= 'z') {
                dest.add(4);
            }
        }
        if (dest.size() >= 2) {
            return;
        }
        throw new IllegalArgumentException("密码不符合规则!");
    }

}
