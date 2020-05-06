package com.dili.points.service;

import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.dto.FirmDto;

import java.util.List;
import java.util.Optional;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/7/20 11:23
 */
public interface FirmService {

    /**
     * 当前用户拥有访问权限的firmcode
     * @return
     */
    List<String> getCurrentUserFirmCodes();

    /**
     * 当前用户拥有访问权限的firmcode
     * @param userId 用户ID
     * @return
     */
    List<String> getCurrentUserFirmCodes(Long userId);

    /**
     * 当前用户拥有访问权限的firmcode
     * @param firmCode
     * @return
     */
    List<String> getCurrentUserAvaliableFirmCodes(String firmCode);

    /**
     * 当前用户拥有访问权限的firmcode
     * @return
     */
    String getCurrentUserDefaultFirmCode();

    /**
     * 根据条件查询市场
     * @param firm
     * @return
     */
    List<Firm> listByExample(FirmDto firm);

    /**
     * 通过code查询firm
     * @param firmCode
     * @return
     */
    Optional<Firm> getFirmByCode(String firmCode);

    /**
     * 获得当前用户拥有的所有Firm
     * @return
     */
    List<Firm> getCurrentUserFirms();

    /**
     * 获得当前用户拥有的所有Firm
     * @param userId 用户ID，如果为空，则从session中获取，如果未获取到，返回空
     * @return
     */
    List<Firm> getCurrentUserFirms(Long userId);
}
