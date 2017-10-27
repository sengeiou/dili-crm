package com.dili.alm.component;

import com.dili.alm.domain.dto.DataDictionaryDto;
import com.dili.alm.rpc.DataDictionaryRPC;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 初始化数据组件
 * Created by asiamaster on 2017/10/27 0027.
 */
@Component
public class InitComponent implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private DataDictionaryRPC dataDictionaryRPC;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
			//获取数据权限类型
			BaseOutput<DataDictionaryDto> dataDictionaryDtoBaseOutput = dataDictionaryRPC.findDataDictionaryByCode("data_auth_type");

		}
	}
}
