<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/commons/taglibs.jsp"%>
<div id="findPwdDiv" style="width: 100%; height: 100%;">
	<div id="findPwdToolBarDiv"></div>
	<div id="findPwdFormDiv"></div>
</div>
<style type="text/css">
.cancel {
	background-image: url(${ctx}/resources/images/icons/cancel.png) !important;
	background-repeat: no-repeat;
}
.send {
	background-image: url(${ctx}/resources/images/icons/arrow_right.png) !important;
	background-repeat: no-repeat;
}
</style>
<script type="text/javascript">
yepnope.injectJs("${ctx}/resources/js/user/findpwd.js");
</script>
