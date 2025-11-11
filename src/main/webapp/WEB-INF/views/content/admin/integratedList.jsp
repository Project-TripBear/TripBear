<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<div class="admin-content-wrapper">

    <h3 class="page-title mb-4">통합 게시판 관리</h3>
    
    <div class="board-nav-tabs mb-4">
        <ul class="nav nav-tabs admin-tab-style">
            <li class="nav-item">
                <a class="nav-link active" href="${contextPath}/admin/board/integratedList">전체</a>
            </li>
            <li class="nav-item"><a class="nav-link" href="#">동행찾기</a></li>
            <li class="nav-item"><a class="nav-link" href="#">후기/추천</a></li>
            <li class="nav-item"><a class="nav-link" href="#">공지/질문</a></li>
        </ul>
        
        <div class="d-flex align-items-center search-box">
            <select class="form-control form-control-sm mr-2 search-select">
                <option>제목</option>
                <option>작성자</option>
            </select>
            <input type="text" class="form-control form-control-sm mr-2 search-input" placeholder="검색어를 입력하세요">
            <button class="btn btn-primary btn-sm search-button">검색</button>
        </div>
    </div>
    
    <div class="card shadow-sm">
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-striped admin-list-table mb-0">
                    <thead>
                        <tr>
                            <th style="width: 5%;">번호</th>
                            <th style="width: 10%;">게시판</th>
                            <th style="width: 45%;">제목</th>
                            <th style="width: 10%;">작성자</th>
                            <th style="width: 10%;">작성일</th>
                            <th style="width: 5%;">추천</th>
                            <th style="width: 5%;">조회수</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty boardList}">
                                <tr>
                                    <td colspan="7" class="text-center py-4">조회된 게시글이 없습니다.</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="board" items="${boardList}">
                                    <tr>
                                        <td>${board.seq}</td>
                                        <td><span class="badge badge-secondary">${board.boardType}</span></td>
                                        <td class="text-left">
                                            <%-- 상세 페이지 링크 --%>
                                            <a href="${contextPath}/admin/board/view?seq=${board.seq}" class="text-dark font-weight-bold">
                                                ${board.title}
                                            </a>
                                            <c:if test="${board.commentCount > 0}">
                                                <span class="comment-count text-primary ml-1">(${board.commentCount})</span>
                                            </c:if>
                                        </td>
                                        <td>${board.nickname}</td>
                                        <td><fmt:formatDate value="${board.regdate}" pattern="yyyy-MM-dd HH:mm"/></td>
                                        <td>${board.likeCount}</td>
                                        <td>${board.viewCount}</td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    
    <div class="d-flex justify-content-center mt-4">
    </div>

</div>

<%-- integratedList.jsp 하단 수정 --%>
<div class="d-flex justify-content-center mt-4">
    <nav>
        <ul class="pagination">
     
            <%-- 이전 페이지 블록 버튼 (<<) --%>
            <c:if test="${paging.startPage > 1}">
                <li class="page-item">
                    <%-- ★★★ [수정] currentPage -> page ★★★ --%>
                    <a class="page-link" 
                       href="${pageContext.request.contextPath}/admin/board/integratedList?page=${paging.startPage - 1}" 
                       aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
            </c:if>

            <%-- 페이지 번호 출력 --%>
            <c:forEach begin="${paging.startPage}" end="${paging.endPage}" var="i">
                <%-- ★★★ [수정] currentPage -> page ★★★ --%>
                <li class="page-item ${i == paging.page ? 'active' : ''}">
                    <%-- ★★★ [수정] currentPage -> page ★★★ --%>
                    <a class="page-link" 
                       href="${pageContext.request.contextPath}/admin/board/integratedList?page=${i}">
                       ${i}
                    </a>
                </li>
            </c:forEach>

            <%-- 다음 페이지 블록 버튼 (>>) --%>
            <c:if test="${paging.endPage < paging.totalPage}">
                <li class="page-item">
                    <%-- ★★★ [수정] currentPage -> page ★★★ --%>
                    <a class="page-link" 
                       href="${pageContext.request.contextPath}/admin/board/integratedList?page=${paging.endPage + 1}" 
                       aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </c:if>
        </ul>
    </nav>
</div>