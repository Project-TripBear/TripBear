<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>게시글 수정 ✏️</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/review.css">
</head>

<body>

<div class="container">
  <h2>게시글 수정 ✏️</h2>

  <form method="post"
        action="${pageContext.request.contextPath}/review/edit" enctype="multipart/form-data">

    <input type="hidden" name="reviewPostId" value="${dto.reviewPostId}"/>

    <label for="reviewBoardTitle">제목</label>
    <input type="text" id="reviewBoardTitle" name="reviewBoardTitle" value="${dto.reviewBoardTitle}" required> <label for="reviewBoardContent">내용</label>
    <textarea id="reviewBoardContent" name="reviewBoardContent" rows="8" required> ${dto.reviewBoardContent}</textarea> <label>현재 등록된 이미지</label>
    <c:choose>
      <c:when test="${not empty imageList}">
        <div class="image-preview">
          <c:forEach var="img" items="${imageList}">
            <img src="${pageContext.request.contextPath}/asset/upload/review/${img.reviewImageUrl}
                 alt="기존 이미지">
          </c:forEach>
        </div>
      </c:when>
      <c:otherwise>
        <p>등록된 이미지가 없습니다.</p>
      </c:otherwise>
    </c:choose>

    <label>이미지 변경 (여러 장 선택 가능)</label>
    <input type="file" name="images" multiple accept="image/*">

    <div class="btn-area">
      <button type="submit" class="btn btn-submit">수정 완료</button>
      <button type="button" class="btn btn-cancel"
              onclick="location.href='${pageContext.request.contextPath}/review/view/${dto.reviewPostId}'"> 취소
      </button>
    </div>
  </form>
</div>

</body>
</html>