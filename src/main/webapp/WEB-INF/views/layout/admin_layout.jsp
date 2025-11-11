<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><tiles:insertAttribute name="title" /></title>

    <link rel="stylesheet" 
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" 
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" 
          crossorigin="anonymous">
    
    <link rel="stylesheet" href="<c:url value='/resources/css/admin.css'/>">
    
</head>
<body>
    <%-- 🚨 Header는 wrapper 바깥에 한 번만 삽입합니다. --%>
    <tiles:insertAttribute name="header" /> 
    
    <div id="wrapper">
        <tiles:insertAttribute name="sidebar" />
        <div id="content-area">
            <tiles:insertAttribute name="content" />
        </div>
    </div>
</body>