<%-- 파일 경로: /WEB-INF/views/content/admin/userlist.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- 이 페이지 전용 CSS (Font Awesome) --%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>

<h1>회원 정보 조회</h1>

<form method="GET" action="${pageContext.request.contextPath}/admin/user/list">
    <div class="search-bar">
        <select name="searchType">
            <option value="real_name" <c:if test="${param.searchType == 'real_name'}">selected</c:if>>이름</option>
            <option value="email" <c:if test="${param.searchType == 'email'}">selected</c:if>>이메일</option>
        </select>
        <input type="text" name="keyword" placeholder="검색어를 입력하세요" value="${param.keyword}">
        
        <select name="status" onchange="this.form.submit()">
            <option value="">전체 상태</option>
            <option value="1" <c:if test="${param.status == '1'}">selected</c:if>>활동중</option>
            <option value="2" <c:if test="${param.status == '2'}">selected</c:if>>정지</option>
        </select>
        
        <button type="submit" class="btn primary"><i class="fa-solid fa-magnifying-glass"></i> 검색</button>
    </div>
</form>

<table class="admin-table">
    <thead>
        <tr>
            <th>회원번호</th>
            <th>닉네임</th>
            <th>이름</th>
            <th>이메일</th>
            <th>가입일</th>
            <th>상태</th>
            <th>관리</th>
        </tr>
    </thead>
    <tbody>
        <c:if test="${empty userlist}">
            <tr>
                <td colspan="7" style="text-align:center;">조건에 맞는 회원이 없습니다.</td>
            </tr>
        </c:if>
        <c:forEach items="${userlist}" var="user">
            <tr>
                <td>${user.userId}</td>
                <td>${user.nickname}</td>
                <td>${user.realName}</td>
                <td>${user.email}</td>
                <td><fmt:formatDate value="${user.regdate}" pattern="yyyy-MM-dd"/></td>
                <td>${user.status}</td>
                <td>
                    <c:if test="${user.status == '활동중'}">
                        <button type="button" class="btn danger" style="padding: 5px 10px;"
                                onclick="openSuspendModal('${user.userId}', '${user.nickname}')">정지</button>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<div class="pagination-container" style="text-align: center; margin-top: 2rem;">
    <ul class="pagination">
        
        <c:if test="${paging.prev}">
            <li class="page-item">
                <a class="page-link" href="${pageContext.request.contextPath}/admin/user/list?page=${paging.startPage - 1}&searchType=${param.searchType}&keyword=${param.keyword}&status=${param.status}">
                    &laquo;
                </a>
            </li>
        </c:if>

        <c:forEach begin="${paging.startPage}" end="${paging.endPage}" var="pageNum">
            <li class="page-item <c:if test="${paging.page == pageNum}">active</c:if>">
                <a class="page-link" href="${pageContext.request.contextPath}/admin/user/list?page=${pageNum}&searchType=${param.searchType}&keyword=${param.keyword}&status=${param.status}">
                    ${pageNum}
                </a>
            </li>
        </c:forEach>

        <c:if test="${paging.next}">
            <li class="page-item">
                <a class="page-link" href="${pageContext.request.contextPath}/admin/user/list?page=${paging.endPage + 1}&searchType=${param.searchType}&keyword=${param.keyword}&status=${param.status}">
                    &raquo;
                </a>
            </li>
        </c:if>
        
    </ul>
</div>
            
<div id="suspendModal" class="modal">
    <div class="modal-content">
        <span class="close-button" onclick="closeSuspendModal()">&times;</span>
        <h2>회원 정지 처리</h2>
        
        <form method="POST" action="${pageContext.request.contextPath}/admin/user/suspend">
            <input type="hidden" name="userId" id="userIdToSuspend">
            <p><strong id="nicknameToSuspend"></strong> 회원을 정지하시겠습니까?</p>
            <div class="input-group">
                <label for="reason">정지 사유</label>
                <input type="text" name="reason" id="reason" class="form-control" required>
            </div>
            <div class="input-group">
                <label for="duration">정지 기간 (일)</label>
                <input type="number" name="duration" id="duration" class="form-control" value="7" required>
            </div>
            <button type="submit" class="btn primary">정지 실행</button>
        </form>
    </div>
</div>

<script>
    const modal = document.getElementById('suspendModal');
    function openSuspendModal(userId, nickname) {
        document.getElementById('userIdToSuspend').value = userId;
        document.getElementById('nicknameToSuspend').innerText = nickname;
        modal.style.display = 'block';
    }
    function closeSuspendModal() {
        modal.style.display = 'none';
    }
</script>