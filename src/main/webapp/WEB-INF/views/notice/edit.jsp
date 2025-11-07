<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!-- notice.edit -->
<main>
    <div class="notice-form-container">
        <h2>공지사항 수정</h2>
        
        <form action="<c:url value="/notice/edit.do"/>" method="POST">
            <sec:csrfInput />
            <%-- 어떤 글을 수정하는지 ID가 반드시 필요합니다. --%>
            <input type="hidden" name="noticeId" value="${notice.noticeId}">
            
            <div class="form-group">
                <label for="title">제목</label>
                <input type="text" id="title" name="title" class="form-control" value="${notice.title}" required>
            </div>
            
            <div class="form-group">
                <label for="content">내용</label>
                <textarea id="content" name="content" class="form-control" rows="10" required>${notice.content}</textarea>
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">수정</button>
                <button type="button" class="btn" onclick="history.back();">취소</button>
            </div>
        </form>
    </div>
</main>