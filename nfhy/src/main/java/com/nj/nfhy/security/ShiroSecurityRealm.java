package com.nj.nfhy.security;

import javax.annotation.Resource;

import com.nj.nfhy.service.SysUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 权限认证
 * @author zhangyong
 * @date   2017年12月7日
 */
@Component
public class ShiroSecurityRealm extends AuthorizingRealm {

    @Resource
    private SysUserService sysUserService;

    public ShiroSecurityRealm() {
        super();
    }

    /**
     * 登录认证
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String username = (String)token.getPrincipal();  //得到用户名
        String password = new String((char[])token.getCredentials()); //得到密码
        Map<String, Object> SysUser = sysUserService.findByLoginAccount(username);
        if (SysUser != null) {
            return new SimpleAuthenticationInfo(username, password, getName());
        } else {
            return null;
        }
    }


    /**
     * 权限认证
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String loginAccount = (String) principals.fromRealm(getName()).iterator().next();
        Map<String, Object> sysUser = sysUserService.findByLoginAccount(loginAccount);
        if (sysUser != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            //查询角色
            String roleIds = "";
            List<Map<String, Object>> sysUserRoleList = sysUserService.selectUserRoleList(Long.valueOf(sysUser.get("userId").toString()));
            if(sysUserRoleList != null && sysUserRoleList.size() > 0){
                for(int i=0;i<sysUserRoleList.size();i++){
                    info.addRole(sysUserRoleList.get(i).get("roleKey").toString());
                    if(i == 0){
                        roleIds += sysUserRoleList.get(i).get("roleId").toString();
                    }else{
                        roleIds += "," + sysUserRoleList.get(i).get("roleId").toString();
                    }
                }
            }
            Set<String> permissions = new HashSet<String>();
            //查询菜单权限
            if(!"".equals(roleIds)){
                List<Map<String, Object>> roleAuthorityList = sysUserService.selectRoleAuthorityList(roleIds);
                if(roleAuthorityList != null){
                    for(int j=0;j<roleAuthorityList.size();j++){
                        permissions.add(roleAuthorityList.get(j).get("menuCode").toString());
                    }
                }
            }
            //查询按钮权限
            if(!"".equals(roleIds)){
                List<Map<String, Object>> rolePermissionList = sysUserService.selectRolePermissionList(roleIds);
                if(rolePermissionList != null){
                    for(int j=0;j<rolePermissionList.size();j++){
                        permissions.add(rolePermissionList.get(j).get("menuCode").toString());
                    }
                }
            }
            if(permissions != null && permissions.size() > 0){
                info.addStringPermissions(permissions);
            }
            return info;
        } else {
            return null;
        }
    }

}
