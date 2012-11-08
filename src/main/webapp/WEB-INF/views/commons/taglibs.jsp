<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="user" value="${sessionScope.CURRENT_USER}" />
<c:set var="fields" value="${applicationScope.fields}" />
<link rel="shortcut icon" href="${ctx }/resources/images/favicon.ico" type="image/x-icon">
<link rel="icon" href="${ctx }/resources/images/favicon.ico" type="image/x-icon">