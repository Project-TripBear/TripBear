	<%-- 파일 경로: /WEB-INF/views/content/admin/reportlist.jsp --%>
	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
	<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
	<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
	
	<h1>신고 관리</h1>
	
	<div class="board-nav-tabs">
	    <ul class="admin-tab-style">
	        <li class="nav-item">
	            <a class="nav-link active" href="${pageContext.request.contextPath}/admin/report/list">대기 중인 신고</a>
	        </li>
	        <li class="nav-item">
	            <a class="nav-link" href="${pageContext.request.contextPath}/admin/report/history">처리 내역</a>
	        </li>
	    </ul>
	</div>
	<br>
	<table class="admin-table admin-list-table">
	    <thead>
	        <tr>
	            <th>신고 번호</th>
	            <th>신고된 게시글</th>
	            <th>신고자</th>
	            <th>신고 대상</th>
	            <th>신고 사유</th>
	            <th>신고일</th>
	            <th>처리 상태</th>
	            <th>관리</th>
	        </tr>
	    </thead>
	    <tbody>
	        <c:forEach items="${list}" var="dto">
	            
	            <%-- ★★★ JSTL: 게시글 제목 이스케이프 로직 분리 (컴파일 오류 방지) ★★★ --%>
	            <c:set var="escapedTitle" value="${fn:replace(dto.postTitle, \"'\", \"\\'\")}" />
	
	            <tr>
	                <td>${dto.reportId}</td>
	                
	                <td>
	                    <c:choose>
	                        <c:when test="${dto.reportTargetType == 'findboard'}">
	                            <a href="${pageContext.request.contextPath}/findboard/view?seq=${dto.reportTargetId}" target="_blank">
	                            ${dto.postTitle}
	                            </a>
	                        </c:when>
	                        <c:when test="${dto.reportTargetType == 'question'}">
	                            <a href="${pageContext.request.contextPath}/question/view?id=${dto.reportTargetId}" target="_blank">
	                                ${dto.postTitle}
	                            </a>
	                        </c:when>
	                        <c:when test="${dto.reportTargetType == 'review'}">
	                            <a href="${pageContext.request.contextPath}/review/view?id=${dto.reportTargetId}" target="_blank">
	                                ${dto.postTitle}
	                            </a>
	                        </c:when>
	                        <c:when test="${dto.reportTargetType == 'hotdeal'}">
	                            <a href="${pageContext.request.contextPath}/hotdeal/view?id=${dto.reportTargetId}" target="_blank">
	                                ${dto.postTitle}
	                            </a>
	                        </c:when>
	                        <c:otherwise>
	                            ${dto.postTitle}
	                        </c:otherwise>
	                    </c:choose>
	                </td>
	                
	                <td>${dto.reporterNickname}</td>
	                <td>${dto.reportedUserNickname}</td>
	                <td>${dto.reportReasonType}</td>
	                <td><fmt:formatDate value="${dto.reportRegdate}" pattern="yyyy-MM-dd"/></td>
	                
	                <td>
	                    <span class="status-badge pending">대기 중</span>
	                </td>
	                
	                <td> 
	                    <button type="button" class="btn secondary" style="padding: 5px 10px; font-size: 0.9rem;"
	                        onclick="openProcessModal('${dto.reportId}', 'HIDE', '${escapedTitle}')">숨김</button>
	                    
	                    <button type="button" class="btn danger" style="padding: 5px 10px; font-size: 0.9rem;"
	                        onclick="openProcessModal('${dto.reportId}', 'REJECT', '${escapedTitle}')">반려</button>
	                </td>
	            </tr>
	        </c:forEach>
	        <c:if test="${empty list}">
	            <tr>
	                <td colspan="8" style="text-align: center;">대기 중인 신고 내역이 없습니다.</td>
	            </tr>
	        </c:if>
	    </tbody>
	</table>
	
	<%-- 신고 처리 모달 --%>
	<div id="processModal" class="modal">
	    <div class="modal-content">
	        <span class="close-button" onclick="closeProcessModal()">&times;</span>
	        <h2 id="modalTitle">신고 처리</h2>
	        
	        <form id="processForm" method="POST" action="${pageContext.request.contextPath}/admin/report/process">
	            
	            <%-- CSRF 토큰 --%>
	            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	            
	            <%-- 필수 정보: 신고 ID와 처리 유형(HIDE/REJECT) --%>
	            <input type="hidden" name="reportId" id="reportIdToProcess">
	            <input type="hidden" name="action" id="processType">
	            
	            <p>게시글: <strong id="postTitleToProcess" style="color: #007bff;"></strong></p>
	            <p><strong id="processTypeText"></strong> 처리를 진행하시겠습니까?</p>
	            
	            <div class="form-group">
	                <label for="processReason">처리 사유 (선택)</label>
	                <input type="text" name="processReason" id="processReason" placeholder="반려 또는 숨김 사유를 입력하세요.">
	            </div>
	            
	            <div class="button-container" style="margin-top: 20px;">
	                <button type="submit" id="modalSubmitButton" class="btn">처리 실행</button> 
	            </div>
	        </form>
	    </div>
	</div>
	
	<%-- 스크립트 (JS 오류 수정 완료) --%>
	<script>
	function openProcessModal(reportId, type, postTitle) {
	    
	    // 1. 함수가 호출될 때 요소를 찾음
	    const modal = document.getElementById('processModal');
	    const modalTitle = document.getElementById('modalTitle');
	    const modalSubmitButton = document.getElementById('modalSubmitButton');
	    const reportIdInput = document.getElementById('reportIdToProcess');
	    const processTypeInput = document.getElementById('processType');
	    const postTitleText = document.getElementById('postTitleToProcess');
	    const processTypeText = document.getElementById('processTypeText');
	
	    // 2. 폼 데이터 설정
	    reportIdInput.value = reportId;
	    processTypeInput.value = type;
	    postTitleText.innerText = postTitle;
	
	    // 3. 모달 UI 변경
	    if (type === 'HIDE') {
	        modalTitle.innerText = '게시글 숨김 처리';
	        processTypeText.innerText = '[숨김]';
	        modalSubmitButton.className = 'btn secondary';
	        modalSubmitButton.innerText = '숨김 실행';
	    } else if (type === 'REJECT') {
	        modalTitle.innerText = '신고 반려 처리';
	        processTypeText.innerText = '[반려]';
	        modalSubmitButton.className = 'btn danger';
	        modalSubmitButton.innerText = '반려 실행';
	    }
	    
	    // 4. 모달 표시
	    modal.style.display = 'flex';
	}
	
	function closeProcessModal() {
	    // 1. 함수가 호출될 때 요소를 찾음
	    const modal = document.getElementById('processModal');
	    modal.style.display = 'none';
	    
	    // 2. 폼 초기화
	    document.getElementById('processReason').value = '';
	}
	</script>