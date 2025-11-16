<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<div class="admin-content-wrapper">

	<div class="board-nav-tabs mb-4 d-flex justify-content-between align-items-center">
	    
	    <%-- 1. 세분화된 탭 메뉴 --%>
	    <ul class="nav nav-tabs admin-tab-style">
	        <li class="nav-item">
	            <a class="nav-link ${empty boardType ? 'active' : ''}" 
	               href="${contextPath}/admin/board/integratedList">전체</a>
	        </li>
	        <li class="nav-item">
	            <a class="nav-link ${boardType eq 'tblNotice' ? 'active' : ''}" 
	               href="${contextPath}/admin/board/integratedList?boardType=tblNotice">공지사항</a>
	        </li>
	        <li class="nav-item">
	            <a class="nav-link ${boardType eq 'tblQuestionBoard' ? 'active' : ''}" 
	               href="${contextPath}/admin/board/integratedList?boardType=tblQuestionBoard">질문</a>
	        </li>
	        <li class="nav-item">
	            <a class="nav-link ${boardType eq 'tblFindBoard' ? 'active' : ''}" 
	               href="${contextPath}/admin/board/integratedList?boardType=tblFindBoard">동행찾기</a>
	        </li>
	        <li class="nav-item">
	            <a class="nav-link ${boardType eq 'tblHotDealPost' ? 'active' : ''}" 
	               href="${contextPath}/admin/board/integratedList?boardType=tblHotDealPost">핫딜</a>
	        </li>
	        <li class="nav-item">
	            <a class="nav-link ${boardType eq 'tblReviewBoard' ? 'active' : ''}" 
	               href="${contextPath}/admin/board/integratedList?boardType=tblReviewBoard">후기</a>
	        </li>
	    </ul>
		
        <!-- 검색 박스 (옵션) -->
        <div class="d-flex align-items-center search-box">
            <select class="form-control form-control-sm mr-2 search-select">
                <option>제목</option>
                <option>작성자</option>
            </select>
            <input type="text" class="form-control form-control-sm mr-2 search-input" placeholder="검색어를 입력하세요">
            <button class="btn btn-primary btn-sm search-button">검색</button>
        </div>
    </div>
    
    <!-- ✅ 게시글 목록 테이블 -->
    <div class="card shadow-sm">
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-striped admin-list-table mb-0">
					<th style="width: 10%;">관리</th>
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
										<td>
										    <span class="badge badge-secondary">
												<c:choose>
												    <c:when test="${board.boardType eq 'tblHotDealPost'}">핫딜</c:when>
												    <c:when test="${board.boardType eq 'tblRoutePost'}">여행루트</c:when>
												    <c:when test="${board.boardType eq 'tblReviewBoard'}">후기</c:when>
												    <c:when test="${board.boardType eq 'tblRecommendBoard'}">추천</c:when>
												    <c:when test="${board.boardType eq 'tblFindBoard'}">동행찾기</c:when>
													<c:when test="${board.boardType eq 'tblNotice'}">공지사항</c:when>

												    <%-- 질문 게시판은 아직 미구현이라 주석 처리 --%>
												    <c:when test="${board.boardType eq 'tblQuestionBoard'}">질문</c:when> 

												</c:choose>
										    </span>
										</td>

					                    <!-- ✅ 게시판별 상세보기 링크 분기 -->
					                    <td class="text-left">
											<c:choose>

											    <c:when test="${board.boardType eq 'tblHotDealPost'}">
											        <a href="${contextPath}/hotdeal/view?seq=${board.seq}" class="text-dark font-weight-bold">
											            ${board.title}
											        </a>
											    </c:when>

											    <c:when test="${board.boardType eq 'tblRoutePost'}">
											        <a href="${contextPath}/routepost/view/${board.seq}" class="text-dark font-weight-bold">
											            ${board.title}
											        </a>
											    </c:when>

											    <c:when test="${board.boardType eq 'tblReviewBoard'}">
											        <a href="${contextPath}/review/view?seq=${board.seq}" class="text-dark font-weight-bold">
											            ${board.title}
											        </a>
											    </c:when>

											    <c:when test="${board.boardType eq 'tblFindBoard'}">
											        <a href="${contextPath}/findboard/view?seq=${board.seq}" class="text-dark font-weight-bold">
											            ${board.title}
											        </a>
											    </c:when>

											    <c:when test="${board.boardType eq 'tblNotice'}">
											        <a href="${contextPath}/notice/view?id=${board.seq}" class="text-dark font-weight-bold">
											            ${board.title}
											        </a>
											    </c:when>

											    <c:otherwise>
											        <span class="text-muted font-weight-bold">${board.title}</span>
											    </c:otherwise>

											</c:choose>

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

</div>

<!-- ✅ 페이징 -->
<div class="d-flex justify-content-center mt-4">
    <nav>
        <ul class="pagination">
            <c:if test="${paging.startPage > 1}">
                <li class="page-item">
                    <a class="page-link" 
                       href="${contextPath}/admin/board/integratedList?page=${paging.startPage - 1}&boardType=${boardType}" 
                       aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
            </c:if>

            <c:forEach begin="${paging.startPage}" end="${paging.endPage}" var="i">
                <li class="page-item ${i == paging.page ? 'active' : ''}">
                    <a class="page-link" 
                       href="${contextPath}/admin/board/integratedList?page=${i}&boardType=${boardType}">
                       ${i}
                    </a>
                </li>
            </c:forEach>

            <c:if test="${paging.endPage < paging.totalPage}">
                <li class="page-item">
                    <a class="page-link" 
                       href="${contextPath}/admin/board/integratedList?page=${paging.endPage + 1}&boardType=${boardType}" 
                       aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </c:if>
        </ul>
    </nav>
</div>
