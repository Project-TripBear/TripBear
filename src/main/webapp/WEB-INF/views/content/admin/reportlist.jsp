<%-- 파일 경로: /WEB-INF/views/content/admin/reportlist.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
<br> <table class="admin-table admin-list-table">
    <thead>
        <tr>
            <th>신고 번호</th>
            <th>신고된 게시글</th>
            <th>신고자</th>
            <th>신고 대상</th>
            <th>신고 사유</th>
            <th>신고일</th>
            <th>처리 상태</th>
            <th>액션</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${list}" var="dto">
            <tr>
                <td>${dto.reportId}</td>
                
                <td>
                    <c:choose>
                        <c:when test="${dto.reportTargetType == 'findboard'}">
                            <a href="${pageContext.request.contextPath}/findboard/view?id=${dto.reportTargetId}" target="_blank">
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
                    <div class="report-actions">
                        <a href="${pageContext.request.contextPath}/admin/report/view?reportId=${dto.reportId}" class="btn secondary">상세</a>
                        </div>
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