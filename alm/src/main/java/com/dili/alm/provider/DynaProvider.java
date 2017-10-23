package com.dili.alm.provider;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by asiamaster on 2017/8/22 0022.
 */
@Component
public class DynaProvider implements ValueProvider {
	@Override
	public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
		List<ValuePair<?>> buffer= new ArrayList<ValuePair<?>>();
		if(val!= null) {
			buffer.add(new ValuePairImpl(val.toString(), val));
		}
		buffer.add(new ValuePairImpl("test", "test"));
		return buffer;
	}

	@Override
	public String getDisplayText(Object val, Map metaMap, FieldMeta fieldMeta) {
		return null;
	}
}
