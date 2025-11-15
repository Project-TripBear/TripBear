<%-- view.jsp (최종 통합본) --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %> 
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/findboard.css">

<main>
    <div class="notice-view-container">
        <h2>동행 찾기 게시글</h2>
        
        <fmt:parseDate value="${dto.find_board_regdate}" var="regDateObj" pattern="yyyy-MM-dd HH:mm:ss"/>
        
        <div class="notice-header">
            <h3>${dto.find_board_title}</h3>
            <div class="notice-meta">
                <span>작성자: **${dto.nickname}**</span>
                <span>작성일: <fmt:formatDate value="${regDateObj}" pattern="yyyy-MM-dd HH:mm" /></span>
                <span>조회수: ${dto.find_board_view_count}</span>
            </div>
        </div>
        
        <div class="notice-content">
            <p><c:out value="${dto.find_board_content}" escapeXml="false" /></p>
        </div>
            
        <%-- 좋아요/스크랩 버튼 영역 --%>
        <div class="interaction-area" style="text-align: center; margin: 30px 0; border-top: 1px solid var(--border); padding-top: 20px;">
            <sec:authorize access="hasAuthority('ACTIVE')">
                <%-- 좋아요 버튼 --%>
                <a href="<c:url value="/findboard/like?seq=${dto.find_board_id}"/>" class="btn ${dto.liked ? 'btn-danger' : 'btn-outline-danger'}" style="margin-right: 15px;">
                    <i class="fa-solid ${dto.liked ? 'fa-heart' : 'fa-regular fa-heart'}"></i> 좋아요 (${dto.likeCount})
                </a>
                
                <%-- 스크랩 버튼 --%>
                <a href="<c:url value="/findboard/scrap?seq=${dto.find_board_id}"/>" class="btn ${dto.scrapped ? 'btn-primary' : 'btn-outline-primary'}">
                    <i class="fa-solid ${dto.scrapped ? 'fa-bookmark' : 'fa-regular fa-bookmark'}"></i> 스크랩 (${dto.scrapCount})
                </a>
            </sec:authorize>
            <sec:authorize access="!hasAuthority('ACTIVE')">
                <span style="color:var(--text-light);">로그인하면 좋아요/스크랩을 할 수 있습니다.</span>
            </sec:authorize>
        </div>
        
        <div class="notice-actions" style="margin-bottom: 50px;">
    <button type="button" class="btn btn-cancel" onclick="location.href='<c:url value="/findboard/list"/>';">목록</button>
    
    <%-- 비로그인 시 오류 방지를 위한 ID 변수 설정 --%>
    <c:set var="currentUserId" value="0"/>
	<sec:authorize access="hasAuthority('ACTIVE')">
        <sec:authentication property="principal.udto.seq" var="currentUserId" />
    </sec:authorize>
    
    <%-- 수정/삭제 버튼 (작성자 본인일 때) --%>
    <c:if test="${dto.user_id == currentUserId}">
        <button type="button" class="btn btn-primary" onclick="location.href='<c:url value="/findboard/edit?seq=${dto.find_board_id}"/>';">수정</button>

        <form action="<c:url value="/findboard/delete"/>" method="GET" style="display:inline;"
            onsubmit="return confirm('정말 삭제하시겠습니까?');">
            <input type="hidden" name="seq" value="${dto.find_board_id}" />
            <button type="submit" class="btn btn-danger">삭제</button>
        </form>
    </c:if>
    
   <%-- ★★★ [JSP 오류 수정] 신고 버튼 c:if 블록 수정 ★★★ --%>
    <c:if test="${dto.user_id != currentUserId and currentUserId != 0}">
        <%-- 1. 팝업 URL 변수 생성 --%>
        <c:url value="/findboard/report" var="reportUrl">
            <c:param name="boardSeq" value="${dto.find_board_id}" />
            <c:param name="reportedUserId" value="${dto.user_id}" />
        </c:url>
      <%-- 2. onclick 팝업 이벤트로 변경 --%>
        <a href="${reportUrl}" class="btn btn-danger" style="float: right;"
           onclick="window.open(this.href, 'reportPopup', 'width=500,height=600,scrollbars=yes'); return false;">신고</a>
    </c:if> <%-- ★★★ 닫는 태그가 신고 링크(a 태그) 뒤에 와야 합니다. ★★★ --%>
