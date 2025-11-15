<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common.css">	
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/mypage.css">

</head>
<body>


<div class="page-mypage-container">

	<div id="main">
	
		<h1>마이페이지</h1>
		
        <div id="my-summary" class="summary-text-block"> 
		    <span class="summary-label">내 활동:</span>
		    <span id="boardCount" class="summary-item">게시글 수: 로딩 중...</span>
		    <span id="commentCount" class="summary-item">댓글 수: 로딩 중...</span>
		    <span id="likeCount" class="summary-item">좋아요 수: 로딩 중...</span>
		    <span id="scrapCount" class="summary-item">스크랩 수: 로딩 중...</span>
		</div>
		
	<div class="menu-card-wrapper">
    <table id="view" class="mypage-menu-table">
        <tr>
            <th></th> <td><a href="/trip/member/userinfo">회원정보</a></td>
        </tr>
        <tr>
            <th></th>
            <td><a href="/trip/member/boardactivities">활동내역</a></td>
        </tr>
        <tr>
            <th></th>
            <td><a href="/trip/member/userroute">내 여행루트</a></td>
        </tr>
        <tr>
            <th></th>
            <td><a href="/trip/member/carreservation">예약</a></td>
        </tr>
    </table>
</div>
        </div>
	
</div>

<script>
    // 💡 스크립트 수정: 텍스트에 "게시글 수: " 레이블을 다시 붙여줍니다.
    document.addEventListener("DOMContentLoaded", function () {
        fetch(`${pageContext.request.contextPath}/member/myactivitiessummary`)
          .then(res => res.json())
          .then(data => {
            document.getElementById('boardCount').textContent = "게시글 수: " + data.boardCount;
            document.getElementById('commentCount').textContent = "댓글 수: " + data.commentCount;
            document.getElementById('likeCount').textContent = "좋아요 수: " + data.likeCount;
            document.getElementById('scrapCount').textContent = "스크랩 수: " + data.scrapCount;
          })
            .catch(err => {
                console.error('내 활동 요약 불러오기 실패', err);
                ['boardCount','commentCount','likeCount','scrapCount'].forEach(id => {
                    document.getElementById(id).textContent = '실패';
                });
            });
    });
</script>
</body>
</html>