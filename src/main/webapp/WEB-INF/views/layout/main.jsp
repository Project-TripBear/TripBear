<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- <link rel="stylesheet" href="http://bit.ly/3WJ5ilK" /> -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" />
</head>
<body>
	<!-- main.jsp  -->
	<tiles:insertAttribute name="header" />
	
	<tiles:insertAttribute name="content" />
	
	<script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
	
</body>
</html>