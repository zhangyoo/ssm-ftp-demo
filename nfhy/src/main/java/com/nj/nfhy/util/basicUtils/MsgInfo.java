package com.nj.nfhy.util.basicUtils;

public class MsgInfo {
	//2区间码
	public static final String a_suc_code = "200"; // 请求成功
	public static final String a_fail_code = "201"; // 业务错误
	//3区间码
	public static final String a_authorityError_code = "301"; // 权限错误
	//4区间码
	public static final String a_paramExcerror_code = "401"; // 请求参数错误 参数错误
	//5区间码
	public static final String a_error_code = "500"; // 请求失败
	public static final String a_loginError_code = "501";// 未登录

	//返回信息
	public static final String a_suc_msg = "请求成功"; // 请求成功
	public static final String a_suc_op_msg = "操作成功"; // 操作成功
	public static final String a_fail_msg = "业务错误"; // 业务错误
	public static final String a_error_msg = "系统内部错误"; // 系统错误
	public static final String a_loginError_msg = "登录失效，请重新登录";// 未登录
	public static final String a_paramExcerror_msg = "请输入请求数据"; // 请求参数错误 参数错误
	public static final String a_Param_NUll = "您查询的数据不存在"; // 请求成功
	public static final String a_authorityError_msg = "系统权限错误"; // 权限错误
	public static final String a_paramNull_msg = "参数为空";

}
