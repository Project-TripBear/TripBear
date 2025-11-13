<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %> 
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>

<main>
    <div class="notice-view-container">
        <h2>Q&A 게시판 게시글</h2>

        <div class="notice-header">
            <h3>${dto.question_board_title}</h3>

            <div class="notice-meta">
                <span>작성자: ${dto.nickname}</span>

                <span>
                    작성일: 
                    <fmt:formatDate value="${dto.question_board_regdate}" pattern="yyyy-MM-dd HH:mm"/>
                </span>

                <span>조회수: ${dto.question_board_view_count}</span>
            </div>
        </div>

        <div class="notice-content">
            <p><c:out value="${dto.question_board_content}" escapeXml="false" /></p>
        </div>

        <!-- 좋아요 & 스크랩 -->
        <div class="interaction-area" style="text-align: center; margin: 30px 0; border-top: 1px solid var(--border); padding-top: 20px;">
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
        <div class="notice-actions" style="margin-bottom: 50px;">
            <button type="button" class="btn btn-cancel" onclick="location.href='<c:url value="/qnaboard/list"/>';">목록</button>
			
			<!-- 로그인한 경우에만 principal.udto.seq 읽기 -->
            <sec:authorize access="isAuthenticated()">
			    <sec:authentication property="principal.udto.seq" var="currentUserId" />
			</sec:authorize>
			<!-- 비로그인 시 currentUserId 기본값 -->
			<c:if test="${empty currentUserId}">
			    <c:set var="currentUserId" value="0"/>
			</c:if>

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

                        <p id="comment-content-${comment.question_answer_id}">${comment.question_answer_content}</p>

                        <c:if test="${comment.user_id == currentUserId}">
                            <div style="font-size:0.9em; text-align:right;">
                                <a href="#" onclick="toggleCommentEdit(${comment.question_answer_id})">수정</a>
                                |
                                <a href="<c:url value='/qnaboard/deletecomment?commentId=${comment.question_answer_id}&boardSeq=${dto.question_board_id}'/>"
                                   onclick="return confirm('댓글을 삭제하시겠습니까?');"
                                   style="color:var(--danger);">삭제</a>
                            </div>

                            <form id="edit-form-${comment.question_answer_id}" action="<c:url value='/qnaboard/editcomment'/>"
                                  method="POST" style="display:none; margin-top:10px;">
                                <input type="hidden" name="question_answer_id" value="${comment.question_answer_id}">
                                <input type="hidden" name="boardSeq" value="${dto.question_board_id}">
                                <textarea name="question_answer_content" class="form-control" rows="2" required>${comment.question_answer_content}</textarea>
                                <button type="submit" class="btn btn-primary" style="margin-top:5px; padding:5px 10px; font-size:0.8em;">수정 완료</button>
                                <button type="button" class="btn btn-cancel" style="margin-top:5px; padding:5px 10px; font-size:0.8em;"
                                        onclick="document.getElementById('edit-form-${comment.question_answer_id}').style.display='none';">취소</button>
                            </form>
                        </c:if>
                    </div>

                </c:forEach>
            </div>

            <sec:authorize access="hasAuthority('ACTIVE')">
                <form action="<c:url value='/qnaboard/addcomment'/>" method="POST" style="margin-top:25px;">
                    <input type="hidden" name="question_board_id" value="${dto.question_board_id}">
                    <textarea name="question_answer_content" class="form-control" rows="3" required placeholder="댓글을 입력하세요"></textarea>

                    <div style="text-align:right; margin-top:10px;">
                        <button type="submit" class="btn btn-primary">댓글 등록</button>
                    </div>
                </form>
            </sec:authorize>

        </div>
    </div>
</main>

<script>
function toggleCommentEdit(id) {
    const display = document.getElementById('comment-content-' + id);
    const form = document.getElementById('edit-form-' + id);

    if (form.style.display === 'none') {
        display.style.display = 'none';
        form.style.display = 'block';
    } else {
        display.style.display = 'block';
        form.style.display = 'none';
    }
}
</script>
