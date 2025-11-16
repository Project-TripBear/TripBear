<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %> 
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/qna.css">

<main>
    <div class="notice-view-container">
        <h2>Q&A 게시판 게시글</h2>

        <div class="notice-header">
            <h3>${dto.question_board_title}</h3>

            <div class="notice-meta">
                <span>작성자: ${dto.nickname}</span>
                <span>작성일: <fmt:formatDate value="${dto.question_board_regdate}" pattern="yyyy-MM-dd HH:mm"/></span>
                <span>조회수: ${dto.question_board_view_count}</span>
            </div>
        </div>

        <div class="notice-content">
            <p><c:out value="${dto.question_board_content}" escapeXml="false" /></p>
        </div>

        <!-- 좋아요 / 스크랩 -->
        <div class="interaction-area" style="text-align:center; margin:30px 0; border-top:1px solid var(--border); padding-top:20px;">
            <sec:authorize access="hasAuthority('ACTIVE')">
                <a href="<c:url value='/qnaboard/like?seq=${dto.question_board_id}'/>" 
                   class="btn ${dto.liked ? 'btn-danger' : 'btn-outline-danger'}" style="margin-right:15px;">
                    <i class="fa-solid ${dto.liked ? 'fa-heart' : 'fa-regular fa-heart'}"></i>
                    좋아요 (${dto.likeCount})
                </a>

                <a href="<c:url value='/qnaboard/scrap?seq=${dto.question_board_id}'/>"
                   class="btn ${dto.scrapped ? 'btn-primary' : 'btn-outline-primary'}">
                    <i class="fa-solid ${dto.scrapped ? 'fa-bookmark' : 'fa-regular fa-bookmark'}"></i>
                    스크랩 (${dto.scrapCount})
                </a>
            </sec:authorize>

            <sec:authorize access="!hasAuthority('ACTIVE')">
                <span style="color:var(--text-light);">로그인하면 좋아요/스크랩을 할 수 있습니다.</span>
            </sec:authorize>
        </div>


        <!-- 목록/수정/삭제 -->
        <div class="notice-actions" style="margin-bottom:50px;">
            <button type="button" class="btn btn-cancel" onclick="location.href='<c:url value="/qnaboard/list"/>';">목록</button>

            <c:set var="currentUserId" value="0"/>
            <sec:authorize access="isAuthenticated()">
                <sec:authentication property="principal.udto.seq" var="currentUserId" />
            </sec:authorize>

            <c:if test="${dto.user_id == currentUserId}">
                <button type="button" class="btn btn-primary" onclick="location.href='<c:url value="/qnaboard/edit?seq=${dto.question_board_id}"/>';">수정</button>

                <form action="<c:url value='/qnaboard/delete'/>" method="GET" style="display:inline;"
                      onsubmit="return confirm('정말 삭제하시겠습니까?');">
                    <input type="hidden" name="seq" value="${dto.question_board_id}">
                    <button type="submit" class="btn btn-danger">삭제</button>
                </form>
            </c:if>

            <c:if test="${dto.user_id != currentUserId && currentUserId != null}">
                <a href="<c:url value='/qnaboard/report?boardSeq=${dto.question_board_id}&reportedUserId=${dto.user_id}'/>"
                   class="btn btn-danger" style="float:right;" target="_blank">신고</a>
            </c:if>
        </div>



        <!-- 댓글 -->
        <div class="comment-area" style="margin-top:30px; border-top:1px solid var(--border); padding-top:20px;">
            <h4>댓글 (${commentList.size()})</h4>

            <div class="comment-list" style="margin-top:15px;">
                <c:forEach var="comment" items="${commentList}">

                    <div class="comment-item" style="border-bottom:1px dashed var(--border); padding:15px 0;">
                        
                        <p style="margin:0 0 5px; font-weight:600; color:var(--primary-dark);">
                            ${comment.nickname}
                            <span style="font-size:0.8em; color:var(--text-light); margin-left:10px;">
                                <fmt:formatDate value="${comment.question_answer_regdate}" pattern="MM-dd HH:mm"/>
                            </span>
                        </p>

                        <p id="comment-content-${comment.question_answer_id}">
                            ${comment.question_answer_content}
                        </p>

                        <c:if test="${comment.user_id == currentUserId}">
				        <div class="comment-buttons" 
				             style="font-size:0.9em; text-align:right; margin-top:5px; position:relative; z-index:5;">
				             
				             <button type="button"
				                     class="comment-round-btn comment-edit-round"
				                     onclick="toggleCommentEdit(${comment.question_answer_id})">
				                 수정
				             </button>
				            
				             <button type="button"
				                     class="comment-round-btn comment-delete-round"
				                     onclick="deleteComment(${comment.question_answer_id}, ${dto.question_board_id})">
				                 삭제
				             </button>

								
                            </div>

                            <!-- 수정 폼 -->
                            <div id="edit-form-${comment.question_answer_id}" style="display:none; margin-top:10px;">
                                <textarea class="form-control" id="edit-text-${comment.question_answer_id}" rows="2">${comment.question_answer_content}</textarea>

                                <button class="btn btn-primary" style="margin-top:8px;" onclick="updateComment(${comment.question_answer_id})">
                                    수정 완료
                                </button>
                                <button class="btn btn-cancel" style="margin-top:8px;" 
                                        onclick="cancelEdit(${comment.question_answer_id})">
                                    취소
                                </button>
                            </div>
                        </c:if>

                    </div>
                </c:forEach>
            </div>


            <!-- 댓글 입력 -->
            <sec:authorize access="hasAuthority('ACTIVE')">
                <form action="<c:url value='/qnaboard/addcomment'/>" method="POST" style="margin-top:25px;">
                    <sec:csrfInput />
                    <input type="hidden" name="question_board_id" value="${dto.question_board_id}">
                    <textarea name="question_answer_content" class="form-control" rows="3" placeholder="댓글을 입력하세요" required></textarea>

                    <div style="text-align:right; margin-top:10px;">
                        <button type="submit" class="btn btn-primary">댓글 등록</button>
                    </div>
                </form>
            </sec:authorize>
        </div>
    </div>
</main>


<!-- ======================  AJAX Scripts  ====================== -->

<script>
/* 댓글 수정 폼 토글 */
function toggleCommentEdit(id) {
    document.getElementById("comment-content-" + id).style.display = 'none';
    document.getElementById("edit-form-" + id).style.display = 'block';
}

/* 댓글 취소 버튼 */
function cancelEdit(id) {
    document.getElementById("comment-content-" + id).style.display = 'block';
    document.getElementById("edit-form-" + id).style.display = 'none';
}

/* 댓글 수정 AJAX */
function updateComment(id) {

    const content = document.getElementById("edit-text-" + id).value;

    fetch('/qnaboard/editcomment', {
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

/* 댓글 삭제 AJAX */
function deleteComment(commentId, boardSeq) {

    if (!confirm("정말 삭제할까요?")) return;

    // 값 확인
    console.log("삭제 요청 commentId =", commentId);
    console.log("삭제 요청 boardSeq =", boardSeq);

    // 삭제 요청 보내기
    location.href = "/trip/qnaboard/deletecomment?commentId=" + commentId + "&boardSeq=" + boardSeq;

}
</script>

