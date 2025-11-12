<%-- 파일 경로: /WEB-INF/views/content/admin/reporthistory.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<h1>신고 처리 내역</h1>

<div class="board-nav-tabs">
    <ul class="admin-tab-style">
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/report/list">대기 중인 신고</a>
        </li>
        <li class="nav-item">
            <a class="nav-link active" href="${pageContext.request.contextPath}/admin/report/history">처리 내역</a>
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
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${list}" var="dto">
            <tr>
                <td>${dto.reportId}</td>
                
                <%-- 신고된 게시글 링크 --%>
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
                    <c:choose>
                        <c:when test="${dto.reportStatus == 'APPROVED'}">
                            <span class="status-badge approved">승인(숨김)</span>
                        </c:when>
                        <c:when test="${dto.reportStatus == 'REJECTED'}">
                            <span class="status-badge rejected">반려</span>
                        </c:when>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty list}">
            <tr>
                <td colspan="7" style="text-align: center;">처리된 신고 내역이 없습니다.</td>
            </tr>
        </c:if>
    </tbody>
</table>