package com.nj.nfhy.controller;

import com.alibaba.fastjson.JSONObject;
import com.nj.nfhy.controller.base.BaseController;
import com.nj.nfhy.pojo.User;
import com.nj.nfhy.service.UserService;
import com.nj.nfhy.util.basicUtils.ConfigInfo;
import com.nj.nfhy.util.basicUtils.ModelResults;
import com.nj.nfhy.util.basicUtils.MsgInfo;
import com.nj.nfhy.util.redisUtils.RedisUtilImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    // logger日志
    private final static Logger logger = Logger.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @Resource
    private RedisUtilImpl redisUtilImpl;

    @Resource
    private ConfigInfo configInfo;

    /**
     * 显示用户信息
     * @param request
     * @param model
     * @author 88386726 by 20171113
     * @return
     */
    @RequestMapping("/showUser.htm")
    public String toIndex(HttpServletRequest request, Model model) {

        System.out.println(configInfo.getIp());

        redisUtilImpl.set("88386726","zy");
        redisUtilImpl.HSetSetValue("nfhy_","firstName","zy");
        redisUtilImpl.HSetSetValue("nfhy_","secondName","xrr");
        redisUtilImpl.HSetSetValue("nfhy_","thirdName","xcs");
        redisUtilImpl.HSetSetValue("nfhy_","firstName","zy");
        System.out.println(redisUtilImpl.HSetGetValue("nfhy_","firstName"));
        redisUtilImpl.HSetSetValue("nfhy_","firstName","zhangyoo");
        int userId = Integer.parseInt(request.getParameter("id"));
        User user = userService.getUserById(userId);
        user.setUserName(user.getUserName() + "--" + redisUtilImpl.get("88386726"));
        model.addAttribute("user", user);
        userService.insertUser();
        return "user";
    }

    /**
     * 获取用户信息
     * @param request
     * @author 88386726 by 20171113
     * @return
     */
    @RequestMapping( value = "/getUser.json", method = RequestMethod.POST)
    public void getUser(HttpServletRequest request, HttpServletResponse response) {
        // 封装返回的结果
        ModelResults results = new ModelResults();

        try
        {
            //获取前端传来的json数据
            JSONObject json = super.initJsonParam(request);
            results = userService.selectUserBySearch(json, request);
        }
        catch(Exception e)
        {
            logger.error(e);
            e.printStackTrace();
            results.setCode(MsgInfo.a_error_code);
            results.setMessage(MsgInfo.a_error_msg);
        }
        results.printJson(results, response, "");
    }

    /**
     * 执行多线程
     * @param request
     * @author 88386726 by 20171113
     * @return
     */
    @RequestMapping( value = "/getThreadRun.json", method = RequestMethod.POST)
    public void getThreadRun(HttpServletRequest request, HttpServletResponse response) {
        // 封装返回的结果
        ModelResults results = new ModelResults();

        try
        {
            //获取前端传来的json数据
            JSONObject json = super.initJsonParam(request);
            results = userService.getThreadRun(json, request);
        }
        catch(Exception e)
        {
            logger.error(e);
            e.printStackTrace();
            results.setCode(MsgInfo.a_error_code);
            results.setMessage(MsgInfo.a_error_msg);
        }
        results.printJson(results, response, "");
    }
}