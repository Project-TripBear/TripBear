<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>여행 추천 루트 게시글 작성 ✈️</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/routepost.css">
</head>

<body>
<%@ include file="/WEB-INF/views/inc/header.jsp" %>

<div class="container">
  <h2>여행 추천 루트 게시글 작성 ✈️</h2>

  <form method="post"
        action="${pageContext.request.contextPath}/routepost/add"
        enctype="multipart/form-data">

    <!-- 제목 -->
    <label for="routepostTitle">제목</label>
    <input type="text" id="routepostTitle" name="routepostTitle"
           placeholder="제목을 입력하세요." required>

    <!-- 루트 선택 -->
    <!-- <label for="routeId">루트 선택</label>
    <select id="routeId" name="routeId" required>
      <option value="">-- 여행 루트를 선택하세요 --</option>
      <option value="1">서울 → 강릉 1박 2일 루트</option>
      <option value="2">부산 해운대 당일치기 루트</option>
      <option value="3">제주도 2박 3일 루트</option>
    </select> -->

    <!-- 내용 -->
    <label for="routepostContent">여행 후기 내용</label>
    <textarea id="routepostContent" name="routepostContent"
              placeholder="여행에 대한 후기를 입력하세요." required></textarea>

    <!-- 만족도 -->
    <label for="routepostSatisfaction">만족도 (0.0 ~ 5.0)</label>
    <input type="number" id="routepostSatisfaction" name="routepostSatisfaction"
           min="0" max="5" step="0.1" value="5.0" required>

    <!-- 이미지 업로드 -->
    <label for="images">여행 사진 업로드 (여러 장 선택 가능)</label>
    <input type="file" id="images" name="images" multiple accept="image/*">

    <!-- 버튼 영역 -->
    <div class="btn-area">
      <button type="submit" class="btn btn-submit">등록하기</button>
      <button type="button" class="btn btn-cancel"
              onclick="location.href='${pageContext.request.contextPath}/routepost/list'">
        취소
      </button>
    </div>
  </form>
</div>

</body>
</html>
