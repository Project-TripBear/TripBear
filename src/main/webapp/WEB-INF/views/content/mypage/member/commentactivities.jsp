<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">
<head>

</head>
<body>
	
	
	<nav class="board-sub-header">
    <div class="sub-header-inner">
        <a href="/trip/member/boardactivities">내가 쓴 게시글</a>
        <a href="/trip/member/commentactivities">내가 쓴 댓글</a>
        <a href="/trip/member/likeactivities">좋아요</a>
        <a href="/trip/member/scrapactivities">스크랩</a>
    </div>
</nav>
	
	<div id="main" >
		<h1>내가 쓴 댓글</h1>
		
		<c:if test="${map.search == 'y'}">
		<div id="labelSearch">
			'${map.word}'(으)로 검색한 결과 ${map.totalCount}건이 있습니다.			
		</div>
		</c:if>
		

		
		
		<table id="list" class="borad-table">
    <tr>
        <th>게시판 이름</th>
        <th>제목 / 댓글 내용</th>
        <th>댓글 작성일</th>
    </tr>
    
    <c:if test="${list.size() == 0}">
    <tr>
        <td colspan="3">작성한 댓글이 없습니다.</td>
    </tr>
    </c:if>
    
    <c:forEach items="${list}" var="dto">
    <tr class="data-row">
        <td>
            ${dto.boradTitle}	
        </td>
        <td>
            <div class="post-title">
                <a href="/trip/board/${dto.boradCode}.do?seq=${dto.seq}&column=${map.column}&word=${map.word}">
                    ${dto.subject}
                </a>
            </div>
        </td>
        <td>
            ${dto.regdate}	
        </td>
    </tr>
    
    <tr class="comment-details-row">
        <td colspan="3" class="comment-details-cell">
            댓글 내용 : ${dto.commentcontent}
        </td>
    </tr>
</c:forEach>
</table>
		
	
		<div id="pagebar">${pagebar}</div>
		

		
	</div>
	
	<script>
	
		<c:if test="${map.search == 'y'}">
		$('select[name=column]').val('${map.column}');
		$('input[name=word]').val('${map.word}');
		</c:if>
	
	</script>
		
</body>
</html>

			
			
		
		
	



