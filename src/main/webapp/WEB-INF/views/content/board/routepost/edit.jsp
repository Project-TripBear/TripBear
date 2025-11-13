<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>게시글 수정 ✏️</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/routepost.css">
</head>

<body>

<div class="container">
  <h2>게시글 수정 ✏️</h2>

  <!-- Spring MVC 기반 수정 폼 -->
  <form method="post"
        action="${pageContext.request.contextPath}/routepost/edit"
        enctype="multipart/form-data">

    <input type="hidden" name="routepostId" value="${dto.routepostId}"/>

    <!-- 제목 -->
    <label for="routepostTitle">제목</label>
    <input type="text" id="routepostTitle" name="routepostTitle"
           value="${dto.routepostTitle}" required>

    <!-- 루트 선택 -->
    <label for="route_id">루트 선택</label>
    <select id="route_id" name="routeId" required>
      <option value="">-- 여행 루트를 선택하세요 --</option>
      <option value="1">서울 → 강릉 1박 2일 루트</option>
      <option value="2">부산 해운대 당일치기 루트</option>
      <option value="3">제주도 2박 3일 루트</option>
    </select>

    <!-- 내용 -->
    <label for="routepostContent">내용</label>
    <textarea id="routepostContent" name="routepostContent" rows="8" required>
${dto.routepostContent}</textarea>

    <!-- 만족도 -->
    <label for="routepostSatisfaction">만족도 (0.0 ~ 5.0)</label>
    <input type="number" id="routepostSatisfaction"
           name="routepostSatisfaction" step="0.1" min="0" max="5"
           value="${dto.routepostSatisfaction}" required>

    <!-- 기존 이미지 -->
    <label>현재 등록된 이미지</label>
    <c:choose>
      <c:when test="${not empty imageList}">
        <div class="image-preview">
          <c:forEach var="img" items="${imageList}">
            <img src="${pageContext.request.contextPath}/asset/upload/routepost/${img.routepostImageUrl}"
                 alt="기존 이미지">
          </c:forEach>
        </div>
      </c:when>
      <c:otherwise>
        <p>등록된 이미지가 없습니다.</p>
      </c:otherwise>
    </c:choose>

    <!-- 새 이미지 업로드 -->
    <label>이미지 변경 (여러 장 선택 가능)</label>
    <input type="file" name="images" multiple accept="image/*">

    <!-- 버튼 영역 -->
    <div class="btn-area">
      <button type="submit" class="btn btn-submit">수정 완료</button>
      <button type="button" class="btn btn-cancel"
              onclick="location.href='${pageContext.request.contextPath}/routepost/view/${dto.routepostId}'">
        취소
      </button>
    </div>
  </form>
</div>

</body>
</html>
