<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title><c:choose><c:when test="${not empty listing}">编辑</c:when><c:otherwise>发布</c:otherwise></c:choose>物品</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
</head>
<body>
<main>
    <h2><c:choose><c:when test="${not empty listing}">编辑</c:when><c:otherwise>发布</c:otherwise></c:choose>物品</h2>
    <form method="post" action="${pageContext.request.contextPath}/listings/<c:out value="${not empty listing ? 'edit' : 'new'}"/>">
        <c:if test="${not empty listing}">
            <input type="hidden" name="id" value="${listing.id}">
        </c:if>
        <label>名称
            <input type="text" name="title" value="${listing.title}" required>
        </label>
        <label>描述
            <textarea name="description" rows="4">${listing.description}</textarea>
        </label>
        <label>数量
            <input type="number" name="quantity" value="${listing.quantity != null ? listing.quantity : 1}" min="1" required>
        </label>
        <button type="submit">保存</button>
    </form>
    <p><a href="${pageContext.request.contextPath}/listings">返回列表</a></p>
</main>
</body>
</html>
