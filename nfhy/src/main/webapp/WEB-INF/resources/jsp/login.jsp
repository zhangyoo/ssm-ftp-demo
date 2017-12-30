<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>登录</title>
    <link href="${ctx}/resources/css/style.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${ctx}/resources/js/demo.js"></script>
</head>
<body>
<form action="/nfhy/login" method="get">
    <p><label>用户名：</label><input type="text" name="loginAccount" /></p>
    <p><label>密码：</label><input type="password" name="password"></p>
    <p><label>记住我：</label><input type="checkbox" name="remberMe" /></p>
    <input type="submit" value="登录" />
</form>
</body>
</html>