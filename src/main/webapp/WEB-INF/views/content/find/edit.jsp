<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<main>
    <div class="notice-form-container">
        <h2>동행 찾기 게시글 수정</h2>
        
        <form action="<c:url value="/findboard/edit"/>" method="POST" enctype="multipart/form-data">
            <sec:csrfInput />
            
            <input type="hidden" name="find_board_id" value="${dto.find_board_id}">
            <input type="hidden" name="find_board_image" value="${dto.find_board_image}">
            
            <div class="form-group">
                <label for="title">제목</label>
                <input type="text" id="title" name="find_board_title" class="form-control" value="${dto.find_board_title}" required>
            </div>
         
            <div class="form-group">
                <label for="content">내용</label>
                <textarea id="content" name="find_board_content" class="form-control" rows="10" required>${dto.find_board_content}</textarea>
            </div>
            
            <%-- 파일 업로드 필드 (수정용) --%>
            <div class="form-group">
                <label for="newFile">대표 이미지 변경</label>
                <p style="font-size: 0.9em; color: gray;">
                    <c:if test="${not empty dto.find_board_image}">현재 파일: ${dto.find_board_image}</c:if>
                    <c:if test="${empty dto.find_board_image}">첨부된 파일 없음</c:if>
                </p>
                <input type="file" id="newFile" name="newFile" class="form-control">
                <p style="font-size: 0.8em; color: var(--text-light); margin-top: 5px;">* 새 파일을 첨부하지 않으면 기존 파일이 유지됩니다.</p>
            </div>
            
            <%-- 키워드/해시태그 입력 필드 --%>
            <div class="form-group">
                <label for="keyword">키워드 (쉼표로 구분하여 입력)</label>
                <input type="text" id="keyword" name="find_board_keyword" value="${dto.find_board_keyword}" placeholder="예: 제주,혼자여행,맛집" class="form-control">
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">수정</button>
                <button type="button" class="btn btn-cancel" onclick="history.back();">취소</button>
            </div>
        </form>
    </div>
</main>