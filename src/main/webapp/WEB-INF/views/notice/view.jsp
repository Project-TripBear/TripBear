<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %> 

<!-- notice.view.jsp  -->

<main>
    <div class="notice-view-container">
        <h2>공지사항</h2>
        
        <div class="notice-header">
            <h3>${notice.title}</h3>
            <div class="notice-meta">
                <span>작성자: 관리자</span>
                <span>작성일: <fmt:formatDate value="${notice.regdate}" pattern="yyyy-MM-dd" /></span>
                <span>조회수: ${notice.viewCount}</span>
            </div>
        </div>
        
        <div class="notice-content">

            <c:out value="${notice.content}" escapeXml="false" />
        </div>
        
        <div class="notice-actions">
            <button type="button" class="btn" onclick="location.href='<c:url value="/notice/list.do"/>';">목록</button>

            <sec:authorize access="hasRole('ROLE_ADMIN')">
                <button type="button" class="btn" onclick="location.href='<c:url value="/notice/edit.do?id=${notice.noticeId}"/>';">수정</button>

                <form action="<c:url value="/notice/delete.do"/>" method="POST" style="display:inline;" onsubmit="return confirm('정말 삭제하시겠습니까?');">
                    <sec:csrfInput />
                    <input type="hidden" name="id" value="${notice.noticeId}" />
                    <button type="submit" class="btn btn-danger">삭제</button>
                </form>
            </sec:authorize>
        </div>
    </div>
</main>