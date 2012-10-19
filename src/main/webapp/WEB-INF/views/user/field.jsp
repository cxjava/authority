<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/commons/taglibs.jsp"%>
<div id="${param.id}"></div>

<script type="text/javascript">
var param='${param.id}',
ENABLED = eval('(${fields.enabled==null?"{}":fields.enabled})');
yepnope("${ctx}/resources/js/user/field.js");
</script>
