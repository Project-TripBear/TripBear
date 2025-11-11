<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<div class="page-mypage-container">

	<div id="main">
	
		<h1>마이페이지</h1>
		
			<div id="my-summary"> 
		    <ul id="summary-list">
				<li>내 활동 요약</li>
		        <li id="boardCount">게시글 수: 로딩 중...</li>
		        <li id="commentCount">댓글 수: 로딩 중...</li>
		        <li id="likeCount">좋아요 수: 로딩 중...</li>
		        <li id="scrapCount">스크랩 수: 로딩 중...</li>
		    </ul>
		</div>

		<table id="view" class="mypage-menu-table">
			<tr>
				<th>회원정보</th>
				<td><a href="/trip/member/userinfo">수정 및 탈퇴</a></td>
			</tr>
			
			<tr>
				<th>활동내역</th>
				<td><a href="/trip/member/boardactivities">게시글, 댓글, 좋아요</a></td>
			</tr>
						
			<tr>
				<th>내 여행루트</th>
				<td><a href="/trip/member/userroute">관리</a></td>
			</tr>
			
			<tr>
				<th>예약</th>
				<td><a href="/trip/member/carreservation">내역 확인</a></td>
			</tr>
			
		</table>
	
	
		
	</div>
	
</div>


<script>
    document.addEventListener("DOMContentLoaded", function () {
    	fetch(`${pageContext.request.contextPath}/member/myactivitiessummary`)
    	  .then(res => res.json())
    	  .then(data => {
    	    console.log(data); // ← data 내용 확인
    	    document.getElementById('boardCount').textContent = "게시글 수: " + data.boardCount;
    	    document.getElementById('commentCount').textContent = "댓글 수: " + data.commentCount;
    	    document.getElementById('likeCount').textContent = "좋아요 수: " + data.likeCount;
    	    document.getElementById('scrapCount').textContent = "스크랩 수: " + data.scrapCount;
    	  })
            .catch(err => {
                console.error('내 활동 요약 불러오기 실패', err);
                const els = ['boardCount','commentCount','likeCount','scrapCount'];
                els.forEach(id => {
                    document.getElementById(id).textContent = '불러오기 실패';
                });
            });
    });
</script>
</body>
</html>