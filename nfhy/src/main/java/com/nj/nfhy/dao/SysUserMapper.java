package com.nj.nfhy.dao;

import com.nj.nfhy.pojo.SysUser;

import java.util.List;
import java.util.Map;

public interface SysUserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    Map<String, Object> selectSysUserList(Map<String, Object> map);

    Map<String, Object> findByLoginAccount(String loginAccount);

    List<Map<String, Object>> selectUserRoleList(Long userId);
}