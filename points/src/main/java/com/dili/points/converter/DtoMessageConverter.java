package com.dili.points.converter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.ss.exception.InternalException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by asiam on 2018/3/19 0019.
 */
public class DtoMessageConverter {
	/**
	 * @param json 要进行转换为DTO的原json字符串
	 * @param clz   要进行转换的目标DTO的类型
	 * @return 返回转换后的Optional<DTO>
	 */
	public static <T extends IDTO> Optional<T> convert(String json,Class<T>clz) {
		if(StringUtils.isNoneBlank(json)) {
			ObjectMapper gson = new ObjectMapper();
			gson.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
			gson.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			gson.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
			try {
				DTO dto = gson.readValue(json, new TypeReference<DTO>() {});
				return Optional.ofNullable(DTOUtils.proxy(dto, clz));
			} catch (IOException e) {
				throw new InternalException(e);
			}
		}
		return Optional.empty();
	}
	/**
	 * @param json 要进行转换为Map的原json字符串
	 * @param clz   要进行转换的目标Map的类型
	 * @return 返回转换后的Optional<Map>
	 */
	public static  Optional<Map<String,Object>> convertAsMap(String json) {
		if(StringUtils.isNoneBlank(json)) {
			ObjectMapper gson = new ObjectMapper();
			gson.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
			gson.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			gson.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
			try {
				HashMap<String, Object> map = gson.readValue(json, new TypeReference<HashMap<String, Object>>() {});
				return Optional.ofNullable(map);
			} catch (IOException e) {
				throw new InternalException(e);
			}
		}
		return Optional.empty();
	}
	/**
	 * @param json 要进行转换为Map的原json字符串
	 * @param clz   要进行转换的目标Map的类型
	 * @return 返回转换后的Optional<Map>
	 */
	public static  List<Map<String,Object>> convertAsList(String json) {
		if(StringUtils.isNoneBlank(json)) {
			ObjectMapper gson = new ObjectMapper();
			gson.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
			gson.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			gson.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
			try {
				List<Map<String,Object>> list = gson.readValue(json, new TypeReference<List<Map<String,Object>>>() {});
				return list;
			} catch (IOException e) {
				throw new InternalException(e);
			}
		}
		return Collections.emptyList();
	}
}