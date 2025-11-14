<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
</head>
<body>
	<div class="page-board-edit-container"> 
	<div id="main">
        <h1>게시판 <small>수정</small></h1>
        
        <form method="POST" action="/trip/hotdeal/edit" enctype="multipart/form-data" class="write-form">
            <input type="hidden" name="seq" value="${dto.seq}">
            
            <table class="vertical form-input-table">
                <tr>
                    <th>제목</th>
                    <td><input type="text" name="subject" required class="full form-control" value="${dto.subject}"></td>
                </tr>
                <tr>
                    <th>내용</th>
                    <td><textarea name="content" required class="full form-control">${dto.content}</textarea></td>
                </tr>
                <tr> 
                    <th>기존 이미지</th>
                    <td>
                        <div class="existing-images-group"> 
                            <c:forEach var="img" items="${images}">
                                <div class="image-item-edit"> <img src="/trip/resources/upload/${img.hotdealImageUrl}" alt="기존 이미지" class="existing-thumb"> <label class="delete-label">
                                        <input type="checkbox" name="deleteImages" value="${img.hotdealImageId}">
                                        삭제
                                    </label>
                                </div>
                            </c:forEach>
                            <c:if test="${empty images}">
                                <p class="no-image-message">등록된 이미지가 없습니다.</p> 
                            </c:if>
                        </div>
                    </td>
                </tr>                 
                <tr>
                    <th>이미지 추가</th>
                    <td><input type="file" name="imgs" class="full form-control-file" accept="image/*" multiple></td>
                </tr> <tr>
                    <th>핫딜아이템 이름</th>
                    <td><input type="text" name="itemname" id="itemname" required class="full form-control" value="${dto.itemName}"></td>
                </tr>
                <tr>
                    <th>가격</th>
                    <td><input type="number" name="price" id="price" required class="full form-control" value="${dto.price}"> 원</td>
                </tr>
                <tr>
                    <th>링크</th>
                    <td><input type="text" name="url" id="url" required class="full form-control" value="${dto.url}"></td>
                </tr>
                                
                </table>
            
            <div class="action-buttons-group"> <button type="button" class="btn btn-secondary" onclick="location.href='/trip/hotdeal/view?seq=${dto.seq}';">취소</button> <button type="submit" class="btn btn-primary">수정</button> </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        </form>
    </div>
</div>		
</body>
</html>






















