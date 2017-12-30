package com.nj.nfhy.util.basicUtils;

import com.alibaba.fastjson.JSONArray;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ValidateUtil {

	@SuppressWarnings("rawtypes")
	public static boolean isNotEmpty(Object object) {

		if (null == object) {
			return false;
		}

		if (object instanceof String) {

			return !"".equals(object);
		}

		if (object instanceof Object[]) {

			return ((Object[]) object).length > 0;
		}

		if (object instanceof List) {

			return !((List) object).isEmpty();
		}

		if (object instanceof Set) {

			return !((Set) object).isEmpty();
		}

		if (object instanceof Map) {
			return !((Map) object).isEmpty();
		}

		if(object instanceof JSONArray){
			return !((JSONArray) object).isEmpty();
		}
		
		return true;
	}

	public static boolean isEmpty(Object object) {

		return !isNotEmpty(object);
	}
}
