<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<main>
    <div class="notice-form-container">
        <h2>공지사항 수정</h2>
        
        <form action="<c:url value="/admin/notice/edit"/>" method="POST">
            <sec:csrfInput />
            
            <%-- 1. 수정 대상 ID (DTO: noticePostId) --%>
            <input type="hidden" name="noticePostId" value="${notice.noticePostId}">
            
            <div class="form-group">
                <label for="title">제목</label>
                <%-- 2. 제목 필드 (name="noticeHeader"로 DTO와 일치) --%>
                <input type="text" id="title" name="noticeHeader" class="form-control" value="${notice.noticeHeader}" required>
            </div>
            
            <div class="form-group">
                <label for="content">내용</label>
                <%-- 3. 내용 필드 (name="noticeContent"로 DTO와 일치) --%>
                <textarea id="content" name="noticeContent" class="form-control" rows="10" required>${notice.noticeContent}</textarea>
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">수정</button>
                <button type="button" class="btn" onclick="history.back();">취소</button>
            </div>
        </form>
    </div>
</main>