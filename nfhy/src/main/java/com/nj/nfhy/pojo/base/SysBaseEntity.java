package com.nj.nfhy.pojo.base;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class SysBaseEntity implements Serializable {

	private static final long serialVersionUID = 301665695974038672L;

	/**
	 * 排序
	 */
	private Map<String, String> sortedConditions = new LinkedHashMap<String, String>();

	public Map<String, String> getSortedConditions() {
		return sortedConditions;
	}
	public void setSortedConditions(Map<String, String> sortedConditions) {
		this.sortedConditions = sortedConditions;
	}

}
