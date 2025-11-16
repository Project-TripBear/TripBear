<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/qna.css">

<!-- ------------------------- 1. 타이틀 ------------------------- -->
<div class="content-header">
    <h1>Q&A 게시판</h1>
</div>

<!-- ------------------------- 2. 게시글 목록 ------------------------- -->
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

            <!-- 데이터 없을 때 -->
            <c:if test="${empty list}">
                <tr>
                    <td colspan="6" class="no-data">게시물이 없습니다.</td>
                </tr>
            </c:if>

            <!-- 게시글 Loop -->
            <c:forEach items="${list}" var="dto">
                <tr>
                    <td>${dto.question_board_id}</td>

                    <td class="title">
                        <a href="<c:url value='/qnaboard/view?seq=${dto.question_board_id}'/>" 
   class="post-title">
                            <span class="category-tag">[${dto.question_category_name}]</span>
                            ${dto.question_board_title}

                            <c:if test="${dto.commentCount > 0}">
                                <span class="comment-count">[${dto.commentCount}]</span>
                            </c:if>
                        </a>
                    </td>

                    <td>${dto.nickname}</td>

                    <!-- 오늘 글이면 "X시간 전" 표시 -->
                    <td>
                        <c:choose>
                            <c:when test="${dto.regHourDiff < 24}">
                                ${dto.regHourDiff}시간 전
                            </c:when>
                            <c:otherwise>
                                ${dto.regDateFormatted}
                            </c:otherwise>
                        </c:choose>
                    </td>

                    <td>${dto.question_board_view_count}</td>
                    <td>${dto.likeCount}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<!-- ------------------------- 3. 필터 영역 ------------------------- -->
<div class="filter-wrapper card-container">

    <!-- [왼쪽] 카테고리 필터 -->
    <form method="GET" action="<c:url value='/qnaboard/list'/>" class="filter-left">
        <input type="hidden" name="page" value="1">

        <select name="category" class="category-select" onchange="this.form.submit()">
            <option value="">전체 카테고리</option>
            <c:forEach items="${categoryList}" var="cat">
                <option value="${cat.question_category_id}"
                    ${searchMap.category == cat.question_category_id ? 'selected' : ''}>
                    ${cat.question_category_name}
                </option>
            </c:forEach>
        </select>
    </form>

    <!-- [가운데] 검색 -->
    <form method="GET" action="<c:url value='/qnaboard/list'/>" class="filter-center">
        <input type="hidden" name="page" value="1">
        <input type="hidden" name="category" value="${searchMap.category}">

        <select name="searchType" class="search-select">
            <option value="title_content" ${searchMap.searchType == 'title_content' ? 'selected' : ''}>제목+내용</option>
            <option value="nickname" ${searchMap.searchType == 'nickname' ? 'selected' : ''}>작성자</option>
        </select>

        <input type="text" name="searchKeyword" placeholder="검색어를 입력하세요"
               value="${searchMap.searchKeyword}" />

        <button type="submit" class="btn-search">검색</button>
    </form>

    <!-- [오른쪽] 글쓰기 버튼 -->
    <sec:authorize access="hasAuthority('ACTIVE')">
        <button type="button" class="btn-write"
                onclick="location.href='<c:url value='/qnaboard/add'/>';">
            글쓰기
        </button>
    </sec:authorize>

</div>

<!-- ------------------------- 4. 페이징 ------------------------- -->
<div class="pagination">

    <c:set var="params"
           value="&category=${searchMap.category}&searchType=${searchMap.searchType}&searchKeyword=${searchMap.searchKeyword}" />

    <c:if test="${paging.prev}">
        <a href="?page=${paging.startPage - 10}${params}">이전</a>
    </c:if>

    <c:forEach begin="${paging.startPage}" end="${paging.endPage}" var="i">
        <c:choose>
            <c:when test="${i == paging.page}">
                <a class="active" href="#!">${i}</a>
            </c:when>
            <c:otherwise>
                <a href="?page=${i}${params}">${i}</a>
            </c:otherwise>
        </c:choose>
    </c:forEach>

    <c:if test="${paging.next}">
        <a href="?page=${paging.endPage + 1}${params}">다음</a>
    </c:if>

</div>

<script>
    document.querySelectorAll(".auto-submit select").forEach(sel => {
        sel.addEventListener("change", function () {
            this.form.submit();
        });
    });
</script>

