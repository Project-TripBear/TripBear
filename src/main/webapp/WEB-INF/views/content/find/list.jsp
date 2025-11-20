<%-- 파일 경로: /WEB-INF/views/content/find/list.jsp (클린 코드 최종) --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="board-page">
    <div class="board-header">
        <h1 class="board-title">동행 찾기 게시판</h1>
        <p class="board-subtitle">함께 여행할 동행을 찾고 실시간 소통을 이어가세요.</p>
    </div>

    <div class="board-table-wrapper">
        <table class="board-table">
            <thead>
                <tr>
                    <th>No.</th>
                    <th>제목</th>
                    <th>글쓴이</th>
                    <th>작성일</th>
                    <th>조회수</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty list}">
                        <c:forEach items="${list}" var="dto">
                            <tr onclick="location.href='${pageContext.request.contextPath}/findboard/view?seq=${dto.find_board_id}'">
                                <td class="numeric">${dto.find_board_id}</td>
                                <td class="title">
                                    <a href="${pageContext.request.contextPath}/findboard/view?seq=${dto.find_board_id}">
                                        ${dto.find_board_title}
                                    </a>
                                    <c:if test="${dto.commentCount > 0}">
                                        <span class="comment-count">[${dto.commentCount}]</span>
                                    </c:if>
                                </td>
                                <td>${dto.nickname}</td>
                                <td>${dto.find_board_regdate}</td>
                                <td class="numeric">${dto.find_board_view_count}</td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="7">
                                <div class="board-empty">게시물이 없습니다.</div>
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>

    <div class="board-actions">
        <form method="GET" action="<c:url value='/findboard/list'/>" class="board-search">
            <input type="hidden" name="page" value="1">
            <select name="searchType">
                <option value="title_content" ${searchMap.searchType == 'title_content' ? 'selected' : ''}>제목+내용</option>
                <option value="nickname" ${searchMap.searchType == 'nickname' ? 'selected' : ''}>작성자</option>
            </select>
            <input type="text" name="searchKeyword" placeholder="검색어를 입력하세요" value="${searchMap.searchKeyword}">
            <button type="submit">검색</button>
        </form>
        <div class="board-button-group">
            <sec:authorize access="hasAuthority('ACTIVE')">
                <button type="button" class="btn btn-primary" onclick="location.href='<c:url value='/findboard/add'/>';">글쓰기</button>
            </sec:authorize>
        </div>
    </div>

    <div class="board-pagination">
        <c:set var="params" value="&searchType=${searchMap.searchType}&searchKeyword=${searchMap.searchKeyword}" />

        <c:if test="${paging.prev}">
            <a href="${pageContext.request.contextPath}/findboard/list?page=${paging.startPage - 1}${params}">이전</a>
        </c:if>

        <c:forEach begin="${paging.startPage}" end="${paging.endPage}" var="i">
            <c:choose>
                <c:when test="${i == paging.page}">
                    <strong>${i}</strong>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/findboard/list?page=${i}${params}">${i}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <c:if test="${paging.next}">
            <a href="${pageContext.request.contextPath}/findboard/list?page=${paging.endPage + 1}${params}">다음</a>
        </c:if>
    </div>
</div>