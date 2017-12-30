package com.nj.nfhy.dao;

import com.nj.nfhy.pojo.SysRole;

import java.util.List;
import java.util.Map;

public interface SysRoleMapper {
    int deleteByPrimaryKey(Integer roleId);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    List<Map<String, Object>> selectRoleAuthorityList(String roleIds);

    List<Map<String, Object>> selectRolePermissionList(String roleIds);
}