<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/commons/taglibs.jsp"%>
<div id="${param.id}"></div>

<script type="text/javascript">
var param='${param.id}',
MODULEMAP =eval('(${moduleMap})'),
LEAF = eval('(${fields.leaf==null?"{}":fields.leaf})'),
EXPANDED = eval('(${fields.expanded==null?"{}":fields.expanded})'),
ISDISPLAY=eval('(${fields.isdisplay==null?"{}":fields.isdisplay})');
yepnope("${ctx}/resources/js/user/module.js");
</script>
