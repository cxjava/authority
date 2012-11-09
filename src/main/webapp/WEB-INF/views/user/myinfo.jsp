<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/commons/taglibs.jsp"%>
<div id="editUserDiv" style="width: 100%; height: 100%;">
	<div id="editUserToolBarDiv"></div>
	<div id="editUserFormDiv""></div>
</div>

<script type="text/javascript">
	Ext.ns("Ext.authority.myinfo"); // 自定义一个命名空间
	myinfo = Ext.authority.myinfo; // 定义命名空间的别名
	myinfo = {
		save : ctx + "/user/myinfo",
		THEME: eval('(${fields.theme==null?"{}":fields.theme})'),//注意括号
		SEX: eval('(${fields.sex==null?"{}":fields.sex})')//注意括号
	};
	myinfo.themeComboBox = new Ext.form.ComboBox({
		hiddenName : 'theme',
		fieldLabel : '风格',
		store : new Ext.data.ArrayStore({
			fields : ['v', 't'],
			data : Share.map2Ary(myinfo.THEME)
		}),
		mode : 'local',
		triggerAction : "all",
		valueField : 'v',
		displayField : 't',
		editable : false,
		anchor : '99%',
		listeners : {
			collapse : function() {
				Share.swapStyle(this.value);
			}
		}
	});
	myinfo.setTheme = function() {
		var theme = Ext.state.Manager.get('Cxjava_theme');
		if (theme && theme != '') {
			myinfo.themeComboBox.setValue(theme);
		} else {
			myinfo.themeComboBox.setValue('xtheme-gray.css');
		}
	};
	myinfo.setTheme();
	/* myinfo.sexradiogroup = new Ext.form.RadioGroup({
		fieldLabel : '性别',
		allowBlank : false,
		name : 'sex',
		value : '${user.sex}',
		items : [ {
			boxLabel : '男',
			name : 'sex',
			checked : true,
			inputValue : 0
		}, {
			boxLabel : '女',
			name : 'sex',
			inputValue : 1
		} ],
		anchor : '99%'
	}); */
	myinfo.sexCombo = new Ext.form.ComboBox({
		fieldLabel : '性别',
		hiddenName : 'sex',
		name : 'sex',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(myinfo.SEX)
				}),
		valueField : 'v',
		displayField : 't',
		value : '${user.sex}',
		allowBlank : false,
		editable : false,
		anchor : '99%'
	});
	// 编辑用户Form
	myinfo.formPanel = new Ext.form.FormPanel({
		renderTo : 'editUserFormDiv',
		frame : false,
		bodyStyle : 'padding:10px;border:0px',
		labelWidth: 70,
		defaultType : 'textfield',
		items : [ {
			xtype : 'hidden',
			fieldLabel : 'ID',
			value : '${user.id}',
			name : 'id'
		}, {
			fieldLabel : '用户姓名',
			maxLength : 64,
			allowBlank : false,
			value : '${user.realName}',
			name : 'realName',
			anchor : '99%'
		}, myinfo.sexCombo, {
			fieldLabel : '电子邮件',
			maxLength : 64,
			allowBlank : false,
			regex : /^[a-zA-Z0-9_\.\-]+\@([a-zA-Z0-9\-]+\.)+[a-zA-Z0-9]{2,4}$/,
			regexText : '请输入有效的邮箱地址',
			value : '${user.email}',
			name : 'email',
			anchor : '99%'
		}, {
			fieldLabel : '手机',
			maxLength : 32,
			allowBlank : false,
			value : '${user.mobile}',
			name : 'mobile',
			anchor : '99%'
		}, {
			fieldLabel : '办公电话',
			maxLength : 32,
			allowBlank : false,
			value : '${user.officePhone}',
			name : 'officePhone',
			anchor : '99%'
		}, myinfo.themeComboBox, {
			xtype : 'textarea',
			fieldLabel : '备注',
			maxLength : 128,
			height : 90,
			value : '${user.remark}',
			name : 'remark',
			anchor : '99%'
		} ]
	});
	// 工具栏
	myinfo.toolbar = new Ext.Toolbar({
		renderTo : 'editUserToolBarDiv',
		items : [ new Ext.Button({
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var form = myinfo.formPanel.getForm();
				if (form.isValid()) {
					var values = form.getValues();
					if (values.theme) {
						delete values.theme;
					}
					Share.AjaxRequest({
						url : myinfo.save,
						params : values,
						callback : function(json) {
							// 重新登录
							Share.getWin().location = ctx;
						}
					});
				}
			}
		}), new Ext.Button({
			text : '取消',
			iconCls : 'cancel',
			handler : function() {
				header.myInfoWindow.close();
			}
		}) ]
	});
</script>
