<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">

</head>
<body>
	<div id="main">
<h1>게시판 <small>수정</small></h1>

<form method="POST" action="/trip/hotdeal/edit" enctype="multipart/form-data">
    <input type="hidden" name="seq" value="${dto.seq}">
    
    <table>
        <tr>
            <th>제목</th>
            <td><input type="text" name="subject" id="subject" required class="full" value="${dto.subject}"></td>
        </tr>
        <tr>
            <th>판매상태</th>
            <td>
                <select name="status">
                    <option value="1" ${dto.status == '1' ? 'selected' : ''}>판매예정</option>
                    <option value="2" ${dto.status == '2' ? 'selected' : ''}>진행중</option>
                    <option value="3" ${dto.status == '3' ? 'selected' : ''}>판매종료</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>카테고리</th>
            <td>
                <select name="category">
                    <option value="1" ${dto.category == '1' ? 'selected' : ''}>잡화</option>
                    <option value="2" ${dto.category == '2' ? 'selected' : ''}>전자기기·액세서리</option>
                    <option value="3" ${dto.category == '3' ? 'selected' : ''}>세면·뷰티</option>
                    <option value="4" ${dto.category == '4' ? 'selected' : ''}>수납·안전용품</option>
                    <option value="5" ${dto.category == '5' ? 'selected' : ''}>e쿠폰·입장권</option>
                    <option value="6" ${dto.category == '6' ? 'selected' : ''}>아웃도어·캠핑</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>내용</th>
            <td><textarea name="content" id="content" required class="full">${dto.content}</textarea></td>
        </tr>
        
        <!-- 기존 이미지 표시 -->
        <tr>
            <th>기존 이미지</th>
            <td>
                <div class="existing-images">
                    <c:forEach var="img" items="${images}">
                        <div class="image-item">
                            <img src="/trip/resources/upload/${img.hotdealImageUrl}" alt="기존 이미지">
                            <input type="checkbox" name="deleteImages" value="${img.hotdealImageId}">
                            <label>삭제</label>
                        </div>
                    </c:forEach>
                    <c:if test="${empty images}">
                        <p>등록된 이미지가 없습니다.</p>
                    </c:if>
                </div>
            </td>
        </tr>
        
        <!-- 새 이미지 추가 -->
        <tr>
            <th>이미지 추가</th>
            <td><input type="file" name="imgs" class="full" accept="image/*" multiple></td>
        </tr>
        
        <tr>
            <th>핫딜아이템 이름</th>
            <td><input type="text" name="itemname" id="itemname" required class="full" value="${dto.itemName}"></td>
        </tr>
        <tr>
            <th>가격</th>
            <td><input type="number" name="price" id="price" required class="full" value="${dto.price}" oninput="this.value = this.value.replace(/[^0-9]/g, '');"></td>
        </tr>
        <tr>
            <th>링크</th>
            <td><input type="text" name="url" id="url" required class="full" value="${dto.url}"></td>
        </tr>
    </table>
    
    <div>
        <button type="button" class="back" onclick="location.href='/trip/hotdeal/view?seq=${dto.seq}';">취소</button>
        <button type="submit" class="add primary">수정</button>
    </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    
</form>

</div>
		
</body>
</html>























