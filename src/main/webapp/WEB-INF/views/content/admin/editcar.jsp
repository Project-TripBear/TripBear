<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<section class="content-header">
    <h1>렌터카 정보 수정 <small>${carDetail.carName}</small></h1>
</section>

<section class="content">
    <div class="row">
        <div class="col-md-12">
            <div class="box box-warning">
                <div class="box-header with-border">
                    <h3 class="box-title">차량 정보 입력</h3>
                </div>
                
                <form role="form" action="edit" method="post">
                    <div class="box-body">
                        
                        <input type="hidden" name="carId" value="${carDetail.carId}">

                        <div class="form-group">
                            <label for="carName">차량명</label>
                            <input type="text" name="carName" class="form-control" id="carName" value="${carDetail.carName}" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="carCompany">제조사</label>
                            <input type="text" name="carCompany" class="form-control" id="carCompany" value="${carDetail.carCompany}" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="carYear">연식</label>
                            <input type="number" name="carYear" class="form-control" id="carYear" value="${carDetail.carYear}" required>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="carType">차종</label>
                                    <select class="form-control" name="carType" id="carType" required>
                                        <option value="경차" ${carDetail.carType eq '경차' ? 'selected' : ''}>경차</option>
                                        <option value="세단" ${carDetail.carType eq '세단' ? 'selected' : ''}>세단</option>
                                        <option value="SUV" ${carDetail.carType eq 'SUV' ? 'selected' : ''}>SUV</option>
                                        <option value="승합/밴" ${carDetail.carType eq '승합/밴' ? 'selected' : ''}>승합/밴</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="carFuel">연료 타입</label>
                                    <select class="form-control" name="carFuel" id="carFuel" required>
                                        <option value="휘발유" ${carDetail.carFuel eq '휘발유' ? 'selected' : ''}>휘발유</option>
                                        <option value="경유" ${carDetail.carFuel eq '경유' ? 'selected' : ''}>경유</option>
                                        <option value="LPG" ${carDetail.carFuel eq 'LPG' ? 'selected' : ''}>LPG</option>
                                        <option value="전기" ${carDetail.carFuel eq '전기' ? 'selected' : ''}>전기</option>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="carPrice">일일 가격 (원)</label>
                            <input type="text" name="carPrice" class="form-control text-right" id="carPrice" value="<fmt:formatNumber value="${carDetail.carPrice}" pattern="#,###"/>" required onkeyup="removeChar(event); this.value=addCommas(this.value);" onfocus="this.value=removeCommas(this.value);" onblur="this.value=addCommas(this.value);">
                        </div>
                        
                        <div class="form-group">
                            <label for="carImage">차량 이미지 URL (또는 파일명)</label>
                            <input type="text" name="carImage" class="form-control" id="carImage" value="${carDetail.carImage}">
                        </div>

                        <div class="form-group">
                            <label for="carDescription">상세 설명</label>
                            <textarea name="carDescription" class="form-control" rows="5" id="carDescription">${carDetail.carDescription}</textarea>
                        </div>

                    </div>
                    <div class="box-footer">
                        <button type="submit" class="btn btn-warning">수정 완료</button>
                        <button type="button" class="btn btn-default" onclick="history.back();">취소</button>
                    </div>
                </form>
            </div>
            </div>
    </div>
</section>

<script>
    function addCommas(x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }
    function removeCommas(x) {
        if (x) {
            return x.toString().replace(/,/g, '');
        }
        return '';
    }
    function removeChar(event) {
        event = event || window.event;
        var keyID = (event.which) ? event.which : event.keyCode;
        // 숫자, 백스페이스, 탭, Enter, 방향키, Delete만 허용
        if ((keyID >= 48 && keyID <= 57) || (keyID >= 96 && keyID <= 105) || keyID == 8 || keyID == 46 || keyID == 37 || keyID == 39 || keyID == 9 || keyID == 13) {
            return;
        } else {
            return event.preventDefault();
        }
    }

    // 폼 제출 전 콤마 제거 (Controller가 정수형으로 받기 위함)
    $('form').submit(function() {
        var priceInput = $('#carPrice');
        priceInput.val(removeCommas(priceInput.val()));
    });
    
    // 페이지 로드 시 포커스 아웃 이벤트 강제 발생 (숫자 포맷팅 적용)
    $(document).ready(function() {
        $('#carPrice').trigger('blur');
    });
</script>