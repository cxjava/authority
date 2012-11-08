<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<link rel="stylesheet" type="text/css" href="${ctx}/resources/extjs/resources/css/ext-all.css" />
<%-- <link id="theme" rel="stylesheet" type="text/css" href="${ctx}/resources/extjs/resources/css/xtheme-gray.css" /> --%>
<script type="text/javascript" src="${ctx}/resources/loader/LAB.min.js"></script>
<script type="text/javascript" >
	$LAB
	.script("${ctx}/resources/extjs/adapter/ext/ext-base.js")
	.script("${ctx}/resources/extjs/ext-all-debug.js")
	.script("${ctx}/resources/extjs/ext-lang-zh_CN.js")
	.script("${ctx}/resources/extjs/ux/ExtMD5.js")
	.script("${ctx}/resources/js/share.js")
	.script("${ctx}/resources/jquery/jquery-1.7.2.min.js")
	.script("${ctx}/resources/jquery/jquery.json-2.2-min.js")
	.script("${ctx}/resources/jquery/jquery.center-min.js")
	.wait(function () {
		ctx = "${ctx}";
		Ext.BLANK_IMAGE_URL = '${ctx}/resources/extjs/resources/images/default/s.gif';
		});
</script>