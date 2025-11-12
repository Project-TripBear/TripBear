<%-- 파일 경로: /WEB-INF/views/content/admin/userlist.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- 이 페이지 전용 CSS (Font Awesome) --%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>

<h1>회원 관리</h1>

<form method="GET" action="${pageContext.request.contextPath}/admin/user/list">
    
    <div class="controls-bar">
        
        <div class="controls-left search-box">
            <select name="searchType" class="search-select">
                <option value="real_name" <c:if test="${param.searchType == 'real_name'}">selected</c:if>>이름</option>
                <option value="email" <c:if test="${param.searchType == 'email'}">selected</c:if>>이메일</option>
            </select>
            <input type="text" name="keyword" placeholder="검색어를 입력하세요" value="${param.keyword}" class="search-input">
            
            <button type="submit" class="btn primary" style="padding: 0.6rem 1.2rem;">
                <i class="fa-solid fa-magnifying-glass"></i> 검색
            </button>
        </div>
        
        <div class="controls-right">
             <select name="status" onchange="this.form.submit()" class="search-select">
                <option value="">전체 상태</option>
                <option value="1" <c:if test="${param.status == '1'}">selected</c:if>>활동중</option>
                <option value="2" <c:if test="${param.status == '2'}">selected</c:if>>정지</option>
            </select>
        </div>
    </div>
</form>

<table class="admin-table admin-list-table">
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
                <td>
                    <c:choose>
                        <c:when test="${user.status == '활동중'}">
                            <span class="status-badge available">${user.status}</span>
                        </c:when>
                        <c:when test="${user.status == '정지'}">
                            <span class="status-badge reserved">${user.status}</span>
                        </c:when>
                        <c:otherwise>
                            ${user.status}
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:if test="${user.status == '활동중'}">
                        <button type="button" class="btn danger" style="padding: 5px 10px; font-size: 0.9rem;"
                            onclick="openSuspendModal('${user.userId}', '${user.nickname}')">정지</button>
                    </c:if>
                    <c:if test="${user.status == '정지'}">
                        </c:if>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<div class="pagination-container d-flex justify-content-center" style="margin-top: 2rem;">
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
            <div class="form-group"> <label for="reason">정지 사유</label>
                <input type="text" name="reason" id="reason" required>
            </div>
            <div class="form-group">
                <label for="duration">정지 기간 (일)</label>
                <input type="number" name="duration" id="duration" value="7" required>
            </div>
            <div class="button-container" style="margin-top: 20px;">
                <button type="submit" class="btn danger">정지 실행</button> 
            </div>
        </form>
    </div>
</div>

<script>
    const modal = document.getElementById('suspendModal');
    function openSuspendModal(userId, nickname) {
        document.getElementById('userIdToSuspend').value = userId;
        document.getElementById('nicknameToSuspend').innerText = nickname;
        modal.style.display = 'flex'; // 모달을 flex로 변경하여 중앙 정렬 (CSS에서 설정)
    }
    function closeSuspendModal() {
        modal.style.display = 'none';
    }
</script>