<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/commons/taglibs.jsp"%>
<div id="${param.id}"></div>

<script type="text/javascript">
	var param='${param.id}',
	SEX = eval('(${fields.sex==null?"{}":fields.sex})');
	yepnope("${ctx}/resources/js/user/user.js");
</script>
