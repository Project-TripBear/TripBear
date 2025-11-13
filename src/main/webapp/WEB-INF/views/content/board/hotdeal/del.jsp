<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
</head>
<body>
	<!-- del.jsp -->
	
	
	<div id="main">
    <div class="delete-confirm">
        <h2>게시글 삭제</h2>
        <p>정말로 이 게시글을 삭제하시겠습니까?</p>
        <p class="warning">삭제된 게시글과 첨부된 이미지는 복구할 수 없습니다.</p>
        
        <c:if test="${not empty error}">
            <p class="error-message">${error}</p>
        </c:if>
        
        <form method="POST" action="/trip/hotdeal/del">
            <!-- CSRF 토큰 (필수) -->
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="seq" value="${seq}">
            
            <div class="button-group">
            	<button type="submit" class="btn-delete">삭제</button>
                <button type="button" class="btn-cancel" onclick="history.back();">취소</button>
            </div>
        </form>
    </div>
</div>
		
</body>
</html>























