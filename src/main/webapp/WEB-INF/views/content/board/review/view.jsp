<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>${review.reviewBoardTitle}</title> <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/review.css">
  <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
  <style>
    .comment-item { border-bottom: 1px solid #ddd; padding: 10px 0; }
    .comment-actions button { font-size: 11px; margin-left: 5px; }
    .comment-edit-area { width: 100%; resize: none; margin-top: 5px; }
    .btn-action-area button.active { background: #ffebeb; }
  </style>
</head>

<body>

<div class="container">

  <div class="header-area">
    <h2>${review.reviewBoardTitle}</h2> <div class="post-info">
      작성자: ${review.nickname} |
      작성일: ${review.reviewBoardRegdate} | 조회수: ${review.reviewBoardCount} </div>
  </div>

  <div class="content-area">
    <p>${review.reviewBoardContent}</p> </div>

  <c:if test="${not empty images}">
    <div class="image-area-wrapper">
      <span class="image-nav-arrow left">&#10094;</span>
      <div class="image-area">
        <c:forEach var="img" items="${images}">
          <img class="review-img"
              src="${pageContext.request.contextPath}/upload/review/${img.reviewImageUrl}" alt="게시글 이미지">
        </c:forEach>
      </div>
      <span class="image-nav-arrow right">&#10095;</span>
    </div>
  </c:if>

  <div class="btn-action-area">
    <button id="btn-like" data-id="${review.reviewPostId}">🤍 추천</button>
    <button id="btn-scrap" data-id="${review.reviewPostId}">📁 스크랩</button>
  </div>

  <div class="btn-area">
    <a href="${pageContext.request.contextPath}/review/list" class="btn-list">목록</a>
    <c:if test="${userId eq review.userId}">
      <a href="${pageContext.request.contextPath}/review/edit/${review.reviewPostId}" class="btn-edit">수정</a>
      <a href="${pageContext.request.contextPath}/review/del/${review.reviewPostId}" class="btn-delete"
         onclick="return confirm('정말 삭제하시겠습니까?');">삭제</a>
    </c:if>
  </div>

  <div class="comment-section" style="margin-top:40px; border-top:1px solid #ccc; padding-top:20px;">
    <h3>댓글 💬 <span id="comment-count">(0)</span></h3>
    <div id="comment-list" style="margin-top:20px;"></div>

    <div class="comment-input" style="margin-top:25px;">
      <textarea id="comment-content" placeholder="댓글을 입력하세요" rows="3"
                style="width:100%; resize:none;"></textarea>
      <button id="btn-comment-add" style="margin-top:5px; float:right;">등록</button>
      <div style="clear:both;"></div>
    </div>
  </div>
</div>

<script>

// ✅ JS 변수명 수정
const reviewId = "${review.reviewPostId}";
const userId = "${userId}"; // Controller에서 model로 전달된 userId
const userName = "${userName}"; 
const contextPath = "${pageContext.request.contextPath}";

// ✅ 댓글 목록 불러오기
function loadComments() {
  // ✅ API URL 수정
  $.getJSON(contextPath + "/api/review/comment/list/" + reviewId, function(list) {
    let html = "";
    if (list.length === 0) {
      html = "<p>등록된 댓글이 없습니다 😶</p>";
    } else {
      list.forEach(c => {
        // ✅ DTO 변수명 수정
        html += `
          <div class="comment-item" data-id="${c.reviewCommentId}">
            <b>${c.nickname}</b>
            <small style="color:#999;">${c.reviewCommentRegdate}</small>
            <div class="comment-content">${c.reviewCommentContent}</div>
            <div class="comment-actions">`;
        
        // userId는 JSP 변수가 아닌 스크립트 상단의 const userId 사용
        if (userId == c.userId) { // (String과 Number 비교일 수 있으므로 == 사용)
          html += `
            <button class="btn-edit">수정</button>
            <button class="btn-delete">삭제</button>`;
        }
        html += `</div></div>`;
      });
    }
    $("#comment-list").html(html);
    $("#comment-count").text("(" + list.length + ")");
  });
}

// ✅ 댓글 등록
$("#btn-comment-add").click(function() {
  if (!userId) return alert("로그인 후 댓글을 작성할 수 있습니다.");
  const content = $("#comment-content").val().trim();
  if (content === "") return alert("댓글 내용을 입력하세요.");

  // ✅ DTO 필드명에 맞게 key 수정
  const data = { 
    reviewPostId: reviewId, 
    userId: userId, 
    reviewCommentContent: content 
  };

  $.ajax({
    url: contextPath + "/api/review/comment/add", // ✅ API URL 수정
    type: "POST",
    contentType: "application/json",
    data: JSON.stringify(data),
    success: function(res) {
      if (res > 0) {
        $("#comment-content").val("");
        loadComments();
      } else alert("댓글 등록 실패 ❌");
    }
  });
});

// ✅ 댓글 삭제
$(document).on("click", ".btn-delete", function() {
  const commentId = $(this).closest(".comment-item").data("id");
  $.ajax({
    url: contextPath + "/api/review/comment/del/" + commentId, // ✅ API URL 수정
    type: "DELETE",
    success: function(res) {
      if (res > 0) loadComments();
      else alert("댓글 삭제 실패 ❌");
    }
  });
});

// ✅ 댓글 수정 모드 (내용 동일)
$(document).on("click", ".btn-edit", function() {
  const $comment = $(this).closest(".comment-item");
  const $content = $comment.find(".comment-content");
  const original = $content.text().trim();
  if ($comment.find("textarea").length > 0) return;
  const editBox = `
    <textarea class="comment-edit-area">${original}</textarea>
    <button class="btn-save">저장</button>
    <button class="btn-cancel">취소</button>
  `;
  $content.hide();
  $content.after(editBox);
});

// ✅ 댓글 수정 저장
$(document).on("click", ".btn-save", function() {
  const $comment = $(this).closest(".comment-item");
  const commentId = $comment.data("id");
  const newContent = $comment.find(".comment-edit-area").val().trim();
  if (newContent === "") return alert("내용을 입력하세요.");

  $.ajax({
    url: contextPath + "/api/review/comment/edit", // ✅ API URL 수정
    type: "PUT",
    contentType: "application/json",
    // ✅ DTO 필드명에 맞게 key 수정
    data: JSON.stringify({ 
      reviewCommentId: commentId, 
      reviewCommentContent: newContent 
    }),
    success: function(res) {
      if (res > 0) loadComments();
      else alert("댓글 수정 실패 ❌");
    }
  });
});

// ✅ 댓글 수정 취소 (내용 동일)
$(document).on("click", ".btn-cancel", function() {
  const $comment = $(this).closest(".comment-item");
  $comment.find(".comment-edit-area, .btn-save, .btn-cancel").remove();
  $comment.find(".comment-content").show();
});

// ✅ 좋아요
$("#btn-like").click(function() {
    if (!userId) return alert("로그인 후 이용할 수 있습니다.");
    $.ajax({
        url: contextPath + "/api/review/like/toggle", // ✅ API URL 수정
        type: "POST",
        contentType: "application/json",
        // ✅ 전송 데이터 수정
        data: JSON.stringify({ reviewPostId: reviewId, userId: userId }), 
        success: function(res) {
            if (res === true) {
                $("#btn-like").addClass("active").text("❤️ 추천됨");
            } else {
                $("#btn-like").removeClass("active").text("🤍 추천");
            }
        }
    });
});

// ✅ 스크랩
$("#btn-scrap").click(function() {
    if (!userId) return alert("로그인 후 이용할 수 있습니다.");
    $.ajax({
        url: contextPath + "/api/review/scrap/toggle", // ✅ API URL 수정
        type: "POST",
        contentType: "application/json",
        // ✅ 전송 데이터 수정
        data: JSON.stringify({ reviewPostId: reviewId, userId: userId }),
        success: function(res) {
            if (res === true) {
                $("#btn-scrap").addClass("active").text("✅ 스크랩됨");
            } else {
                $("#btn-scrap").removeClass("active").text("📁 스크랩");
            }
        }
    });
});

//✅ 초기 로드
$(function() {
  // CSRF (보안) 헤더 설정 (RoutePost와 동일)
  const token = "${_csrf.token}";
  const header = "${_csrf.headerName}";
  if (token) { // 토큰이 있을 때만
    $(document).ajaxSend(function(e, xhr) {
      xhr.setRequestHeader(header, token);
    });
  }
  
  loadLikeScrapStatus();
  loadComments();
});

function loadLikeScrapStatus() {
    if (!userId || userId === "") return;

    // ❤️ 좋아요 상태 로드
    $.get(contextPath + "/api/review/like/status", // ✅ API URL 수정
        { reviewPostId: reviewId, userId: userId }, // ✅ 파라미터 수정
        function(res) {
            const liked = (res === true || res === "true" || res === 1);
            if (liked) {
                $("#btn-like").addClass("active").text("❤️ 추천됨");
            } else {
                $("#btn-like").removeClass("active").text("🤍 추천");
            }
        }
    );

    // 📁 스크랩 상태 로드
    $.get(contextPath + "/api/review/scrap/status", // ✅ API URL 수정
        { reviewPostId: reviewId, userId: userId }, // ✅ 파라미터 수정
        function(res) {
            const scrapped = (res === true || res === "true" || res === 1);
            if (scrapped) {
                $("#btn-scrap").addClass("active").text("✅ 스크랩됨");
            } else {
                $("#btn-scrap").removeClass("active").text("📁 스크랩");
            }
        }
    );
}

</script>

</body>
</html>