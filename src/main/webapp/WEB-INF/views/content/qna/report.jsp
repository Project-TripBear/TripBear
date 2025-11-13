<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<h1 class="report-title">게시글 신고</h1>

<form method="POST" action="<c:url value="/qnaboard/report"/>">
    <sec:csrfInput /> <input type="hidden" name="boardSeq" value="${boardSeq}">
    <input type="hidden" name="reportedUserId" value="${reportedUserId}">
    
    <div class="report-form-group">
        <label for="reason">신고 사유</label>
        <select id="reason" name="reason">
            <option value="스팸/홍보물">스팸/홍보물</option>
            <option value="음란물">음란물</option>
            <option value="도배">도배</option>
            <option value="정치글">정치글</option>
            <option value="기타">기타</option>
        </select>
    </div>
    
    <button type="submit" class="report-submit-btn">신고하기</button>
</form>