$(document).ready(function() {
Ext.ns("Ext.authority.findpassword"); // 自定义一个命名空间
findpwd = Ext.authority.findpassword; // 定义命名空间的别名
findpwd = {
	findurl : ctx + "findpwd"
};
// 编辑用户Form
findpwd.findPwdFormPanel = new Ext.form.FormPanel({
	renderTo : 'findPwdFormDiv',
	border : false,
	labelWidth : 70,
	defaults : {
		xtype : "textfield"
	},
	bodyStyle : 'padding:5px;background-color:transparent;',
	items : [ {// 帐号
		fieldLabel : '账号',
		emptyText:'请输入帐号',
		maxLength : 64,
		allowBlank : false,
		name : 'account',
		id:'findpwd_account',
		anchor : '99%'
	}, { // 注册邮箱
		fieldLabel : '注册邮箱',
		maxLength : 64,
		allowBlank : false,
		regex : /^[a-zA-Z0-9_\.\-]+\@([a-zA-Z0-9\-]+\.)+[a-zA-Z0-9]{2,4}$/,
		regexText : '请输入有效的邮箱地址',
		emptyText:'请输入有效的邮箱地址',
		name : 'email',
		id:'findpwd_email',
		anchor : '99%'
	}, {
		fieldLabel : '验证码',
		maxLength : 6,
		emptyText:'验证码不区分大小写',
		allowBlank : false,
		name : 'captcha',
		id:'findpwd_captcha',
		anchor : '99%'
	}, {
		xtype : 'box', //或者xtype: 'component',  
		width : 195, //图片宽度  
		height : 40, //图片高度 
		style : 'margin-left: 75px',
		id:'_checkimage',
		autoEl : {
			tag : 'img', //指定为img标签
			alt : "验证码",
			title : '看不清楚？点击获得新图片。',
			onclick : "javacript:$(this).hide().attr('src', ctx + '/checkimage.jpg?r=' + Math.random()).fadeIn();",
			src : ctx + '/checkimage.jpg?r=' + Math.random() //指定url路径  
		}
	} ]
});
// 工具栏
findpwd.findPwdToolbar = new Ext.Toolbar({
	renderTo : 'findPwdToolBarDiv',
	items : [ new Ext.Button({
		text : '发送',
		iconCls : 'send',
		handler : function() {
			var form = findpwd.findPwdFormPanel.getForm();
			if (form.isValid()) {
				var values = form.getValues();
				// 发送请求
				Share.AjaxRequest({
					url : findpwd.findurl,
					params : values,
					callback : function(json) {
						// 关闭窗口
						login.findPwdWindow.close();
					},
					falseFun : function(json) {//失败后想做的个性化操作函数
						if (json.msg.indexOf('验证码已经失效') > -1) {
							$("#_checkimage").click();
							$("#findpwd_captcha").focus().val('');
							return;
						}
						if (json.msg.indexOf('验证码输入不正确') > -1) {
							$("#findpwd_captcha").val('').focus();
							//$("#findpwd_captcha").select().val('');
							//$("#findpwd_captcha").focus().val('');
							return;
						}
						if (json.msg.indexOf('请输入正确的帐号和其注册邮箱') > -1) {
							$("#_checkimage").click();
							$("#findpwd_captcha").val('');
							$("#findpwd_email").val('');
							$("#findpwd_account").val('').focus();
							return;
						}
					}
				});
			}
		}
	}), new Ext.Button({
		text : '取消',
		iconCls : 'cancel',
		handler : function() {
			login.findPwdWindow.close();
		}
	}) ]
});
var events = "beforecopy beforepaste beforedrag contextmenu selectstart drag paste copy cut dragenter";
$("#findpwd_account").bind(events, function(e) {
	if (e && e.preventDefault)
		e.preventDefault();
	else
		window.event.returnValue = false;
	return false;
});
$("#findpwd_email").bind(events, function(e) {
	if (e && e.preventDefault)
		e.preventDefault();
	else
		window.event.returnValue = false;
	return false;
});
$("#findpwd_captcha").bind(events, function(e) {
	if (e && e.preventDefault)
		e.preventDefault();
	else
		window.event.returnValue = false;
	return false;
});
});