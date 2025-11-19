<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>여행 후기 작성 ✈️</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/review.css">
</head>

<body>

<div class="review-page">
<div class="container board-form-card">
  <h2>여행 후기 작성 ✈️</h2>

  <form method="post"
        action="${pageContext.request.contextPath}/review/add" enctype="multipart/form-data">

    <label for="reviewBoardTitle">제목</label> <input type="text" id="reviewBoardTitle" name="reviewBoardTitle"
           placeholder="제목을 입력하세요." required>

    <label for="reviewBoardContent">여행 후기 내용</label>
    <textarea id="reviewBoardContent" name="reviewBoardContent" placeholder="여행에 대한 후기를 입력하세요." required></textarea>

    <label for="images">여행 사진 업로드 (여러 장 선택 가능)</label>
    <input type="file" id="images" name="images" multiple accept="image/*">

    <div class="btn-area">
      <button type="submit" class="btn btn-submit">등록하기</button>
      <button type="button" class="btn btn-cancel"
               onclick="location.href='${pageContext.request.contextPath}/review/list'"> 취소
      </button>
    </div>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    
  </form>
</div>
</div>

</body>
</html>