</div>
        <%-- 댓글 목록 및 작성 폼 영역 --%>
        <div class="comment-area" style="margin-top: 30px; border-top: 1px solid var(--border); padding-top: 20px;">
            <h4>댓글 (${commentList.size()})</h4>
            
            <div class="comment-list" style="margin-top: 15px;">
                <c:forEach items="${commentList}" var="comment">
                    <fmt:parseDate value="${comment.find_comment_regdate}" var="commentRegDateObj" pattern="yyyy-MM-dd HH:mm:ss"/>
                    
                    <div class="comment-item" style="border-bottom: 1px dashed var(--border); padding: 15px 0;">
                        <p style="margin: 0 0 5px 0; font-weight: 600; color: var(--primary-dark);">
                            ${comment.nickname} <span style="font-size: 0.8em; color: var(--text-light); font-weight: 400; margin-left: 10px;">
                            <fmt:formatDate value="${commentRegDateObj}" pattern="MM-dd HH:mm"/>
                            </span>
                        </p>
                        <p id="comment-content-${comment.find_comment_id}" style="margin: 0;">${comment.find_comment_content}</p>
                        
                        <c:if test="${comment.user_id == currentUserId}">
                            <div class="comment-actions" style="font-size: 0.9em; text-align: right;">
                                <a href="#" onclick="toggleCommentEdit(${comment.find_comment_id}, '${comment.find_comment_content}')">수정</a>
                                <span style="margin: 0 5px;">|</span>
                                <a href="<c:url value="/findboard/deletecomment?commentId=${comment.find_comment_id}&boardSeq=${dto.find_board_id}"/>" 
                                    onclick="return confirm('댓글을 삭제하시겠습니까?');" style="color: var(--danger);">삭제</a>
                            </div>
                            
                            <form id="edit-form-${comment.find_comment_id}" action="<c:url value="/findboard/editcomment"/>" method="POST" style="display:none; margin-top: 10px;">
                                <sec:csrfInput /> <input type="hidden" name="find_comment_id" value="${comment.find_comment_id}"> 
                                <input type="hidden" name="boardSeq" value="${dto.find_board_id}">
                                <textarea name="find_comment_content" class="form-control" rows="2" required>${comment.find_comment_content}</textarea> 
                                <button type="submit" class="btn btn-primary" style="margin-top: 5px; padding: 5px 10px; font-size: 0.8em;">수정 완료</button>
                                <button type="button" class="btn btn-cancel" style="margin-top: 5px; padding: 5px 10px; font-size: 0.8em;" onclick="document.getElementById('edit-form-${comment.find_comment_id}').style.display='none';">취소</button>
                            </form>
                        </c:if>
                    </div>
                </c:forEach>
            </div>
            
            <sec:authorize access="hasAuthority('ACTIVE')">
                <form action="<c:url value="/findboard/addcomment"/>" method="POST" style="margin-top: 25px;">
                    <sec:csrfInput /> <input type="hidden" name="find_board_id" value="${dto.find_board_id}">
                    <textarea name="find_comment_content" class="form-control" rows="3" required placeholder="댓글을 입력하세요"></textarea>
                    <div style="text-align: right; margin-top: 10px;">
                        <button type="submit" class="btn btn-primary" style="padding: 8px 15px;">댓글 등록</button>
                    </div>
                </form>
            </sec:authorize>
        </div>
    </div>
</main>

<script>
    function toggleCommentEdit(commentId, content) {
        const displayDiv = document.getElementById('comment-content-' + commentId);
        const editForm = document.getElementById('edit-form-' + commentId);
        
        if (editForm.style.display === 'none') {
            displayDiv.style.display = 'none';
            editForm.style.display = 'block';
            editForm.querySelector('textarea').focus();
        } else {
            displayDiv.style.display = 'block';
            editForm.style.display = 'none';
        }
        return false;
    }
</script>