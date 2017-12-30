package com.nj.nfhy.service;

import com.alibaba.fastjson.JSONObject;
import com.nj.nfhy.pojo.User;
import com.nj.nfhy.util.basicUtils.ModelResults;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    public User getUserById(int userId);

    public void insertUser();

    public ModelResults selectUserBySearch(JSONObject json, HttpServletRequest request);

    public ModelResults getThreadRun(JSONObject json, HttpServletRequest request);

}