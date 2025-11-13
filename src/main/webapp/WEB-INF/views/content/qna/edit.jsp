<%-- edit.jsp (수정 후) --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<main>
    <div class="notice-form-container">
        <h2>동행 찾기 게시글 수정</h2>
        
        <form action="<c:url value="/qnaboard/edit"/>" method="POST" enctype="multipart/form-data">
            <sec:csrfInput />
            
            
            <input type="hidden" name="question_board_id" value="${dto.question_board_id}">
            
            <input type="hidden" name="user_id" value="${dto.user_id}">
            <%-- <input type="hidden" name="qna_board_image" value="${dto.qna_board_image}"> (제거됨) --%>
            
            <div class="form-group">
                <label for="title">제목</label>
                <input type="text" id="title" name="question_board_title" class="form-control" value="${dto.question_board_title}" required>
            </div>
         
         
            <div class="form-group">
                <label for="content">내용</label>
                <textarea id="content" name="question_board_content" class="form-control" rows="10" required>${dto.question_board_content}</textarea>
            </div>
            
            <%-- 파일 업로드 필드 (수정용) (제거됨) --%>
            
          
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">수정</button>
                <button type="button" class="btn btn-cancel" onclick="history.back();">취소</button>
            </div>
 
        </form>
    </div>
</main>