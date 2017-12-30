<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>欢迎登录系统</title>
    <link href="${ctx}/resources/css/style.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${ctx}/resources/js/demo.js"></script>
</head>
<body>
欢迎 ${userName} 登录系统
<p>${meaasge1}</p>
<p>${meaasge2}</p>
<p><a href="/nfhy/logout">退出系统</a> </p>
</body>
</html>