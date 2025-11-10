<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!-- list.jsp -->

<main>
	<div class="notice-container">
		
		<div class="notice-title">
				<h2>공지사항</h2>
		</div>
			
		<table class="notice-table">
			<thead>
				<tr>
					<th>No.</th>
					<th>게시글</th>
					<th>글쓴이</th>
					<th>작성일</th>
					<th>조회수</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${empty list}">
				<tr>
					<td colspan="5">게시물이 없습니다.</td>
				</tr>
				</c:if>	
					
				<c:forEach items="${list}" var="notice">
				<tr>
					<td>${notice.noticePostId}</td>
					<td class="title">
						<a href="<c:url value='/notice/view?id=${notice.noticePostId}'/>">${notice.noticeHeader}</a>
					</td>
					<td>관리자</td>
					<td>${notice.noticeRegdate}</td>
                    <td>${notice.noticeViewCount}</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
			
			<div class="notice-footer">
				<div class="search-box">
					<form method="GET" action="<c:url value='/notice/list'/>">
						<input type="text" name="search" placeholder="Search">
						<button type="submit">검색</button>
					</form>
				</div>
				<div class="pagination">
    				${pagebar}
				</div>
			
				<div class="table-options">
					<sec:authorize access="hasRole('ROLE_ADMIN')">
						<button type="button" class="btn btn-primary" 
							onclick="location.href='<c:url value='/notice/add'/>';">글쓰기</button>
					</sec:authorize>
				</div>
			</div>
		</div>
	</main>


    <script src="${pageContext.request.contextPath}/asset/js/main.js"></script>	
