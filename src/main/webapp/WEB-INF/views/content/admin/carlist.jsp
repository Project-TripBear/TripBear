<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<section class="content-header">
    <h1>렌터카 관리 <small>목록 및 필터링</small></h1>
</section>

<section class="content">
    <div class="row">
        <div class="col-md-12">
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">차량 목록 (${list.size()} 건)</h3>
                    <div class="box-tools">
                        <button type="button" class="btn btn-success btn-sm" onclick="location.href='add'">
                            <i class="fa fa-plus"></i> 신규 렌터카 등록
                        </button>
                    </div>
                </div>
                
                <div class="box-body">
                    <table class="table table-bordered table-striped text-center">
                        <thead>
                            <tr>
                                <th style="width: 10px;">ID</th>
                                <th>차량명</th>
                                <th>제조사</th>
                                <th>차종</th>
                                <th>연식</th>
                                <th>연료</th>
                                <th>일일 가격</th>
                                <th style="width: 15%;">관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty list}">
                                    <c:forEach var="car" items="${list}">
                                        <tr>
                                            <td>${car.carId}</td>
                                            <td>
                                                <a href="view?carId=${car.carId}">${car.carName}</a>
                                            </td>
                                            <td>${car.carCompany}</td>
                                            <td>${car.carType}</td>
                                            <td>${car.carYear}</td>
                                            <td>${car.carFuel}</td>
                                            <td><fmt:formatNumber value="${car.carPrice}" pattern="#,###"/>원</td>
                                            <td>
                                                <a href="edit?carId=${car.carId}" class="btn btn-warning btn-xs">수정</a>
                                                
                                                <button type="button" class="btn btn-danger btn-xs delete-btn" data-id="${car.carId}">삭제</button>
                                                
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="8">조회된 렌터카 정보가 없습니다.</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
                </div>
            </div>
    </div>
</section>

<form id="deleteForm" action="delete" method="post">
    <input type="hidden" name="carId" id="deleteCarId">
</form>

<script>
$(document).ready(function() {
    // 삭제 버튼 클릭 이벤트
    $('.delete-btn').on('click', function() {
        var carId = $(this).data('id');
        if (confirm(carId + '번 차량을 정말 삭제하시겠습니까? (예약 내역이 있으면 실패할 수 있습니다)')) {
            $('#deleteCarId').val(carId);
            $('#deleteForm').submit();
        }
    });
});
</script>