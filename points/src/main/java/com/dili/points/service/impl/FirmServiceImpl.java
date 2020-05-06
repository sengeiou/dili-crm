package com.dili.points.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dili.points.service.FirmService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.UserDataAuth;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.domain.dto.FirmDto;
import com.dili.uap.sdk.glossary.DataAuthType;
import com.dili.uap.sdk.rpc.DataAuthRpc;
import com.dili.uap.sdk.rpc.FirmRpc;
import com.dili.uap.sdk.session.SessionContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/7/20 11:24
 */
@Service
public class FirmServiceImpl implements FirmService {

    @Autowired
    DataAuthRpc dataAuthRpc;
    @Autowired
    FirmRpc firmRpc;

    @Override
    public List<Firm> listByExample(FirmDto firm){
        BaseOutput<List<Firm>> output = firmRpc.listByExample(firm);
        return output.isSuccess() ? output.getData() : null;
    }

    @Override
    public List<String> getCurrentUserFirmCodes() {
        return getCurrentUserFirmCodes(null);
    }

    @Override
    public List<String> getCurrentUserFirmCodes(Long userId) {
        List<Firm> list = this.getCurrentUserFirms(userId);
        List<String> resultList = list.stream().map(Firm::getCode).collect(Collectors.toList());
        return resultList;
    }

    @Override
    public List<String> getCurrentUserAvaliableFirmCodes(String firmCode) {
        List<Firm> list = this.getCurrentUserFirms(null);
        List<String> resultList = list.stream().map(Firm::getCode).collect(Collectors.toList());
        if (StringUtils.isBlank(firmCode) || !resultList.contains(firmCode)) {
            return resultList;
        } else {
            return Arrays.asList(firmCode);
        }
    }

    @Override
    public String getCurrentUserDefaultFirmCode() {
        String defaultFirmCode = "hd";
        List<Firm> list = this.getCurrentUserFirms(null);
        List<String> resultList = list.stream().map(Firm::getCode).collect(Collectors.toList());
        if (resultList.contains(defaultFirmCode)) {
            return defaultFirmCode;
        } else if (resultList.size() > 0) {
            return resultList.get(0);
        } else {
            return "";
        }
    }

    @Override
    public Optional<Firm> getFirmByCode(String firmCode) {
        if (StringUtils.isBlank(firmCode)) {
            return Optional.empty();
        }
        BaseOutput<Firm> out = firmRpc.getByCode(firmCode);
        if (out.isSuccess()) {
            Firm firm = out.getData();
            return Optional.ofNullable(firm);
        }
        return Optional.empty();
    }

    @Override
    public List<Firm> getCurrentUserFirms() {
        return getCurrentUserFirms(null);
    }

    @Override
    public List<Firm> getCurrentUserFirms(Long userId) {
        UserDataAuth userDataAuth = DTOUtils.newDTO(UserDataAuth.class);
        if (null == userId) {
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            if (null == userTicket){
                return Collections.emptyList();
            }
            userDataAuth.setUserId(SessionContext.getSessionContext().getUserTicket().getId());
        } else {
            userDataAuth.setUserId(userId);
        }
        userDataAuth.setRefCode(DataAuthType.MARKET.getCode());
        BaseOutput<List<Map>> out = dataAuthRpc.listUserDataAuthDetail(userDataAuth);
        if (out.isSuccess()) {
            Stream<Firm> stream = out.getData().stream().flatMap(m -> m.values().stream())
                    .map(json -> {
                                JSONObject obj = (JSONObject) json;
                                Firm firm = DTOUtils.newDTO(Firm.class);
                                firm.setCode(String.valueOf(obj.get("code")));
                                firm.setName(String.valueOf(obj.get("name")));
                                return firm;
                            }
                    );
            return stream.collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }


}
