<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<script type="text/javascript" src="${ctx}/resources/loader/yepnope.min.js"></script>
<script type="text/javascript">
	pagesize=eval('(${fields.pagesize==null?"{}":fields.pagesize})');
	yepnope({
			load : [ //extjs
			         "${ctx}/resources/extjs/resources/css/ext-all.css",
			         "${ctx}/resources/extjs/adapter/ext/ext-base.js",
					 "${ctx}/resources/extjs/ext-all.js", 
					 "${ctx}/resources/extjs/ext-lang-zh_CN.js", 
					 "${ctx}/resources/extjs/ux/ExtMD5.js", 
					 "${ctx}/resources/extjs/ux/TabCloseMenu.js",
					 "${ctx}/resources/extjs/ux/SearchField.js",
					 "${ctx}/resources/extjs/ux/ProgressBarPager.js",
					 //通用
					 "${ctx}/resources/js/Ext.ux.override.js",
					 "${ctx}/resources/js/share.js",
					 //jquery
					 "${ctx}/resources/jquery/jquery-1.7.2.min.js", 
					 "${ctx}/resources/jquery/jquery.json-2.2-min.js", 
					 "${ctx}/resources/jquery/jquery.center-min.js",
			         "${ctx}/resources/css/default.css"
					 ],
			complete : function() {
				Ext.QuickTips.init();
				Ext.form.Field.prototype.msgTarget = 'title';//qtip,title,under,side
				Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
				ctx = "${ctx}";
				Ext.BLANK_IMAGE_URL = '${ctx}/resources/extjs/resources/images/default/s.gif';
				}
			});
</script>