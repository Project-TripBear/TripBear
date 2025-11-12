<%-- 파일 경로: /WEB-INF/views/layout/popup_layout.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><tiles:insertAttribute name="title" /></title>
    
    <%-- 팝업 전용 CSS (findboard.css) 로드 --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/findboard.css"> 

</head>
<body>
    <%-- tiles_admin.xml에서 정의한 content (report.jsp)가 여기에 삽입됩니다. --%>
    <tiles:insertAttribute name="content" />
</body>
</html>