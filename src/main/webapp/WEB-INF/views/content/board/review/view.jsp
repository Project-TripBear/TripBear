<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>${review.reviewBoardTitle}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/board-view.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/review.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
</head>
<body>
<div class="review-page">
	<div class="page-board-view-container">
	    <div id="main">
	        <div class="post-container">
	            <div class="post-header">
	                <span class="category">여행 후기</span>
	                <h2 class="subject">${review.reviewBoardTitle}</h2>
	                <div class="post-meta">
	                    <span>작성자: <strong>${review.nickname}</strong></span>
	                    <span>|</span>
	                    <span>작성일: ${review.reviewBoardRegdate}</span>
	                    <span>|</span>
	                    <span>조회수: ${review.reviewBoardCount}</span>
	                </div>
	            </div>
	
	            <div class="post-content">${review.reviewBoardContent}</div>
	
	            <c:if test="${not empty images}">
	                <div class="post-images review-gallery">
	                    <span class="image-nav-arrow left">&#10094;</span>
	                    <div class="image-area">
	                        <c:forEach var="img" items="${images}">
	                            <img class="review-img"
	                                 src="${pageContext.request.contextPath}/upload/review/${img.reviewImageUrl}"
	                                 alt="게시글 이미지">
	                        </c:forEach>
	                    </div>
	                    <span class="image-nav-arrow right">&#10095;</span>
	                </div>
	            </c:if>
	
	            <div class="post-actions action-buttons-group">
	                <button type="button" class="btn like" id="btn-like" data-id="${review.reviewPostId}">🤍 추천</button>
	                <button type="button" class="btn scrap" id="btn-scrap" data-id="${review.reviewPostId}">📁 스크랩</button>
	            </div>
	
	            <div class="comment-section">
	                <h3>댓글 <span id="comment-count">(0)</span></h3>
	                <table id="comment" class="comment-list-table">
	                    <tbody>
	                    <tr>
	                        <td class="comment-empty" colspan="2">등록된 댓글이 없습니다 😶</td>
	                    </tr>
	                    </tbody>
	                </table>
	            </div>
	
	            <c:if test="${not empty userId}">
	                <div class="comment-add-form">
	                    <textarea id="comment-content" placeholder="댓글을 입력하세요" rows="3"></textarea>
	                    <div class="form-actions">
	                        <button type="button" class="btn btn-primary" id="btn-comment-add">댓글 등록</button>
	                    </div>
	                </div>
	            </c:if>
	
	            <div class="bottom-buttons action-buttons-group">
	                <a href="${pageContext.request.contextPath}/review/list" class="btn btn-secondary">목록</a>
	                <div class="action-buttons-group">
	                    <c:if test="${userId eq review.userId}">
	                        <a href="${pageContext.request.contextPath}/review/edit/${review.reviewPostId}" class="btn btn-primary">수정</a>
	                        <a href="${pageContext.request.contextPath}/review/del/${review.reviewPostId}"
	                           class="btn btn-danger"
	                           onclick="return confirm('정말 삭제하시겠습니까?');">삭제</a>
	                    </c:if>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
</div>
<script>
const reviewId = "${review.reviewPostId}";
const userId = "${userId}";
const contextPath = "${pageContext.request.contextPath}";
const token = "${_csrf.token}";
const header = "${_csrf.headerName}";

const $commentBody = () => $("#comment tbody");

function renderEmptyRow() {
    return '<tr><td class="comment-empty" colspan="2">등록된 댓글이 없습니다 😶</td></tr>';
}

function buildCommentRow(c) {
    let actions = '';
    if (String(userId) === String(c.userId) && userId) {
        actions = `
            <div class="comment-actions">
                <span class="comment-edit" data-id="${c.reviewCommentId}">수정</span>
                <span class="comment-delete" data-id="${c.reviewCommentId}">삭제</span>
            </div>`;
    }

    return `
        <tr class="comment-row" id="comment-row-${c.reviewCommentId}">
            <td class="commentContent">
                <div>${c.reviewCommentContent}</div>
                <div>${c.reviewCommentRegdate}</div>
            </td>
            <td class="commentInfo">
                <div>
                    <div>${c.nickname}</div>
                    ${actions}
                </div>
            </td>
        </tr>`;
}

function loadComments() {
    $.getJSON(contextPath + "/api/review/comment/list/" + reviewId, function(list) {
        const $tbody = $commentBody();
        $tbody.empty();
        $('.commentEditRow').remove();

        if (!list || list.length === 0) {
            $tbody.append(renderEmptyRow());
        } else {
            list.forEach(function(c) {
                $tbody.append(buildCommentRow(c));
            });
        }
        $("#comment-count").text("(" + (list ? list.length : 0) + ")");
    });
}

