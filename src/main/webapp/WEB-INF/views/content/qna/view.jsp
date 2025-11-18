<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:csrfMetaTags />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/board-view.css">

<fmt:parseDate value="${dto.question_board_regdate}" var="regDateObj" pattern="EEE MMM dd HH:mm:ss zzz yyyy" parseLocale="en"/>
<c:set var="currentUserId" value="0"/>
<sec:authorize access="hasAuthority('ACTIVE')">
    <sec:authentication property="principal.udto.seq" var="currentUserId" />
</sec:authorize>

<main class="page-board-view-container">
    <div id="main">
        <div class="post-container">
            <div class="post-header">
                <span class="category">Q&A</span>
                <h2 class="subject">${dto.question_board_title}</h2>
                <div class="post-meta">
                    <span>작성자: <strong>${dto.nickname}</strong></span>
                    <span>|</span>
                    <span>작성일: <fmt:formatDate value="${regDateObj}" pattern="yyyy-MM-dd HH:mm" /></span>
                    <span>|</span>
                    <span>조회수: ${dto.question_board_view_count}</span>
                </div>
            </div>

            <div class="post-content">
                <c:out value="${dto.question_board_content}" escapeXml="false" />
            </div>

            <div class="post-actions action-buttons-group">
                <sec:authorize access="hasAuthority('ACTIVE')">
                    <a href="<c:url value="/qnaboard/like?seq=${dto.question_board_id}"/>"
                       class="btn ${dto.liked ? 'active' : ''}">
                        <i class="fa-solid ${dto.liked ? 'fa-heart' : 'fa-regular fa-heart'}"></i> 좋아요 (${dto.likeCount})
                    </a>
                    <a href="<c:url value="/qnaboard/scrap?seq=${dto.question_board_id}"/>"
                       class="btn ${dto.scrapped ? 'active' : ''}">
                        <i class="fa-solid ${dto.scrapped ? 'fa-bookmark' : 'fa-regular fa-bookmark'}"></i> 스크랩 (${dto.scrapCount})
                    </a>
                </sec:authorize>
                <sec:authorize access="!hasAuthority('ACTIVE')">
                    <span class="action-helper-text">로그인하면 좋아요/스크랩을 할 수 있습니다.</span>
                </sec:authorize>
            </div>

            <div class="comment-section">
                <h3>댓글 (${commentList.size()})</h3>
                <table class="comment-list-table">
                    <tbody>
                    <c:choose>
                        <c:when test="${empty commentList}">
                            <tr>
                                <td class="comment-empty" colspan="2">등록된 댓글이 없습니다.</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="comment" items="${commentList}">
                                <fmt:parseDate value="${comment.question_answer_regdate}" var="commentDate" pattern="EEE MMM dd HH:mm:ss zzz yyyy" parseLocale="en"/>
                                <tr class="comment-row" id="comment-row-${comment.question_answer_id}">
                                    <td class="commentContent">
                                        <div id="comment-content-${comment.question_answer_id}">${comment.question_answer_content}</div>
                                        <div><fmt:formatDate value="${commentDate}" pattern="MM-dd HH:mm"/></div>
                                    </td>
                                    <td class="commentInfo">
                                        <div>
                                            <div>${comment.nickname}</div>
                                            <c:if test="${comment.user_id == currentUserId}">
                                                <div class="comment-actions">
                                                    <a href="#" onclick="return toggleCommentEdit(${comment.question_answer_id});">수정</a>
                                                    <span>|</span>
                                                    <a href="#" class="comment-inline-action" data-comment-id="${comment.question_answer_id}" data-board-id="${dto.question_board_id}" onclick="return deleteCommentAjax(this);">삭제</a>
                                                </div>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                                <c:if test="${comment.user_id == currentUserId}">
                                    <tr class="commentEditRow" id="edit-row-${comment.question_answer_id}" style="display:none;">
                                        <td colspan="2">
                                            <form action="<c:url value="/qnaboard/editcomment"/>" method="POST" class="comment-edit-form" data-comment-id="${comment.question_answer_id}">
                                                <sec:csrfInput />
                                                <input type="hidden" name="question_answer_id" value="${comment.question_answer_id}">
                                                <input type="hidden" name="boardSeq" value="${dto.question_board_id}">
                                                <textarea name="question_answer_content" class="comment-edit-area" rows="3" required>${comment.question_answer_content}</textarea>
                                                <div class="comment-edit-actions">
                                                    <button type="submit" class="btn btn-primary">수정 완료</button>
                                                    <button type="button" class="btn btn-secondary" onclick="return toggleCommentEdit(${comment.question_answer_id});">취소</button>
                                                </div>
                                            </form>
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>

            <sec:authorize access="hasAuthority('ACTIVE')">
                <div class="comment-add-form">
                    <form action="<c:url value="/qnaboard/addcomment"/>" method="POST">
                        <sec:csrfInput />
                        <input type="hidden" name="question_board_id" value="${dto.question_board_id}">
                        <textarea name="question_answer_content" rows="3" required placeholder="댓글을 입력하세요"></textarea>
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">댓글 등록</button>
                        </div>
                    </form>
                </div>
            </sec:authorize>

            <sec:authorize access="!hasAuthority('ACTIVE')">
                <p class="action-helper-text" style="margin-top: 10px;">로그인 후 댓글을 작성할 수 있습니다.</p>
            </sec:authorize>

            <div class="bottom-buttons action-buttons-group">
                <a href="<c:url value="/qnaboard/list"/>" class="btn btn-secondary">목록</a>
                <div class="action-buttons-group">
                    <c:if test="${dto.user_id == currentUserId}">
                        <a href="<c:url value="/qnaboard/edit?seq=${dto.question_board_id}"/>" class="btn btn-primary">수정</a>
                        <form action="<c:url value="/qnaboard/delete"/>" method="GET" style="display:inline;" onsubmit="return confirm('정말 삭제하시겠습니까?');">
                            <input type="hidden" name="seq" value="${dto.question_board_id}" />
                            <button type="submit" class="btn btn-danger">삭제</button>
                        </form>
                    </c:if>

                    <c:if test="${dto.user_id != currentUserId && currentUserId != 0}">
                        <c:url value="/qnaboard/report" var="reportUrl">
                            <c:param name="boardSeq" value="${dto.question_board_id}" />
                            <c:param name="reportedUserId" value="${dto.user_id}" />
                        </c:url>
                        <a href="${reportUrl}" class="btn btn-danger"
                           onclick="window.open(this.href, 'reportPopup', 'width=500,height=600,scrollbars=yes'); return false;">신고</a>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</main>

