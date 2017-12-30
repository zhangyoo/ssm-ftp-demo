package com.nj.nfhy.service.impl;

import com.nj.nfhy.dao.SysRoleMapper;
import com.nj.nfhy.dao.SysUserMapper;
import com.nj.nfhy.pojo.SysUser;
import com.nj.nfhy.service.SysUserService;
import com.nj.nfhy.util.basicUtils.MD5Util;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户信息相关接口
 * @author  zhangyong create by 20171206
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    // logger日志
    private final static Logger logger = Logger.getLogger(SysUserServiceImpl.class);

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Override
    public SysUser get(Long userId){
        return sysUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public Map<String, Object> getUserByLoginAccountPassword(String loginAccount, String password){
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("loginAccount",loginAccount);
        map.put("password",MD5Util.GetMD5Code(password));

        return sysUserMapper.selectSysUserList(map);
    }

    @Override
    public Map<String, Object> findByLoginAccount(String loginAccount){
        return sysUserMapper.findByLoginAccount(loginAccount);
    }

    @Override
    public List<Map<String, Object>> selectUserRoleList(Long userId){
        return sysUserMapper.selectUserRoleList(userId);
    }

    @Override
    public List<Map<String, Object>> selectRoleAuthorityList(String roleIds){
        return sysRoleMapper.selectRoleAuthorityList(roleIds);
    }

    @Override
    public List<Map<String, Object>> selectRolePermissionList(String roleIds){
        return sysRoleMapper.selectRolePermissionList(roleIds);
    }
}