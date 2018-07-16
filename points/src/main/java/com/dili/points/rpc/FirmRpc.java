package com.dili.points.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.ss.retrofitful.annotation.VOSingleParameter;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.domain.Firm;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

/**
 * <B>Description</B>
 * <B>Copyright</B>
 * 本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.<br />
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/3/21 14:29
 */
@Restful("${uap.contextPath}")
public interface FirmRpc {


	    @POST("/firmApi/listByExample.api")
	    public BaseOutput<List<Firm>> listByExample(@VOBody Firm firm) ;
	    
	    @POST("/firmApi/getByCode.api")
	    public BaseOutput<Firm> getByCode(@VOSingleParameter String code);
	}
