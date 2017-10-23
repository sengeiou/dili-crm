package com.dili.alm.component.beetl;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.MetadataUtils;
import com.dili.ss.metadata.ObjectMeta;
import com.dili.ss.util.POJOUtils;
import org.beetl.core.Tag;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 查询对话框标签
 * Created by asiamaster on 2017/7/20 0020.
 */
@Component("query")
public class QueryTag extends Tag {

	private final String LINE_SEPARATOR = System.getProperty("line.separator");
	private final String TAB = "    ";

	//标签自定义属性
	private final String DTO_CLASS = "_dtoClass";

	@Override
	public void render() {
		Map<String, Object> argsMap = (Map)this.args[1];
		String dtoClassName = (String) argsMap.get(DTO_CLASS);
		Class dtoClass = null;
		try {
			dtoClass = Class.forName(dtoClassName);
			ObjectMeta objectMeta = MetadataUtils.getDTOMeta(dtoClass);
			StringBuilder stringBuilder = new StringBuilder();
			for(Method method : dtoClass.getMethods()) {
				//从get方法上取FieldDef和EdisMode注解信息
				if(POJOUtils.isGetMethod(method)){
					String beanField = POJOUtils.getBeanField(method);
					FieldMeta fieldMeta1 = objectMeta.getFieldMetaById(beanField);

				}
			}

			stringBuilder.append("<form id=\"queryForm\" class=\"easyui-form\" method=\"post\" fit=\"true\">");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}


	}
}
