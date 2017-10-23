package com.dili.alm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.alm.dao.DataDictionaryMapper;
import com.dili.alm.dao.DataDictionaryValueMapper;
import com.dili.alm.domain.DataDictionary;
import com.dili.alm.domain.DataDictionaryValue;
import com.dili.alm.domain.dto.DataDictionaryValueTreeView;
import com.dili.alm.service.DataDictionaryValueService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;

/**
 * ��MyBatis Generator�����Զ����� This file was generated on 2017-07-12
 * 10:41:19.
 */
@Service
public class DataDictionaryValueServiceImpl extends BaseServiceImpl<DataDictionaryValue, Long>
		implements DataDictionaryValueService {

	@Autowired
	private DataDictionaryMapper dataDictionaryMapper;

	public DataDictionaryValueMapper getActualDao() {
		return (DataDictionaryValueMapper) getDao();
	}

	@Override
	public BaseOutput<Object> insertAndGet(DataDictionaryValue dataDictionaryValue) {
		int result = this.getActualDao().insert(dataDictionaryValue);
		if (result <= 0) {
			return BaseOutput.failure();
		}
		dataDictionaryValue = this.getActualDao().selectByPrimaryKey(dataDictionaryValue.getId());
		return BaseOutput.success().setData(dataDictionaryValue);
	}

	@Override
	public List<DataDictionaryValueTreeView> listTree(Long ddId) {
		DataDictionary dd = this.dataDictionaryMapper.selectByPrimaryKey(ddId);
		if (dd == null) {
			return null;
		}
		DataDictionaryValueTreeView root = new DataDictionaryValueTreeView();
		root.setId(0L);
		root.addAttribute("ddId", dd.getId());
		root.setName(dd.getName());
		List<DataDictionaryValueTreeView> target = new ArrayList<>();
		target.add(root);
		DataDictionaryValue record = DTOUtils.newDTO(DataDictionaryValue.class);
		record.setDdId(ddId);
		List<DataDictionaryValue> list = this.getActualDao().select(record);
		if (CollectionUtils.isEmpty(list)) {
			return target;
		}
		for (DataDictionaryValue ddValue : list) {
			DataDictionaryValueTreeView vo = new DataDictionaryValueTreeView();
			vo.setId(ddValue.getId());
			vo.addAttribute("ddId", ddValue.getDdId());
			vo.setName(ddValue.getCode());
			vo.setParentId(ddValue.getParentId());
			target.add(vo);
		}
		return target;
	}
}