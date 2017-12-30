package com.nj.nfhy.controller;

import com.nj.nfhy.controller.base.BaseController;
import com.nj.nfhy.service.SysUserService;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class HomeController extends BaseController {

    // logger日志
    private final static Logger logger = Logger.getLogger(HomeController.class);

    @Resource
    private SysUserService sysUserService;

    /**
     * 首页
     * @param request
     * @param model
     * @author 88386726 create by 20171208
     * @return
     */
    @RequestMapping("/login.htm")
    public String loginHtm(HttpServletRequest request, Model model) {

        return "login";
    }

    /**
     * 登录
     * @param request
     * @param redirectAttributes
     * @author 88386726 create by 20171208
     * @return
     */
    @RequestMapping("/login")
    public String login(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Boolean remember = false;
        String loginAccount = request.getParameter("loginAccount");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("remberMe");
        if(rememberMe != null && "on".equals(rememberMe)){
            remember = true;
        }
        //查询账号是否存在
        Map<String, Object> sysUser = sysUserService.getUserByLoginAccountPassword(loginAccount,password);
        if(sysUser != null){
            SecurityUtils.getSubject().login(new UsernamePasswordToken(loginAccount, password,remember));
            return "redirect:/index.htm";
        }else{
            redirectAttributes.addFlashAttribute("message","用户名或密码错误");
            return "redirect:/login.jsp?error=1";
        }
    }

    /**
     * 首页
     * @param request
     * @param model
     * @author 88386726 create by 20171208
     * @return
     */
    @RequestMapping("/index.htm")
    public String index(HttpServletRequest request, Model model) {
        Subject subject = SecurityUtils.getSubject();
        String info1 = "";
        String info2 = "";
        if(subject.hasRole("ROLE_ADMIN")){
            logger.info("该用户有角色权限：ROLE_ADMIN");
            info1 = "该用户有角色权限：ROLE_ADMIN";
        }
        if(subject.isPermitted("100100100")){
            logger.info("该用户有操作权限：苏宁官网-监控中心网站-增加");
            info2 = "该用户有操作权限：苏宁官网-监控中心网站-增加";
        }
        model.addAttribute("userName",subject.getPrincipal());
        model.addAttribute("meaasge1",info1);
        model.addAttribute("meaasge2",info2);
        return "index";
    }

    /**
     * 退出
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value="/logout")
    public String logout(RedirectAttributes redirectAttributes ){
        //使用权限管理工具进行用户的退出，跳出登录，给出提示信息
        SecurityUtils.getSubject().logout();
        redirectAttributes.addFlashAttribute("message", "您已安全退出");
        logger.info("您已安全退出");
        return "redirect:/login";
    }

    /**
     *
     * @return
     */
    @RequestMapping("/error.htm")
    public String error(){

        return "error";
    }

}