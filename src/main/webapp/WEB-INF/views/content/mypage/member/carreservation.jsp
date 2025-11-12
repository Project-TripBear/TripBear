<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
</head>
<body>
	
	<%-- 
	<nav class="board-sub-header">
    <div class="sub-header-inner">
        <a href="/trip/member/carreservation">렌트카 예약</a>
        <a href="/trip/member/accomreservation">숙소 예약</a>
    </div>
</nav>
	
	<div id="main" >
		<h1>차량 예약</h1>
		
		<c:if test="${map.search == 'y'}">
		<div id="labelSearch">
			'${map.word}'(으)로 검색한 결과 ${map.totalCount}건이 있습니다.			
		</div>
		</c:if>	
		
		<table id="list" class="borad-table">
			<tr>
				<th>예약번호</th>
				<th>차종</th>
				<th>차량모델</th>
				<th>차량대여일</th>
				<th>차량반납일</th>
			</tr>
			<c:if test="${list.size() == 0}">
			<tr>
				<td colspan="5">예약 내역이 없습니다.</td>
			</tr>
			</c:if>
			<c:forEach items="${list}" var="dto">
			<tr onclick="location.href='/trip/member/carreservationview?seq=${dto.seq}&carseq=${dto.carseq}'">
				<td>
				${dto.seq}	
					
				</td>
				<td>

					${dto.cartype}

					
				</td>
				<td>
		
					${dto.carname}	
				</td>
				<td>
					${dto.pickupdate}	
				</td>
				<td>
					${dto.dropoffdate}	
				</td>
			</tr>
			</c:forEach>
		</table>


		<div id="pagebar">${pagebar}</div>
		
	
	</div> --%>
	
	<div class="page-carreservation-container"> <nav class="board-sub-header">
        <div class="sub-header-inner">
            <a href="/trip/member/carreservation" class="active">렌트카 예약</a> <a href="/trip/member/accomreservation">숙소 예약</a>
        </div>
    </nav>
	
	<div id="main">
		<h1>차량 예약</h1>
		
		<c:if test="${map.search == 'y'}">
		<div id="labelSearch">
			'${map.word}'(으)로 검색한 결과 ${map.totalCount}건이 있습니다.			
		</div>
		</c:if>	
		
		<table id="list" class="reservation-list-table"> <thead>
                <tr>
                    <th>예약번호</th>
                    <th>차종</th>
                    <th>차량모델</th>
                    <th>차량대여일</th>
                    <th>차량반납일</th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${list.size() == 0}">
                <tr>
                    <td colspan="5" class="no-data-cell">예약 내역이 없습니다.</td>
                </tr>
                </c:if>
                <c:forEach items="${list}" var="dto">
                <tr onclick="location.href='/trip/member/carreservationview?seq=${dto.seq}&carseq=${dto.carseq}'" class="data-row">
                    <td>${dto.seq}</td>
                    <td>${dto.cartype}</td>
                    <td>${dto.carname}</td>
                    <td>${dto.pickupdate}</td>
                    <td>${dto.dropoffdate}</td>
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