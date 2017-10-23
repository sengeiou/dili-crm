package com.dili.alm.provider;

import com.dili.alm.domain.Milestones;
import com.dili.alm.service.MilestonesService;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by asiamaster on 2017/10/18 0018.
 */
@Component
public class MilestonesProvider implements ValueProvider {

	@Autowired
	private MilestonesService milestonesService;


	@Override
	public List<ValuePair<?>> getLookupList(Object o, Map map, FieldMeta fieldMeta) {
		return null;
	}

	@Override
	public String getDisplayText(Object o, Map map, FieldMeta fieldMeta) {
		Milestones milestones = (Milestones)map.get("_rowData");
		if(milestones.getParentId() == null) return null;
		return milestonesService.get(milestones.getParentId()).getCode();
	}
}
