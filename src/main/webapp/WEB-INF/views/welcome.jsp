<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/commons/taglibs.jsp"%>
<div id="home" align="center" style="height: 100%; width: 100%;">欢迎，${user.realName }</div>
<input type="button" value="上传" id="_uploads"/>
<script type="text/javascript">
yepnope({
	load : ["${ctx}/resources/swfupload/css/icons.css",
	        "${ctx}/resources/swfupload/swfupload.js",
	        "${ctx}/resources/swfupload/uploaderPanel.js"],
	complete : function() {
		$("#_uploads").click(function(){
		//使用实例
		new Ext.Window({
					width : 650,
					title : '上传示例',
					height : 300,
					layout : 'fit',
					items : [{
								xtype : 'SWFUploader',
								border : false,
								fileSize : 1024 * 550,// 限制文件大小550MB
								uploadUrl : ctx + '/fileupload',
								flashUrl : ctx + '/resources/swfupload/swfupload.swf',
								filePostName : 'file', // 后台接收参数
								fileTypes : '*.log',// 可上传文件类型
								postParams : {
									name : "chenxin"
								}
							}]
				}).show();
		});
	}
});
</script>