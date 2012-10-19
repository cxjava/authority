<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/commons/taglibs.jsp"%>
<div id="changePwdDiv" style="width: 100%; height: 100%;">
	<div id="changePwdToolBarDiv"></div>
	<div id="changePwdFormDiv"></div>
</div>

<script type="text/javascript">
	var userId='${user.userId}';
	yepnope("${ctx}/resources/js/user/changepwd.js");
</script>
