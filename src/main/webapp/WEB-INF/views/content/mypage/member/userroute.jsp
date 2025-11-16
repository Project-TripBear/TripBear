<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <%-- jQuery 라이브러리를 꼭 추가해주세요 --%>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/mypage.css">
    
    <%-- ▼▼▼ 분리한 CSS 파일을 연결합니다 ▼▼▼ --%>

</head>
<body>


  <%--   <div id="main">
        <h1>내 여행 루트</h1>

        <div id="route-list-body">
            
            <c:choose>
                <c:when test="${not empty list}">
                    루트가 있을 때
                    <c:forEach items="${list}" var="dto">
                        <div class="route-item" onclick="location.href='/trip/route/userRouteView.do?id=${dto.seq}'">
                            <div class="route-title">${dto.userroutetitle}</div>
                            <div class="route-details">
                                <span>인원수: ${dto.userroutedays}</span>
                                <span>여행시작일: ${dto.userroutestartdate}</span>
                                <span>여행종료일: ${dto.userrouteenddate}</span>
                            </div>
                            
                        </div>
                        <form action="${pageContext.request.contextPath}/reservation/accomList.do" method="get">
                            <input type="hidden" name="start_date" value="${dto.userroutestartdate}">
                            <input type="hidden" name="end_date" value="${dto.userrouteenddate}">
                            <input type="hidden" name="people" value="${dto.userroutedays}">
                            <button type="submit" class="btn btn-primary">예약하기</button>
                        </form>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    루트가 없을 때 (list가 비어있거나 null일 때)
                    <p style="text-align: center; padding: 20px; font-size: 1.2em; color: #6c757d;">
                        **등록된 여행 루트가 없습니다.** 
                    </p>
                </c:otherwise>
            </c:choose>
            
        </div>

        <c:if test="${nowPage < totalPage}">
            <div class="load-more-container">
                <button id="loadMoreBtn">더보기</button>
            </div>
        </c:if>
    </div> --%>
    
    <div class="page-userroute-container"> <div id="main">
        <h1>내 여행 루트</h1>

        <div id="route-list-wrapper"> <c:choose>
                <c:when test="${not empty list}">
                    <c:forEach items="${list}" var="dto">
                        <div class="route-card-item"> <div class="route-item-content" onclick="location.href='/trip/route/userRouteView.do?id=${dto.seq}'">
                                <div class="route-title">${dto.userroutetitle}</div>
                                <div class="route-details">
                                    <span><i class="fa-solid fa-calendar"></i> 여행일자: ${dto.userroutedays}</span>
                                    <span><i class="far fa-calendar-alt"></i> 시작: ${dto.userroutestartdate}</span>
                                    <span><i class="far fa-calendar-alt"></i> 종료: ${dto.userrouteenddate}</span>
                                </div>
                            </div>
                            
                            <form action="${pageContext.request.contextPath}/reservation/select-accom" method="get" class="route-booking-form"> 
                            	<input type="hidden" name="region" value="${dto.userrouteregion}">
                            	<input type="hidden" name="checkin" value="${dto.userroutestartdate}">
                                <input type="hidden" name="checkout" value="${dto.userrouteenddate}">
                                <input type="hidden" name="people" value="${dto.userroutedays}">
                                <button type="submit" class="btn btn-primary btn-book">예약하기</button> </form>
                            
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p class="no-route-message">
                        **등록된 여행 루트가 없습니다.**
                    </p>
                </c:otherwise>
            </c:choose>
        </div>

        <c:if test="${nowPage < totalPage}">
            <div class="load-more-container">
                <button id="loadMoreBtn" class="btn btn-secondary">더보기</button> </div>
        </c:if>
    </div>
</div>

    <%-- JavaScript 부분은 변경 없이 그대로 둡니다. --%>
    <script>
        let currentPage = ${nowPage};
        const totalPage = ${totalPage};

        $('#loadMoreBtn').on('click', function() {
            currentPage++;

            $.ajax({
                type: 'GET',
                url: '/trip/user/userroute.do',
                data: {
                    page: currentPage,
                    ajax: 'true'
                },
                dataType: 'json',
                success: function(newList) {
                    if (newList.length > 0) {
                        newList.forEach(function(dto) {
                            const newRow = `
                                <div class="route-item" onclick="location.href='/trip/user/userroute.do?seq=\${dto.seq}'">
                                    <div class="route-title">\${dto.userroutetitle}</div>
                                    <div class="route-details">
                                        <span>인원수: \${dto.userroutedays}</span>
                                        <span>여행시작일: \${dto.userroutestartdate}</span>
                                        <span>여행종료일: \${dto.userrouteenddate}</span>
                                    </div>
                                </div>
                            `;
                            $('#route-list-body').append(newRow);
                        });
                    }

                    if (currentPage >= totalPage) {
                        $('#loadMoreBtn').parent().hide();
                    }
                },
                error: function(err) {
                    console.log('데이터를 불러오는 데 실패했습니다.', err);
                    alert('오류가 발생했습니다. 다시 시도해주세요.');
                }
            });
        });
    </script>

</body>
</html>