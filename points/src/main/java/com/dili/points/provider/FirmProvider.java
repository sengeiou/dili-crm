package com.dili.points.provider;

import com.dili.points.domain.Customer;
import com.dili.points.domain.dto.CustomerApiDTO;
import com.dili.points.rpc.CustomerRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 市场提供者
 * Created by guofeng on 2018/07/12.
 */
@Component
public class FirmProvider implements ValueProvider {

	 private static final List<ValuePair<?>> BUFFER;

	    static {
	        BUFFER = new ArrayList<ValuePair<?>>();
	        BUFFER.add(new ValuePairImpl("硬编码-市场A", "1"));
	        BUFFER.add(new ValuePairImpl("硬编码-市场B", "2"));
	    }

	    @Override
	    public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
	        return BUFFER;
	    }

	    @Override
	    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
	        if(obj == null || "".equals(obj)){
	            return null;
	        }
	        for(ValuePair<?> valuePair : BUFFER){
	            if(obj.toString().equals(valuePair.getValue())){
	                return valuePair.getText();
	            }
	        }
	        return null;
	    }
}
