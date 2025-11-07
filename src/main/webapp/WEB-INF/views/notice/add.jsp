<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!-- noticeadd.jsp -->
<main>
    <div class="notice-form-container">
        <h2>공지사항 작성</h2>
        
        <form action="<c:url value="/notice/add.do"/>" method="POST">
            <sec:csrfInput />
            
            <div class="form-group">
                <label for="title">제목</label>
                <input type="text" id="title" name="title" class="form-control" required>
            </div>
            
            <div class="form-group">
                <label for="content">내용</label>
                <textarea id="content" name="content" class="form-control" rows="10" required></textarea>
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">등록</button>
                <button type="button" class="btn" onclick="location.href='<c:url value="/notice/list.do"/>';">취소</button>
            </div>
        </form>
    </div>
</main>