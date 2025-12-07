<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>注册 - 二手物品信息</title>
    <link rel="stylesheet" href="static/css/styles.css">
</head>
<body>
<main class="auth-card">
    <h2>注册</h2>
    <c:if test="${not empty error}">
        <div class="alert">${error}</div>
    </c:if>
    <form method="post" action="${pageContext.request.contextPath}/register">
        <label>用户名
            <input type="text" name="username" required>
        </label>
        <label>密码
            <input type="password" name="password" required>
        </label>
        <button type="submit">注册</button>
    </form>
    <p class="auth-switch">已有账号？<a href="${pageContext.request.contextPath}/login">去登录</a></p>
</main>
</body>
</html>
