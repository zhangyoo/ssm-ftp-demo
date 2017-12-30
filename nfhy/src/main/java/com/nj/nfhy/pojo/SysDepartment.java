package com.nj.nfhy.pojo;

import com.nj.nfhy.pojo.base.SysBaseEntity;

public class SysDepartment extends SysBaseEntity {
    private static final long serialVersionUID = -6454347353753608539L;
    private Long id;

    private String departmentKey;

    private String departmentValue;

    private String description;

    private String parentDepartmentkey;

    private String createTime;

    private String updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentKey() {
        return departmentKey;
    }

    public void setDepartmentKey(String departmentKey) {
        this.departmentKey = departmentKey == null ? null : departmentKey.trim();
    }

    public String getDepartmentValue() {
        return departmentValue;
    }

    public void setDepartmentValue(String departmentValue) {
        this.departmentValue = departmentValue == null ? null : departmentValue.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getParentDepartmentkey() {
        return parentDepartmentkey;
    }

    public void setParentDepartmentkey(String parentDepartmentkey) {
        this.parentDepartmentkey = parentDepartmentkey == null ? null : parentDepartmentkey.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime == null ? null : updateTime.trim();
    }
}