<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>${post.routepostTitle}</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/routepost.css">
  <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
  <style>
    .comment-item { border-bottom: 1px solid #ddd; padding: 10px 0; }
    .comment-actions button { font-size: 11px; margin-left: 5px; }
    .comment-edit-area { width: 100%; resize: none; margin-top: 5px; }
    .btn-action-area button.active { background: #ffebeb; }
  </style>
</head>

<body>
<%@ include file="/WEB-INF/views/inc/header.jsp" %>

<div class="container">

  <!-- ===== 게시글 헤더 ===== -->
  <div class="header-area">
    <h2>${post.routepostTitle}</h2>
    <div class="post-info">
      작성자: ${post.nickname} |
      작성일: ${post.routepostRegdate} |
      조회수: ${post.routepostViewCount}
    </div>
  </div>

  <!-- ===== 본문 내용 ===== -->
  <div class="content-area">
    <p>${post.routepostContent}</p>
  </div>

  <!-- ===== 이미지 영역 ===== -->
  <c:if test="${not empty images}">
    <div class="image-area-wrapper">
      <span class="image-nav-arrow left">&#10094;</span>
      <div class="image-area">
        <c:forEach var="img" items="${images}">
          <img class="routepost-img"
               src="${pageContext.request.contextPath}/asset/upload/routepost/${img.routepostImageUrl}"
               alt="게시글 이미지">
        </c:forEach>
      </div>
      <span class="image-nav-arrow right">&#10095;</span>
    </div>
  </c:if>

  <!-- ===== 추천 / 스크랩 ===== -->
  <div class="btn-action-area">
    <button id="btn-like" data-id="${post.routepostId}">🤍 추천</button>
    <button id="btn-scrap" data-id="${post.routepostId}">📁 스크랩</button>
  </div>

  <!-- ===== 목록 / 수정 / 삭제 ===== -->
  <div class="btn-area">
    <a href="${pageContext.request.contextPath}/routepost/list" class="btn-list">목록</a>
    <c:if test="${sessionScope.userId eq post.userId}">
      <a href="${pageContext.request.contextPath}/routepost/edit/${post.routepostId}" class="btn-edit">수정</a>
      <a href="${pageContext.request.contextPath}/routepost/del/${post.routepostId}" class="btn-delete"
         onclick="return confirm('정말 삭제하시겠습니까?');">삭제</a>
    </c:if>
  </div>

  <!-- ===== 댓글 영역 ===== -->
  <div class="comment-section" style="margin-top:40px; border-top:1px solid #ccc; padding-top:20px;">
    <h3>댓글 💬 <span id="comment-count">(0)</span></h3>
    <div id="comment-list" style="margin-top:20px;"></div>

    <!-- 댓글 입력 -->
    <div class="comment-input" style="margin-top:25px;">
      <textarea id="comment-content" placeholder="댓글을 입력하세요" rows="3"
                style="width:100%; resize:none;"></textarea>
      <button id="btn-comment-add" style="margin-top:5px; float:right;">등록</button>
      <div style="clear:both;"></div>
    </div>
  </div>
</div>

<!-- ======================= JS ======================= -->
<script>

const routepostId = "${post.routepostId}";
const userId = "${userId}"; // 숫자
const userName = "${userName}"; // 아이디

// ✅ 댓글 목록 불러오기
function loadComments() {
  $.getJSON("${pageContext.request.contextPath}/api/routepost/comment/list/" + routepostId, function(list) {
    let html = "";
    if (list.length === 0) {
      html = "<p>등록된 댓글이 없습니다 😶</p>";
    } else {
      list.forEach(c => {
        html += `
          <div class="comment-item" data-id="${c.routepostCommentId}">
            <b>${c.nickname}</b>
            <small style="color:#999;">${c.routepostCommentRegdate}</small>
            <div class="comment-content">${c.routepostContent}</div>
            <div class="comment-actions">`;
        if (userId === c.userId) {
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

  const data = { routepostId, userId, routepostContent: content };

  $.ajax({
    url: "${pageContext.request.contextPath}/api/routepost/comment/add",
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
    url: "${pageContext.request.contextPath}/api/routepost/comment/del/" + commentId,
    type: "DELETE",
    success: function(res) {
      if (res > 0) loadComments();
      else alert("댓글 삭제 실패 ❌");
    }
  });
});

// ✅ 댓글 수정 모드
$(document).on("click", ".btn-edit", function() {
  const $comment = $(this).closest(".comment-item");
  const $content = $comment.find(".comment-content");
  const original = $content.text().trim();

  // 이미 수정 중이면 return
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
    url: "${pageContext.request.contextPath}/api/routepost/comment/edit",
    type: "PUT",
    contentType: "application/json",
    data: JSON.stringify({ routepostCommentId: commentId, routepostContent: newContent }),
    success: function(res) {
      if (res > 0) loadComments();
      else alert("댓글 수정 실패 ❌");
    }
  });
});

// ✅ 댓글 수정 취소
$(document).on("click", ".btn-cancel", function() {
  const $comment = $(this).closest(".comment-item");
  $comment.find(".comment-edit-area, .btn-save, .btn-cancel").remove();
  $comment.find(".comment-content").show();
});


// ✅ 좋아요
$("#btn-like").click(function() {
  const liked = $(this).hasClass("active");
  $.ajax({
    url: "${pageContext.request.contextPath}/api/routepost/like",
    type: liked ? "DELETE" : "POST",
    contentType: "application/json",
    data: JSON.stringify({ routepostId, userId }),
    success: function(res) {
      if (res > 0) {
        $("#btn-like").toggleClass("active")
          .text(liked ? "🤍 추천" : "❤️ 추천됨");
      }
    }
  });
});

// ✅ 스크랩
$("#btn-scrap").click(function() {
  const scrapped = $(this).hasClass("active");
  $.ajax({
    url: "${pageContext.request.contextPath}/api/routepost/scrap",
    type: scrapped ? "DELETE" : "POST",
    contentType: "application/json",
    data: JSON.stringify({ routepostId, userId }),
    success: function(res) {
      if (res > 0) {
        $("#btn-scrap").toggleClass("active")
          .text(scrapped ? "📁 스크랩" : "✅ 스크랩됨");
      }
    }
  });
});

//✅ 초기 로드 + CSRF 헤더 세팅
$(function() {
  const token = "${_csrf.token}";
  const header = "${_csrf.headerName}";
  $(document).ajaxSend(function(e, xhr) {
    xhr.setRequestHeader(header, token);
  });

  loadComments();
});


</script>

</body>
</html>
