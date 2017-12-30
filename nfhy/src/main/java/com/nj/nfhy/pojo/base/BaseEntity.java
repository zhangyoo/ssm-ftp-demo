package com.nj.nfhy.pojo.base;

import java.io.Serializable;

public class BaseEntity implements Serializable {

	private static final long serialVersionUID = -8438496965629007065L;
	private int isDel;
	private Integer createTime;
    private Integer updateTime;

	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public Integer getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Integer updateTime) {
		this.updateTime = updateTime;
	}
	

}
