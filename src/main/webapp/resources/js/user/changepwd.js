$(document).ready(function() {
	Ext.ns("Ext.authority.changepassword"); // 自定义一个命名空间
	changepwd = Ext.authority.changepassword; // 定义命名空间的别名
	changepwd = {
		changeurl : ctx + "/user/changepwd"
	};
	// 编辑用户Form
	changepwd.changePwdFormPanel = new Ext.form.FormPanel({
		renderTo : 'changePwdFormDiv',
		border : false,
		labelWidth : 70,
		defaults : {
			xtype : "textfield"
		},
		bodyStyle : 'padding:5px;background-color:transparent;',
		items : [ {// 原密码
			id : 'old_password',
			name : 'old_password',
			inputType : "password",
			fieldLabel : '原密码',
			anchor : '99%',
			allowBlank : false,
			maxLength : 32
		}, { // 新密码
			id : 'new_password',
			name : 'new_password',
			inputType : "password",
			fieldLabel : '新密码',
			anchor : '99%',
			allowBlank : false,
			maxLength : 32
		}, {// 确认密码
			id : 'compare_password',
			name : 'compare_password',
			inputType : "password",
			fieldLabel : '确认密码',
			vtype : "password",// 自定义的验证类型
			vtypeText : "两次输入的密码不一致！",
			confirmTo : "new_password",// 要比较的另外一个的组件的id
			anchor : '99%',
			allowBlank : false,
			maxLength : 32
		}, {// 账户ID
			xtype : 'hidden',
			name : 'userId',
			value : userId
		} ]
	});
	// 工具栏
	changepwd.changePwdToolbar = new Ext.Toolbar({
		renderTo : 'changePwdToolBarDiv',
		items : [ new Ext.Button({
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var form = changepwd.changePwdFormPanel.getForm();
				if (form.isValid()) {
					var values = form.getValues();
					// 发送请求
					Share.AjaxRequest({
						url : changepwd.changeurl,
						params : {
							oldPassword : Ext.MD5(values.old_password),
							newPassword : Ext.MD5(values.new_password),
							comparePassword : Ext.MD5(values.compare_password),
							userId : values.userId
						},
						callback : function(json) {
							// 重新登录
							Share.getWin().location = ctx;
						},
						falseFun : function(json) {//失败后想做的个性化操作函数
							if (json.msg.indexOf('原密码不正确！请重新输入') > -1) {
								$("#old_password").focus().val('');
								return;
							}
							if (json.msg.indexOf('两次输入的新密码不一致') > -1) {
								$("#new_password").val('');
								$("#compare_password").val('').focus();
								return;
							}
							if (json.msg.indexOf('请输入正确的帐号和其注册邮箱') > -1) {
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
				header.changePwdWindow.close();
			}
		}) ]
	});
	var events = "beforecopy beforepaste beforedrag contextmenu selectstart drag paste copy cut dragenter";
	$("#old_password").bind(events, function(e) {
		if (e && e.preventDefault)
			e.preventDefault();
		else
			window.event.returnValue = false;
		return false;
	});
	$("#new_password").bind(events, function(e) {
		return false;
	});
	$("#compare_password").bind(events, function(e) {
		return false;
	});
});