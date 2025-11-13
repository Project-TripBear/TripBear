<%-- /WEB-INF/views/content/find/add.jsp (수정 후) --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="notice-form-container">
    <h2>동행 찾기 게시글 작성</h2>
    
    <form action="<c:url value="/findboard/add"/>" method="POST" enctype="multipart/form-data">
        <sec:csrfInput />
        
        <div class="form-group">
            <label for="title">제목</label>
            <input type="text" id="title" 
name="find_board_title" class="form-control" required>
        </div>
        
        <div class="form-group">
            <label for="content">내용</label>
            <textarea id="content" name="find_board_content" class="form-control" rows="10" required></textarea>
        </div>
        
        <%-- 파일 업로드 필드 (제거됨) --%>

        <%-- 키워드/해시태그 입력 필드 (find_board_keyword는 유지) --%>
        <div class="form-group">
            <label for="keyword">키워드 (쉼표로 구분하여 입력)</label>
            <input type="text" id="keyword" name="find_board_keyword" placeholder="예: 제주,혼자여행,맛집" class="form-control">
        </div>
    
        <div class="form-actions">
            <button type="submit" class="btn btn-primary">등록</button>
            <button type="button" class="btn btn-cancel" onclick="location.href='<c:url value="/findboard/list"/>';">취소</button>
        </div>
    </form>
</div>