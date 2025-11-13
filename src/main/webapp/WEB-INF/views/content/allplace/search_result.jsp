<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- 
  [Tiles Content]
  컨트롤러로부터 'placeList' (List<PlaceDTO>)와 'keyword' (String)를 전달받습니다.
--%>

<%-- (삭제) <head> ... <style> ... </style> </head> --%>

<div class="search-result-container">
    <h2>'${keyword}' 검색 결과</h2>
    <p>총 ${placeList.size()}개의 결과가 있습니다.</p>
    
    <hr>
    
    <div class="search-grid">
    <c:choose>
        <c:when test="${not empty placeList}">
            <c:forEach var="place" items="${placeList}">
                <div class="place-card">

                   
 <%-- --- [핵심 수정] --- --%>
                    <%-- (기존) /allplace/detail/${place.placeId} --%>
                    <%-- (변경) /allplace/view/${place.placeApiId} --%>
                    <a href="${pageContext.request.contextPath}/allplace/view/${place.placeApiId}" style="text-decoration: none; color: inherit;">
                    <%-- --- [수정 
끝] --- --%>
                    
                        <c:choose>
                            <c:when test="${not empty place.placeMainImageUrl}">
       
                
     <img src="${place.placeMainImageUrl}" alt="${place.name}">
                            </c:when>
                            <c:otherwise>
     
                       <img src="/resources/img/default_image.png" alt="기본 이미지">
      
                      </c:otherwise>
                        </c:choose>
      
              
                        <div class="place-card-content">
         
                   <h3><c:out value="${place.name}" /></h3>
               
         <p><c:out value="${place.address}" /></p>
                        </div>
                    </a>
         
       </div>
         
   </c:forEach>
            </c:when>
            <c:otherwise>
                <p>검색 결과가 없습니다.</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>