package com.dili.points.rpc;

import com.dili.points.domain.DataDictionaryValue;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOSingleParameter;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Created by asiam on 2018/3/20 0020.
 */
@Restful("${crm.contextPath}")
public interface DataDictionaryValueRpc {

    @POST("/dataDictionaryApi/listByDdId")
    BaseOutput<List<DataDictionaryValue>> listByDdId(@VOSingleParameter Long ddId);
}
