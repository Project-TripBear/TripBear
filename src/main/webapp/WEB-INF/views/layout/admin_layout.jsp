<%-- 파일 경로: /WEB-INF/views/layout/admin_layout.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><tiles:insertAttribute name="title" /></title>

    <%@ include file="/WEB-INF/views/inc/admin_asset.jsp" %> 
    
    <link rel="stylesheet" 
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" 
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" 
          crossorigin="anonymous">
    
</head>
<body>
    <tiles:insertAttribute name="header" /> 
    
    <div id="wrapper">
        <div id="content-area">
            <tiles:insertAttribute name="content" />
        </div>
    </div>
</body>
</html>