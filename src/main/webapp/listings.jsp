<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>二手物品信息列表</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
</head>
<body>
<header>
    <h1>二手物品信息</h1>
    <nav>
        <a href="${pageContext.request.contextPath}/listings">全部物品</a>
        <a class="btn" href="${pageContext.request.contextPath}/listings/new">发布物品</a>
        <a href="${pageContext.request.contextPath}/logout">退出登录</a>
    </nav>
</header>
<main>
    <section class="search-bar">
        <form method="get" action="${pageContext.request.contextPath}/listings">
            <input type="text" name="keyword" value="${param.keyword}" placeholder="输入关键字搜索">
            <button type="submit">搜索</button>
        </form>
    </section>
    <section class="listing-grid">
        <c:if test="${empty listings}">
            <p class="muted">暂无物品信息。</p>
        </c:if>
        <c:forEach var="item" items="${listings}">
            <article>
                <div class="card-header">
                    <h3>${item.title}</h3>
                    <span class="badge">数量：${item.quantity}</span>
                </div>
                <p class="description">${item.description}</p>
                <p class="muted">发布者：${item.ownerUsername}</p>
                <c:if test="${sessionScope.currentUser.id == item.userId}">
                    <div class="card-actions">
                        <form method="get" action="${pageContext.request.contextPath}/listings/edit">
                            <input type="hidden" name="id" value="${item.id}">
                            <button type="submit">编辑</button>
                        </form>
                        <form method="post" action="${pageContext.request.contextPath}/listings/delete" onsubmit="return confirm('确定删除该物品吗？');">
                            <input type="hidden" name="id" value="${item.id}">
                            <button type="submit" class="danger">删除</button>
                        </form>
                    </div>
                </c:if>
            </article>
        </c:forEach>
    </section>
</main>
</body>
</html>
