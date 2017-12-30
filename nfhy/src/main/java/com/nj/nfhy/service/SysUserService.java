package com.nj.nfhy.service;

import com.nj.nfhy.pojo.SysUser;

import java.util.List;
import java.util.Map;

public interface SysUserService {

    public SysUser get(Long userId);

    public Map<String, Object> getUserByLoginAccountPassword(String loginAccount, String password);

    public Map<String, Object> findByLoginAccount(String loginAccount);

    public List<Map<String, Object>> selectUserRoleList(Long userId);

    public List<Map<String, Object>> selectRoleAuthorityList(String roleIds);

    public List<Map<String, Object>> selectRolePermissionList(String roleIds);
}