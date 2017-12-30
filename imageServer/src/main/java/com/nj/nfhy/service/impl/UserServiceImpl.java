package com.nj.nfhy.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nj.nfhy.dao.UserMapper;
import com.nj.nfhy.pojo.User;
import com.nj.nfhy.service.UserService;
import com.nj.nfhy.util.basicUtils.Constants;
import com.nj.nfhy.util.basicUtils.ModelResults;
import com.nj.nfhy.util.basicUtils.MsgInfo;
import com.nj.nfhy.util.threadUtil.pool.UserThreadPool;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 * 用户信息相关接口
 * @author  zhangyong create by 20171206
 */
@Service
public class UserServiceImpl implements UserService {

    // logger日志
    private final static Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Resource
    private UserMapper userMapper;

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    @Override
    public User getUserById(int userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    /**
     * 存储用户信息
     */
    @Override
    public void insertUser(){
        User user = new User();
        user.setUserName("事务");
        user.setPassword("123");
        user.setAge(18);
        int result = userMapper.insertSelective(user);
        //测试事务使用
//        throw new RuntimeException("test");
    }

    /**
     * 查询用户列表
     * @param json
     * @param request
     * @return
     */
    @Override
    public ModelResults selectUserBySearch(JSONObject json, HttpServletRequest request){
        ModelResults results = new ModelResults();
        try
        {
//            Long userId = json.getLong("userId");
            // 获取登录用户信息
//            User pue = (User) request.getSession().getAttribute(Constants.LOGIN_USER_SESSION_NAME);

            String userName = json.getString("userName");
            String page = json.getString("page");
            String pageSize = json.getString("pageSize");
            PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(pageSize));
            //业务逻辑
            Map<String, Object> map = new HashMap<String,Object>();
            List<Map<String, Object>> getUserList = userMapper.selectUserList(map);
            // 把查询的结果集放入分页插件中
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(getUserList);
            // 得到分页总数
            long resultsCount = pageInfo.getTotal();
            results.setData(pageInfo.getList());
            results.setResultsCount(resultsCount);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            results.setCode(MsgInfo.a_error_code);
            results.setMessage(MsgInfo.a_error_msg);
        }
        return results;

    }

    /**
     * 执行多线程demo
     * @param json
     * @param request
     * @return
     */
    @Override
    public ModelResults getThreadRun(JSONObject json, HttpServletRequest request){
        ModelResults results = new ModelResults();
        User user = null;
        try
        {
            // 先看线程池是否可以接受任务，如果可以，则去尝试获取执行权限
            // 如果线程池线程都在工作，且配置列表尚有剩余，则等待线程池有空闲后再尝试处理
            long start = System.currentTimeMillis();
            while (UserThreadPool.userThreadPool.getActiveCount() >= UserThreadPool.poolSize) {
                logger.info("暂时没有多余线程池");
                // 活动的线程数大于等于线程池大小时，等待，不允许提交任务。
                // 等待超过30秒则打破规则，获取任务权限，提交任务
                if (System.currentTimeMillis() - start > 30 * 1000) {
                    logger.warn("等待30秒仍没有多余的线程池，超时，尝试获取任务执行权限并添加到任务等待队列");
                    break;
                }
            }
            //调用runable执行，无返回结果
            for(int i=0;i < 25;i++){
                user = new User();
                user.setId(i);
                UserRunnable job = new UserRunnable(user);
                UserThreadPool.userThreadPool.execute(job);
            }
            //调用callable执行，有返回结果
            String result = "result:";
            //取得一个倒计时器
            CountDownLatch countDownLatch = new CountDownLatch(20);
            for(int i=0;i < 20;i++){
                user = new User();
                user.setId(i);
                UserCallable jobCall = new UserCallable(user,countDownLatch);
                Future<String> callResult = UserThreadPool.userThreadPool.submit(jobCall);
                if(callResult.get() != null){
                    result += callResult.get();
                }
            }
            countDownLatch.await();
            System.out.println(result);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            results.setCode(MsgInfo.a_error_code);
            results.setMessage(MsgInfo.a_error_msg);
        }
        return results;

    }

    class UserRunnable implements Runnable {

        private User user;

        public UserRunnable(User user) {
            this.user = user;
        }

        @Override
        public void run() {

            userRun(user);

        }

    }

    public void userRun(User user){
        System.out.println(user.getId());
    }

    class UserCallable implements Callable<String> {
        private User user;

        private CountDownLatch countDownLatch;

        public UserCallable(User user,CountDownLatch countDownLatch){
            this.user = user;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public String call() throws Exception {
            String result = userCall(user);
            //线程结束时，将计时器减一
            countDownLatch.countDown();
            return result;
        }

    }

    public String userCall(User user){
        System.out.println("call-" + user.getId());
        return "call-" + user.getId();
    }

}