// 파일 경로: admin_layout.jsp

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><tiles:insertAttribute name="title" /></title>

    <%-- 🚩 [수정] admin_asset.jsp의 내용을 여기에 포함시켜 모든 CSS/JS가 로드되도록 합니다. --%>
    <%@ include file="/WEB-INF/views/inc/admin_asset.jsp" %> 
    
    <%-- Bootstrap은 그대로 유지 --%>
    <link rel="stylesheet" 
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" 
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" 
          crossorigin="anonymous">
    
    <%-- 🚨 기존 <link rel="stylesheet" href="<c:url value='/resources/css/admin.css'/>">는 삭제합니다. --%>
    
</head>
<body>
    <tiles:insertAttribute name="header" /> 
    
    <div id="wrapper">
        <tiles:insertAttribute name="sidebar" />
        <div id="content-area">
            <tiles:insertAttribute name="content" />
        </div>
    </div>
</body>
</html>