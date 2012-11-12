<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/commons/taglibs.jsp"%>
<div id="${param.id}"></div>

<script type="text/javascript">
	var param = '${param.id}';
	yepnope({
		load : ["${ctx}/resources/extjs/ux/treegrid/treegrid.css",
		         "${ctx}/resources/extjs/ux/treegrid/TreeGridSorter.js",
		         "${ctx}/resources/extjs/ux/treegrid/TreeGridColumnResizer.js",
		         "${ctx}/resources/extjs/ux/treegrid/TreeGridNodeUI.js",
		         "${ctx}/resources/extjs/ux/treegrid/TreeGridLoader.js",
		         "${ctx}/resources/extjs/ux/treegrid/TreeGridColumns.js",
		         "${ctx}/resources/extjs/ux/treegrid/TreeGrid.js"]
	});
	yepnope.injectJs("${ctx}/resources/js/user/role.js");
</script>
