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

<div class="routepost-page">
<div class="container">   
  <h2>게시글 수정 ✏️</h2>

  <form method="post"
        action="${pageContext.request.contextPath}/routepost/edit"
        enctype="multipart/form-data">

    <input type="hidden" name="routepostId" value="${dto.routepostId}"/>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <!-- 제목 -->
    <label for="routepostTitle">제목</label>
    <input type="text" id="routepostTitle" name="routepostTitle"
           value="${dto.routepostTitle}" required>

    <!-- 내용 -->
    <label for="routepostContent">내용</label>
    <textarea id="routepostContent" name="routepostContent" rows="8" required>
${dto.routepostContent}</textarea>

    <!-- 만족도 -->
    <label for="routepostSatisfaction">만족도 (0.0 ~ 5.0)</label>
    <input type="number" id="routepostSatisfaction"
           name="routepostSatisfaction" step="0.1" min="0" max="5"
           value="${dto.routepostSatisfaction}" required>

    <!-- 삭제될 이미지 ID 저장 -->
    <input type="hidden" id="deleteImageIds" name="deleteImageIds"/>

    <!-- 기존 이미지 영역 -->
    <label>현재 등록된 이미지</label>
    <c:choose>
      <c:when test="${not empty imageList}">
        <div class="image-preview" style="display:flex; gap:10px; flex-wrap:wrap;">

          <c:forEach var="img" items="${imageList}">
            <div class="img-box"
                 data-id="${img.routepostImageId}"
                 style="position:relative; display:inline-block;">

              <!-- X 버튼 -->
              <span class="btn-delete-img"
                    style="position:absolute; top:-8px; right:-8px; 
                           background:#ff4d4d; color:white; width:20px; height:20px;
                           display:flex; justify-content:center; align-items:center;
                           border-radius:50%; font-size:14px; cursor:pointer;">
                ✕
              </span>

              <!-- 이미지 -->
              <img src="${pageContext.request.contextPath}/upload/routepost/${img.routepostImageUrl}"
                   style="width:120px; height:120px; object-fit:cover; border-radius:8px;">
            </div>
          </c:forEach>

        </div>
      </c:when>
      <c:otherwise>
        <p>등록된 이미지가 없습니다.</p>
      </c:otherwise>
    </c:choose>

    <!-- 새 이미지 등록 -->
    <label>이미지 변경 (여러 장 선택 가능)</label>
    <input type="file" name="images" multiple accept="image/*">

    <!-- 버튼 -->
    <div class="btn-area">
      <button type="submit" class="btn btn-submit">수정 완료</button>
      <button type="button" class="btn btn-cancel"
              onclick="location.href='${pageContext.request.contextPath}/routepost/view/${dto.routepostId}'">
        취소
      </button>
    </div>

  </form>
</div>
</div>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script>
// 이미지 삭제(X 버튼)
$(document).on("click", ".btn-delete-img", function () {

    const imgBox = $(this).closest(".img-box");
    const imgId = imgBox.data("id");

    // 화면에서 제거
    imgBox.remove();

    // 삭제될 이미지 ID 기록
    let deleteIds = $("#deleteImageIds").val();

    if (!deleteIds) {
        $("#deleteImageIds").val(imgId);
    } else {
        $("#deleteImageIds").val(deleteIds + "," + imgId);
    }
});
</script>

</body>
</html>