function appendEditRow(commentId, original) {
    const $row = $('#comment-row-' + commentId);
    if (!$row.length) return;

    $('.commentEditRow').remove();

    const editRowHtml = `
        <tr class="commentEditRow">
            <td colspan="2">
                <textarea class="comment-edit-area">${original}</textarea>
                <div class="comment-edit-actions">
                    <button type="button" class="btn btn-primary comment-edit-save" data-id="${commentId}">확인</button>
                    <button type="button" class="btn btn-secondary comment-edit-cancel">닫기</button>
                </div>
            </td>
        </tr>`;

    $row.after(editRowHtml);
}

$('#btn-comment-add').click(function() {
    if (!userId) {
        alert('로그인 후 댓글을 작성할 수 있습니다.');
        return;
    }

    const content = $("#comment-content").val().trim();
    if (!content) {
        alert('댓글 내용을 입력하세요.');
        return;
    }

    $.ajax({
        url: contextPath + "/api/review/comment/add",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({ reviewPostId: reviewId, userId: userId, reviewCommentContent: content }),
        beforeSend: function(xhr) { xhr.setRequestHeader(header, token); },
        success: function(res) {
            if (res > 0) {
                $("#comment-content").val('');
                loadComments();
            } else {
                alert('댓글 등록 실패 ❌');
            }
        }
    });
});

$(document).on('click', '.comment-delete', function() {
    if (!confirm('댓글을 삭제하시겠습니까?')) {
        return;
    }

    const commentId = $(this).data('id');
    $.ajax({
        url: contextPath + "/api/review/comment/del/" + commentId,
        type: "DELETE",
        beforeSend: function(xhr) { xhr.setRequestHeader(header, token); },
        success: function(res) {
            if (res > 0) {
                loadComments();
            } else {
                alert('댓글 삭제 실패 ❌');
            }
        }
    });
});

$(document).on('click', '.comment-edit', function() {
    const commentId = $(this).data('id');
    const $row = $('#comment-row-' + commentId);
    if (!$row.length) return;

    const original = $row.find('.commentContent div').first().text().trim();
    appendEditRow(commentId, original.replace(/"/g, '&quot;'));
});

$(document).on('click', '.comment-edit-save', function() {
    const commentId = $(this).data('id');
    const $editRow = $(this).closest('.commentEditRow');
    const newContent = $editRow.find('.comment-edit-area').val().trim();

    if (!newContent) {
        alert('내용을 입력하세요.');
        return;
    }

    $.ajax({
        url: contextPath + "/api/review/comment/edit",
        type: "PUT",
        contentType: "application/json",
        data: JSON.stringify({ reviewCommentId: commentId, reviewCommentContent: newContent }),
        beforeSend: function(xhr) { xhr.setRequestHeader(header, token); },
        success: function(res) {
            if (res > 0) {
                loadComments();
            } else {
                alert('댓글 수정 실패 ❌');
            }
        }
    });
});

$(document).on('click', '.comment-edit-cancel', function() {
    $(this).closest('.commentEditRow').remove();
});

$('#btn-like').click(function() {
    if (!userId) {
        alert('로그인 후 이용할 수 있습니다.');
        return;
    }
    $.ajax({
        url: contextPath + "/api/review/like/toggle",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({ reviewPostId: reviewId, userId: userId }),
        beforeSend: function(xhr) { xhr.setRequestHeader(header, token); },
        success: function(res) {
            if (res === true) {
                $("#btn-like").addClass("active").text("❤️ 추천됨");
            } else {
                $("#btn-like").removeClass("active").text("🤍 추천");
            }
        }
    });
});

$('#btn-scrap').click(function() {
    if (!userId) {
        alert('로그인 후 이용할 수 있습니다.');
        return;
    }
    $.ajax({
        url: contextPath + "/api/review/scrap/toggle",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({ reviewPostId: reviewId, userId: userId }),
        beforeSend: function(xhr) { xhr.setRequestHeader(header, token); },
        success: function(res) {
            if (res === true) {
                $("#btn-scrap").addClass("active").text("✅ 스크랩됨");
            } else {
                $("#btn-scrap").removeClass("active").text("📁 스크랩");
            }
        }
    });
});

function loadLikeScrapStatus() {
    if (!userId) return;

    $.get(contextPath + "/api/review/like/status", { reviewPostId: reviewId, userId: userId }, function(res) {
        const liked = (res === true || res === "true" || res === 1);
        if (liked) {
            $("#btn-like").addClass("active").text("❤️ 추천됨");
        } else {
            $("#btn-like").removeClass("active").text("🤍 추천");
        }
    });

    $.get(contextPath + "/api/review/scrap/status", { reviewPostId: reviewId, userId: userId }, function(res) {
        const scrapped = (res === true || res === "true" || res === 1);
        if (scrapped) {
            $("#btn-scrap").addClass("active").text("✅ 스크랩됨");
        } else {
            $("#btn-scrap").removeClass("active").text("📁 스크랩");
        }
    });
}

$(function() {
    $.ajaxSetup({
        beforeSend: function(xhr) {
            if (token && header) {
                xhr.setRequestHeader(header, token);
            }
        }
    });

    loadLikeScrapStatus();
    loadComments();
});
</script>
</body>
</html>
