<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">
<head>

	
</head>
<body>
	
	
	<%-- <nav class="board-sub-header">
    <div class="sub-header-inner">
        <a href="/trip/member/carreservation.do">렌트카 예약</a>
        <a href="/trip/member/accomreservation.do">숙소 예약</a>
    </div>
</nav>
	
	<div id="main" >
		<h1>숙소 예약</h1>
		
		<c:if test="${map.search == 'y'}">
		<div id="labelSearch">
			'${map.word}'(으)로 검색한 결과 ${map.totalCount}건이 있습니다.			
		</div>
		</c:if>
		
		
		<table id="list" class="borad-table">
			<tr>
				<th>예약번호</th>
				<th>객실명</th>
				<th>체크인</th>
				<th>체크아웃</th>
			</tr>
			<c:if test="${list.size() == 0}">
			<tr>
				<td colspan="5">게시물이 없습니다.</td>
			</tr>
			</c:if>
			<c:forEach items="${list}" var="dto">
			
			<tr onclick="location.href='/trip/member/accomreservationview?seq=${dto.seq}&accomseq=${dto.accomseq}'">
				<td>
				${dto.seq}	
					
				</td>
				<td>
				<!-- 글제목 -->
	

					${dto.roomname}
					
				</td>
				<td>
					${dto.checkindate}	
				</td>
				<td>
					${dto.checkoutdate}	
				</td>
			</tr>
			</c:forEach>
		</table>
		
		<!-- 검색 -->
		<!-- <form id="searchForm" method="GET" action="/main/user/boardactivities.do">
			<select name="column">
				<option value="hotdeal_title">제목</option>
				<option value="hotdeal_content">내용</option>
				<option value="hotdeal_name">이름</option>
			</select>
			<input type="text" name="word" class="long" required>
			<input type="submit" value="검색하기">	
		</form>		 -->
		
		<!-- 페이지바 -->
		<div id="pagebar">${pagebar}</div>
		
	
		
	</div> --%>
	
	<div class="page-accomreservation-container"> <nav class="board-sub-header">
        <div class="sub-header-inner">
            <a href="/trip/member/carreservation.do">렌트카 예약</a>
            <a href="/trip/member/accomreservation.do" class="active">숙소 예약</a> </div>
    </nav>
	
	<div id="main">
		<h1>숙소 예약</h1>
		
		<c:if test="${map.search == 'y'}">
		<div id="labelSearch">
			'${map.word}'(으)로 검색한 결과 ${map.totalCount}건이 있습니다.			
		</div>
		</c:if>	
		
		<table id="list" class="reservation-list-table"> <thead>
                <tr>
                    <th>예약번호</th>
                    <th>객실명</th>
                    <th>체크인</th>
                    <th>체크아웃</th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${list.size() == 0}">
                <tr>
                    <td colspan="4" class="no-data-cell">예약 내역이 없습니다.</td> </tr>
                </c:if>
                <c:forEach items="${list}" var="dto">
                <tr onclick="location.href='/trip/member/accomreservationview?seq=${dto.seq}&accomseq=${dto.accomseq}'" class="data-row">
                    <td>${dto.seq}</td>
                    <td>${dto.roomname}</td>
                    <td>${dto.checkindate}</td>
                    <td>${dto.checkoutdate}</td>
                </tr>
                </c:forEach>
            </tbody>
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

			
			
		
