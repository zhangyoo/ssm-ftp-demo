package com.nj.nfhy.util.timer;

import com.nj.nfhy.pojo.User;
import com.nj.nfhy.service.UserService;

import javax.annotation.Resource;

public class MyTimer {

    @Resource
    private UserService userService;

    public void executeMethod() {
        System.out.println(System.currentTimeMillis());
        System.out.println("timer");
        User userInfo = userService.getUserById(1);
        System.out.println(userInfo.getUserName());
    }
}
