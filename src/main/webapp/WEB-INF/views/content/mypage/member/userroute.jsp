<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    

</head>
<body>


    <div id="main">
        <h1>내 여행 루트</h1>

        <div id="route-list-body">
            <c:forEach items="${list}" var="dto">
                <div class="route-item" onclick="location.href='/trip/user/trip/route/userRouteView.do?id=${dto.seq}'">
                    <div class="route-title">${dto.userroutetitle}</div>
                    <div class="route-details">
                        <span>인원수: ${dto.userroutedays}</span>
                        <span>여행시작일: ${dto.userroutestartdate}</span>
                        <span>여행종료일: ${dto.userrouteenddate}</span>
                    </div>
                </div>
            </c:forEach>
        </div>

        <c:if test="${nowPage < totalPage}">
            <div class="load-more-container">
                <button id="loadMoreBtn">더보기</button>
            </div>
        </c:if>
    </div>

    <script src="https://code.jquery.com/jquery-3.7.1.min.js"/>
    <script>
        let currentPage = ${nowPage};
        const totalPage = ${totalPage};

        $('#loadMoreBtn').on('click', function() {
            currentPage++;

            $.ajax({
                type: 'GET',
                url: '/main/user/userroute.do',
                data: {
                    page: currentPage,
                    ajax: 'true'
                },
                dataType: 'json',
                success: function(newList) {
                    if (newList.length > 0) {
                        newList.forEach(function(dto) {
                            const newRow = `
                                <div class="route-item" onclick="location.href='/main/user/userroute.do?seq=\${dto.seq}'">
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