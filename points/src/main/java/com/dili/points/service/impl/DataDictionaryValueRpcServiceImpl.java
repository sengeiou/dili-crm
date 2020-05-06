package com.dili.points.service.impl;

import com.dili.points.service.DataDictionaryValueRpcService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by asiam on 2018/7/10 0010.
 */
@Component
public class DataDictionaryValueRpcServiceImpl implements DataDictionaryValueRpcService {

    @Autowired
    private DataDictionaryRpc dataDictionaryRpc;

    @Override
    public List<DataDictionaryValue> listByDdCode(String ddCode) {
        DataDictionaryValue dataDictionaryValue = DTOUtils.newDTO(DataDictionaryValue.class);
        dataDictionaryValue.setDdCode(ddCode);
        BaseOutput<List<DataDictionaryValue>> output = dataDictionaryRpc.listDataDictionaryValue(dataDictionaryValue);
        if(output.isSuccess()){
            return output.getData();
        }
        return null;
    }
}