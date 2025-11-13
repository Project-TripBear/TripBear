<%-- 파일 경로: /WEB-INF/views/content/qna/list.jsp (클린 코드 최종) --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%-- 1. 타이틀 영역 (style 속성 제거) --%>
<div class="content-header"> 
    <h1>Q&A 게시판</h1>
</div>

<%-- 2. 게시글 목록 테이블 (style 속성 제거) --%>
<div class="card-container">
    <table class="notice-table">
        <thead>
            <tr>
                <th>No.</th>
                <th>제목</th>
                <th>글쓴이</th>
                <th>작성일</th>
                <th>조회수</th>
                <th>좋아요</th>
            </tr>
        </thead>
        <tbody>
            <%-- 데이터 없을 때 --%>
            <c:if test="${empty list}">
            <tr>
                <td colspan="6" class="no-data">게시물이 없습니다.</td>
            </tr>
            </c:if>	
            
            <%-- 데이터 루프 --%>
    	 <c:forEach items="${list}" var="dto">
            <tr>
                <td>${dto.question_board_id}</td>
                <td class="title">
                    <a href="<c:url value='/qnaboard/view?seq=${dto.question_board_id}'/>">
                        ${dto.question_board_title} 
                        <%-- 댓글 수 표시 (style 속성 제거) --%>
                        <c:if test="${dto.commentCount > 0}">
                            <span class="comment-count">[${dto.commentCount}]</span>
                        </c:if>
                    </a>
                </td>
                <td>${dto.nickname}</td>
                <td>${dto.question_board_regdate}</td>
                <td>${dto.question_board_view_count}</td>
                <td>${dto.likeCount}</td>
            </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<%-- 3. 검색 및 버튼 영역 (Footer) (style 속성 제거) --%>
<div class="notice-footer">
    
    <%-- 검색 폼 --%>
    <div class="search-box">
        <form method="GET" action="<c:url value='/qnaboard/list'/>">
            <input type="hidden" name="page" value="1">
            <select name="searchType" class="search-select">
                <option value="title_content" ${searchMap.searchType == 'title_content' ? 'selected' : ''}>제목+내용</option>
                <option value="nickname" ${searchMap.searchType == 'nickname' ? 'selected' : ''}>작성자</option>
            </select>
            <input type="text" name="searchKeyword" placeholder="검색어를 입력하세요" value="${searchMap.searchKeyword}">
            <button type="submit" class="btn btn-primary">검색</button>
        </form>
    </div>
    
    <%-- 글쓰기 버튼 --%>
    <div class="table-options">
        <sec:authorize access="hasAuthority('ACTIVE')">
            <button type="button" class="btn btn-primary" onclick="location.href='<c:url value='/qnaboard/add'/>';">글쓰기</button>
        </sec:authorize>
    </div>
</div>

<%-- 4. 페이징 영역 --%>
<div class="pagination">
    <c:set var="params" value="&searchType=${searchMap.searchType}&searchKeyword=${searchMap.searchKeyword}" />

    <c:if test="${paging.prev}">
        <a href="${pageContext.request.contextPath}/qnaboard/list?page=${paging.startPage - 1}${params}">이전</a>
    </c:if>

    <c:forEach begin="${paging.startPage}" end="${paging.endPage}" var="i">
        <c:choose>
            <c:when test="${i == paging.page}">
                <a href="#!" class="active">${i}</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/qnaboard/list?page=${i}${params}">${i}</a>
            </c:otherwise>
        </c:choose>
    </c:forEach>

    <c:if test="${paging.next}">
        <a href="${pageContext.request.contextPath}/qnaboard/list?page=${paging.endPage + 1}${params}">다음</a>
    </c:if>
</div>