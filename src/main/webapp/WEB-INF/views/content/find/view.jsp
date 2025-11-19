<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/board-view.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/findboard.css">

<fmt:parseDate value="${dto.find_board_regdate}" var="regDateObj" pattern="yyyy-MM-dd HH:mm:ss"/>
<c:set var="currentUserId" value="0"/>
<sec:authorize access="hasAuthority('ACTIVE')">
    <sec:authentication property="principal.udto.seq" var="currentUserId" />
</sec:authorize>

<main class="page-board-view-container">
    <div id="main">
        <div class="post-container">
            <div class="post-header">
                <span class="category">동행 찾기</span>
                <h2 class="subject">${dto.find_board_title}</h2>
                <div class="post-meta">
                    <span>작성자: <strong>${dto.nickname}</strong></span>
                    <span>|</span>
                    <span>작성일: <fmt:formatDate value="${regDateObj}" pattern="yyyy-MM-dd HH:mm" /></span>
                    <span>|</span>
                    <span>조회수: ${dto.find_board_view_count}</span>
                </div>
            </div>

            <div class="post-content">
                <c:out value="${dto.find_board_content}" escapeXml="false" />
            </div>

            <div class="post-actions action-buttons-group">
                <sec:authorize access="hasAuthority('ACTIVE')">
                    <a href="<c:url value="/findboard/like?seq=${dto.find_board_id}"/>"
                       class="btn like ${dto.liked ? 'active' : ''}">
                        <i class="fa-solid ${dto.liked ? 'fa-heart' : 'fa-regular fa-heart'}"></i> 좋아요 (${dto.likeCount})
                    </a>
                    <a href="<c:url value="/findboard/scrap?seq=${dto.find_board_id}"/>"
                       class="btn scrap ${dto.scrapped ? 'active' : ''}">
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
                            <c:forEach items="${commentList}" var="comment">
                                <fmt:parseDate value="${comment.find_comment_regdate}" var="commentRegDateObj" pattern="yyyy-MM-dd HH:mm:ss"/>
                                <tr class="comment-row" id="comment-row-${comment.find_comment_id}">
                                    <td class="commentContent">
                                        <div id="comment-content-${comment.find_comment_id}">${comment.find_comment_content}</div>
                                        <div><fmt:formatDate value="${commentRegDateObj}" pattern="MM-dd HH:mm"/></div>
                                    </td>
                                    <td class="commentInfo">
                                        <div>
                                            <div>${comment.nickname}</div>
                                            <c:if test="${comment.user_id == currentUserId}">
                                                <div class="comment-actions">
                                                    <a href="#" onclick="return toggleCommentEdit(${comment.find_comment_id});">수정</a>
                                                    <span>|</span>
                                                    <a href="<c:url value="/findboard/deletecomment?commentId=${comment.find_comment_id}&boardSeq=${dto.find_board_id}"/>"
                                                       onclick="return confirm('댓글을 삭제하시겠습니까?');">삭제</a>
                                                </div>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                                <c:if test="${comment.user_id == currentUserId}">
                                    <tr class="commentEditRow" id="edit-row-${comment.find_comment_id}" style="display:none;">
                                        <td colspan="2">
                                            <form action="<c:url value="/findboard/editcomment"/>" method="POST">
                                                <sec:csrfInput />
                                                <input type="hidden" name="find_comment_id" value="${comment.find_comment_id}">
                                                <input type="hidden" name="boardSeq" value="${dto.find_board_id}">
                                                <textarea name="find_comment_content" class="comment-edit-area" rows="3" required>${comment.find_comment_content}</textarea>
                                                <div class="comment-edit-actions">
                                                    <button type="submit" class="btn btn-primary">수정 완료</button>
                                                    <button type="button" class="btn btn-secondary" onclick="return toggleCommentEdit(${comment.find_comment_id});">취소</button>
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
                    <form action="<c:url value="/findboard/addcomment"/>" method="POST">
                        <sec:csrfInput />
                        <input type="hidden" name="find_board_id" value="${dto.find_board_id}">
                        <textarea name="find_comment_content" rows="3" required placeholder="댓글을 입력하세요"></textarea>
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
                <a href="<c:url value="/findboard/list"/>" class="btn btn-secondary">목록</a>
                <div class="action-buttons-group">
                    <c:if test="${dto.user_id == currentUserId}">
                        <a href="<c:url value="/findboard/edit?seq=${dto.find_board_id}"/>" class="btn btn-primary">수정</a>
                        <form action="<c:url value="/findboard/delete"/>" method="GET" style="display:inline;" onsubmit="return confirm('정말 삭제하시겠습니까?');">
                            <input type="hidden" name="seq" value="${dto.find_board_id}" />
                            <button type="submit" class="btn btn-danger">삭제</button>
                        </form>
                    </c:if>

                    <c:if test="${dto.user_id != currentUserId && currentUserId != 0}">
                        <c:url value="/findboard/report" var="reportUrl">
                            <c:param name="boardSeq" value="${dto.find_board_id}" />
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
</script>
