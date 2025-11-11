<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><tiles:getAsString name="title" ignore="true"/></title>
</head>
<body>

    <!-- Header 영역 -->
    <tiles:insertAttribute name="header" />

    <!-- 본문(content) 영역 -->
    <tiles:insertAttribute name="content" />

</body>
</html>
