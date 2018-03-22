package com.dili.points.rpc;

import com.dili.points.domain.Customer;
import com.dili.points.domain.dto.CustomerApiDTO;
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
 * @author yuehongbo
 * @createTime 2018/3/21 14:29
 */
@Restful("${syscrm.contextPath}")
public interface CustomerRpc {

    @POST("/customerApi/list")
    BaseOutput<List<Customer>> list(@VOBody CustomerApiDTO customer);
}
