<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">

</head>
<body>
	
	<div class="page-likeactivities-container">
<nav class="board-sub-header">
    <div class="sub-header-inner">
        <a href="/trip/member/boardactivities">내가 쓴 게시글</a>
        <a href="/trip/member/commentactivities">내가 쓴 댓글</a>
        <a href="/trip/member/likeactivities">좋아요</a>
        <a href="/trip/member/scrapactivities">스크랩</a>
    </div>
</nav>
	
	<div id="main" >
		<h1>내가 좋아요한 게시글</h1>
		
		<c:if test="${map.search == 'y'}">
		<div id="labelSearch">
			'${map.word}'(으)로 검색한 결과 ${map.totalCount}건이 있습니다.			
		</div>
		</c:if>

		
		
		<table id="list" class="activity-list-table">
			<tr>
				<th>게시판이름</th>
				<th>제목</th>
				<th>날짜</th>
			</tr>
			<c:if test="${list.size() == 0}">
			<tr>
				<td colspan="5">게시물이 없습니다.</td>
			</tr>
			</c:if>
			<c:forEach items="${list}" var="dto">
                <tr class="data-row">
                    <td class="board-title-cell">${dto.boradTitle}</td>
                    <td class="post-subject-cell">
                        <a href="/trip/${dto.boradCode}/view?seq=${dto.seq}&column=${map.column}&word=${map.word}">
                            ${dto.subject}
                        </a>
                    </td>
                    <td class="regdate-cell">${dto.regdate}</td>
                </tr>
            </c:forEach>
			<%-- <c:forEach items="${list}" var="dto">
			

			
			
				<td>
				<!-- 글번호 -->
				${dto.boradTitle}	
					
				</td>
				<td>
				<!-- 글제목 -->
						<a href="/trip/board/${dto.boradCode}.do?seq=${dto.seq}&column=${map.column}&word=${map.word}">
						${dto.subject}</a>


					
				</td>
				<td>
			
					${dto.regdate}	
				</td>
			</tr>
			</c:forEach> --%>
		</table>
		

		<div id="pagebar" class="pagebar-container">${pagebar}</div>
		
		
		
	</div>
	</div>
	
	<script>
	
		<c:if test="${map.search == 'y'}">
		$('select[name=column]').val('${map.column}');
		$('input[name=word]').val('${map.word}');
		</c:if>
	
	</script>
		
</body>
</html>

			
			
		
		
	



