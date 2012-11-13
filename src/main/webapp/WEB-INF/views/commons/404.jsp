<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/WEB-INF/views/commons/taglibs.jsp"%>
<title>404错误: 找不到页</title>
</head>
<body>
	<div align="center" style="vertical-align: middle;">
		<img src="${ctx}/resources/images/404.png" alt="404错误: 找不到页面" width="500" height="450" border="0" usemap="#Map">
		<map name="Map">
			<area shape="rect" coords="300,365,350,400" href="${ctx}/" target="_self">
		</map>
	</div>
</body>
</html>