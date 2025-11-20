<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<div class="board-page">
    <div class="board-header">
        <h1 class="board-title">여행 용품 게시판</h1>
        <c:choose>
            <c:when test="${map.search == 'y'}">
                <p class="board-subtitle">'${map.word}' 검색 결과 총 ${map.totalCount}건이 있습니다.</p>
            </c:when>
            <c:otherwise>
                <p class="board-subtitle">최신 여행 용품 정보를 공유하고 댓글과 좋아요를 받아보세요.</p>
            </c:otherwise>
        </c:choose>
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
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto">
                            <tr onclick="location.href='${pageContext.request.contextPath}/hotdeal/view?seq=${dto.seq}&column=${map.column}&word=${map.word}'">
                                <td class="numeric">${dto.seq}</td>
                                <td class="title">
                                    <a href="${pageContext.request.contextPath}/hotdeal/view?seq=${dto.seq}&column=${map.column}&word=${map.word}">
                                        <c:choose>
                                            <c:when test="${not empty dto.img}">
                                                <img class="thumb" src="${pageContext.request.contextPath}/upload/${dto.img}" alt="썸네일">
                                            </c:when>
                                            <c:otherwise>
                                                <img class="thumb" src="${pageContext.request.contextPath}/resources/img/hotdeal/default.png" alt="기본 이미지">
                                            </c:otherwise>
                                        </c:choose>
                                        <span class="board-chip">${dto.status}</span>
                                        <span>${dto.subject}</span>
                                        <c:if test="${dto.commentCount > 0}">
                                        	<span class="comment-count">[${dto.commentCount}]</span>
                                    	</c:if>
                                    </a>
                                </td>
                                <td>${dto.name}</td>
                                <td>${dto.regdate}</td>
                                <td class="numeric">${dto.readcount}</td>
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
        <form id="searchForm" method="GET" action="<c:url value='/hotdeal/list'/>" class="board-search">
            <select name="column">
                <option value="hotdeal_title" ${map.column == 'hotdeal_title' ? 'selected' : ''}>제목</option>
                <option value="hotdeal_content" ${map.column == 'hotdeal_content' ? 'selected' : ''}>내용</option>
                <option value="nickname" ${map.column == 'nickname' ? 'selected' : ''}>이름</option>
            </select>
            <input type="text" name="word" value="${map.word}" placeholder="검색어를 입력하세요">
            <button type="submit">검색하기</button>
        </form>
        <div class="board-button-group">
            <sec:authorize access="hasAuthority('ACTIVE')">
                <button type="button" class="btn btn-primary"
                        onclick="location.href='${pageContext.request.contextPath}/hotdeal/add';">글쓰기</button>
            </sec:authorize>
        </div>
    </div>

    <div class="board-pagination">
        <c:if test="${totalPages > 1}">
            <c:if test="${currentPage > 1}">
                <a href="${pageContext.request.contextPath}/hotdeal/list?page=${currentPage - 1}&column=${map.column}&word=${map.word}">이전</a>
            </c:if>

            <c:forEach var="i" begin="1" end="${totalPages}">
                <c:choose>
                    <c:when test="${i == currentPage}">
                        <strong>${i}</strong>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/hotdeal/list?page=${i}&column=${map.column}&word=${map.word}">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:if test="${currentPage < totalPages}">
                <a href="${pageContext.request.contextPath}/hotdeal/list?page=${currentPage + 1}&column=${map.column}&word=${map.word}">다음</a>
            </c:if>
        </c:if>
    </div>
</div>
