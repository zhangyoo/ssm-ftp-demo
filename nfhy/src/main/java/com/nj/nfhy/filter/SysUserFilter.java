package com.nj.nfhy.filter;
import com.nj.nfhy.service.SysUserService;

import org.apache.shiro.web.filter.PathMatchingFilter;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 自定义用户过滤器
 * @author zhangyong
 *
 */
public class SysUserFilter extends PathMatchingFilter {

    @Resource
    private SysUserService sysUserService;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        return true;
    }
}