<script>
    function toggleCommentEdit(commentId) {
        const contentDiv = document.getElementById('comment-content-' + commentId);
        const editRow = document.getElementById('edit-row-' + commentId);

        if (!contentDiv || !editRow) {
            return false;
        }

        if (editRow.style.display === 'none' || editRow.style.display === '') {
            contentDiv.style.display = 'none';
            editRow.style.display = 'table-row';
            const textarea = editRow.querySelector('textarea');
            if (textarea) {
                textarea.focus();
            }
        } else {
            contentDiv.style.display = 'block';
            editRow.style.display = 'none';
        }
        return false;
    }

    const contextPath = '${pageContext.request.contextPath}';
    const csrfHeaderName = document.querySelector('meta[name="_csrf_header"]')?.content;
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;

    /* 댓글 수정 - AJAX */
    document.querySelectorAll('.comment-edit-form').forEach(form => {
        form.addEventListener('submit', function (e) {
            e.preventDefault();

            const formData = new FormData(this);
            const commentId = this.dataset.commentId;
            const contentDiv = document.getElementById('comment-content-' + commentId);

            fetch(this.action, {
                method: 'POST',
                headers: csrfHeaderName && csrfToken ? { [csrfHeaderName]: csrfToken } : {},
                body: new URLSearchParams(formData)
            })
                .then(res => {
                    if (!res.ok) throw new Error('댓글 수정 실패');
                    return res.text();
                })
                .then(() => {
                    const newContent = formData.get('question_answer_content');
                    if (contentDiv) {
                        contentDiv.textContent = newContent;
                    }
                    toggleCommentEdit(commentId);
                })
                .catch(err => {
                    alert(err.message);
                });
        });
    });

    /* 댓글 삭제 - AJAX */
    function deleteCommentAjax(btn) {
        const commentId = btn.dataset.commentId;
        const boardId = btn.dataset.boardId;
        if (!commentId || !boardId) return false;

        if (!confirm('댓글을 삭제하시겠습니까?')) {
            return false;
        }

        const params = new URLSearchParams({ commentId, boardSeq: boardId });

        fetch(`${contextPath}/qnaboard/deletecomment?${params.toString()}`, {
            method: 'GET',
            headers: csrfHeaderName && csrfToken ? { [csrfHeaderName]: csrfToken } : {}
        })
            .then(res => {
                if (!res.ok) throw new Error('댓글 삭제 실패');
                return res.text();
            })
            .then(() => {
                const row = document.getElementById('comment-row-' + commentId);
                const editRow = document.getElementById('edit-row-' + commentId);
                if (row) row.remove();
                if (editRow) editRow.remove();
            })
            .catch(err => alert(err.message));

        return false;
    }
</script>
