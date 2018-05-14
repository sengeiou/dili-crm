package com.dili.crm.rpc;

import com.dili.crm.domain.dto.CustomerPoints;
import com.dili.crm.domain.dto.CustomerPointsApiDTO;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;

import java.util.List;

/**
 * <B>Description</B>
 * <B>Copyright</B>
 * 本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.<br />
 * <B>农丰时代科技有限公司</B>
 *
 * @author wangmi
 * @createTime 2018/3/24 14:11
 */
@Restful("http://127.0.0.1:8086")
public interface CustomerPointsRpc {

    @POST("/customerPointsApi/listCustomerPoints")
    BaseOutput<List<CustomerPoints>> listCustomerPoints(@VOBody CustomerPointsApiDTO customer);
    @POST("/customerPointsApi/deleteCustomerPoints")
    BaseOutput<CustomerPoints> deleteCustomerPoints(@VOBody Long customerId);

    @POST("/customerPointsApi/updateCategoryPoints")
    BaseOutput<Void> updateCategoryPoints(@VOBody String paramJson);

}
