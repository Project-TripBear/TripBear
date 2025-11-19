<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/board-view.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/qna.css">

<div class="page-board-view-container">
    <div id="main">
        <div class="post-container">
            <div class="post-header">
                <span class="category">Q&A</span>
                <h2 class="subject">${dto.question_board_title}</h2>

                <div class="post-meta">
                    <span>작성자: <strong>${dto.nickname}</strong></span>
                    <span>|</span>
                    <span>작성일: <fmt:formatDate value="${dto.question_board_regdate}" pattern="yyyy-MM-dd HH:mm"/></span>
                    <span>|</span>
                    <span>조회수: ${dto.question_board_view_count}</span>
                </div>
            </div>

            <div class="post-content">
                <c:out value="${dto.question_board_content}" escapeXml="false" />
            </div>

            <div class="post-actions action-buttons-group">
                <sec:authorize access="hasAuthority('ACTIVE')">
                    <a href="<c:url value='/qnaboard/like?seq=${dto.question_board_id}'/>"
                       class="btn like ${dto.liked ? 'active' : ''}">
                        <i class="fa-solid ${dto.liked ? 'fa-heart' : 'fa-regular fa-heart'}"></i>
                        좋아요 (${dto.likeCount})
                    </a>

                    <a href="<c:url value='/qnaboard/scrap?seq=${dto.question_board_id}'/>"
                       class="btn scrap ${dto.scrapped ? 'active' : ''}">
                        <i class="fa-solid ${dto.scrapped ? 'fa-bookmark' : 'fa-regular fa-bookmark'}"></i>
                        스크랩 (${dto.scrapCount})
                    </a>
                </sec:authorize>

                <sec:authorize access="!hasAuthority('ACTIVE')">
                    <span class="post-actions-helper">로그인하면 좋아요/스크랩을 할 수 있습니다.</span>
                </sec:authorize>
            </div>

            <div class="bottom-buttons action-buttons-group">
                <a href="<c:url value='/qnaboard/list'/>" class="btn btn-secondary">목록</a>

                <c:set var="currentUserId" value="0"/>
                <sec:authorize access="isAuthenticated()">
                    <sec:authentication property="principal.udto.seq" var="currentUserId" />
                </sec:authorize>

                <div class="action-buttons-group">
                    <c:if test="${dto.user_id == currentUserId}">
                        <a href="<c:url value='/qnaboard/edit?seq=${dto.question_board_id}'/>" class="btn btn-primary">수정</a>

                        <form action="<c:url value='/qnaboard/delete'/>" method="GET" style="display:inline;"
                              onsubmit="return confirm('정말 삭제하시겠습니까?');">
                            <input type="hidden" name="seq" value="${dto.question_board_id}">
                            <sec:csrfInput />
                            <button type="submit" class="btn btn-danger">삭제</button>
                        </form>
                    </c:if>

                    <c:if test="${dto.user_id != currentUserId && currentUserId != null}">
                        <a href="<c:url value='/qnaboard/report?boardSeq=${dto.question_board_id}&reportedUserId=${dto.user_id}'/>"
                           class="btn btn-danger" target="_blank">신고</a>
                    </c:if>
                </div>
            </div>

            <div class="comment-section">
                <h3>댓글 (${commentList.size()})</h3>

                <table class="comment-list-table">
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty commentList}">
                            <c:forEach var="comment" items="${commentList}">
                                <tr class="comment-row" id="comment-row-${comment.question_answer_id}">
                                    <td class="commentContent">
                                        <div id="comment-content-${comment.question_answer_id}">${comment.question_answer_content}</div>
                                        <div><fmt:formatDate value="${comment.question_answer_regdate}" pattern="MM-dd HH:mm"/></div>
                                    </td>
                                    <td class="commentInfo">
                                        <div>
                                            <div>${comment.nickname}</div>
                                            <c:if test="${comment.user_id == currentUserId}">
                                                <div class="comment-actions">
                                                    <span class="comment-edit" onclick="toggleCommentEdit(${comment.question_answer_id})">수정</span>
                                                    <span class="comment-delete" onclick="deleteComment(${comment.question_answer_id}, ${dto.question_board_id})">삭제</span>
                                                </div>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                                <tr class="commentEditRow" id="edit-row-${comment.question_answer_id}" style="display:none;">
                                    <td colspan="2">
                                        <textarea class="comment-edit-area" id="edit-text-${comment.question_answer_id}">${comment.question_answer_content}</textarea>
                                        <div class="comment-edit-actions">
                                            <button type="button" class="btn btn-primary" onclick="updateComment(${comment.question_answer_id})">확인</button>
                                            <button type="button" class="btn btn-secondary" onclick="cancelEdit(${comment.question_answer_id})">닫기</button>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="2" class="comment-empty">등록된 댓글이 없습니다 😶</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>

                <sec:authorize access="hasAuthority('ACTIVE')">
                    <form action="<c:url value='/qnaboard/addcomment'/>" method="POST" class="comment-add-form">
                        <sec:csrfInput />
                        <input type="hidden" name="question_board_id" value="${dto.question_board_id}">
                        <textarea name="question_answer_content" rows="3" placeholder="댓글을 입력하세요" required></textarea>

                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">댓글 등록</button>
                        </div>
                    </form>
                </sec:authorize>
            </div>
        </div>
    </div>
</div>

<script>
const contextPath = "${pageContext.request.contextPath}";

function toggleCommentEdit(id) {
    const editRow = document.getElementById("edit-row-" + id);
    if (editRow) {
        editRow.style.display = editRow.style.display === 'none' ? '' : 'none';
    }
}

function cancelEdit(id) {
    const editRow = document.getElementById("edit-row-" + id);
    if (editRow) {
        editRow.style.display = 'none';
    }
}

function updateComment(id) {
    const content = document.getElementById("edit-text-" + id).value;

    fetch(contextPath + '/qnaboard/editcomment', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: "question_answer_id=" + id +
              "&question_answer_content=" + encodeURIComponent(content)
    })
        .then(r => r.text())
        .then(() => {
            document.getElementById("comment-content-" + id).innerText = content;
            cancelEdit(id);
        });
}

function deleteComment(commentId, boardSeq) {
    if (!confirm("정말 삭제할까요?")) return;

    location.href = contextPath + "/qnaboard/deletecomment?commentId=" + commentId + "&boardSeq=" + boardSeq;
}
</script>
