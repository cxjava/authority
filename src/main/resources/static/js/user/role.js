Ext.ns("Ext.authority.role"); // 自定义一个命名空间
role = Ext.authority.role; // 定义命名空间的别名
role = {
	all : ctx + 'role',// 加载所有
	save : ctx + "role/save",// 保存
	del : ctx + "role/del/",// 删除
	allModules : ctx + "module",// 系统菜单
	saveModules : ctx + "module/saverole",// 保存角色与系统菜单
	myModules : ctx + "module/",
	childNodes : '',
	pageSize : 20
	// 每页显示的记录数
};
/** 改变页的combo */
role.pageSizeCombo = new Share.pageSizeCombo({
			value : '20',
			listeners : {
				select : function(comboBox) {
					role.pageSize = parseInt(comboBox.getValue());
					role.bbar.pageSize = parseInt(comboBox.getValue());
					role.store.baseParams.limit = role.pageSize;
					role.store.baseParams.start = 0;
					role.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
role.pageSize = parseInt(role.pageSizeCombo.getValue());
/** 基本信息-数据源 */
role.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
//			baseParams : {
//				start : 0,
//				limit : role.pageSize
//			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : role.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'results', // 记录总数
				root : 'rows' // Json中的列表数据根节点
			}, ['id', 'roleName', 'roleDesc']),
			listeners : {
				'load' : function(store, records, options) {
					role.alwaysFun();
				}
			}
		});
/** 基本信息-选择模式 */
role.selModel = new Ext.grid.CheckboxSelectionModel({
			singleSelect : true,
			listeners : {
				'rowselect' : function(selectionModel, rowIndex, record) {
					role.deleteAction.enable();
					role.editAction.enable();
				},
				'rowdeselect' : function(selectionModel, rowIndex, record) {
					role.alwaysFun();
				}
			}
		});
/** 基本信息-数据列 */
role.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [role.selModel, {
						hidden : true,
						header : '角色ID',
						dataIndex : 'id'
					}, {
						header : '角色名称',
						dataIndex : 'roleName'
					}, {
						header : '角色描述',
						// id : 'roleDesc',
						dataIndex : 'roleDesc'
					}]
		});
/** 新建 */
role.addAction = new Ext.Action({
			text : '新建',
			iconCls : 'role_add',
			handler : function() {
				role.addWindow.setIconClass('role_add'); // 设置窗口的样式
				role.addWindow.setTitle('新建角色'); // 设置窗口的名称
				role.addWindow.show().center(); // 显示窗口
				role.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			}
		});
/** 编辑 */
role.editAction = new Ext.Action({
			text : '编辑',
			iconCls : 'role_edit',
			disabled : true,
			handler : function() {
				var record = role.grid.getSelectionModel().getSelected();
				role.addWindow.setIconClass('role_edit'); // 设置窗口的样式
				role.addWindow.setTitle('编辑角色'); // 设置窗口的名称
				role.addWindow.show().center();
				role.formPanel.getForm().reset();
				role.formPanel.getForm().loadRecord(record);
			}
		});
/** 删除 */
role.deleteAction = new Ext.Action({
			text : '删除',
			iconCls : 'role_delete',
			disabled : true,
			handler : function() {
				role.delFun();
			}
		});
/** 查询 */
role.searchField = new Ext.ux.form.SearchField({
			store : role.store,
			paramName : 'roleName',
			emptyText : '请输入角色名称',
			style : 'margin-left: 5px;'
		});
/** 顶部工具栏 */
role.tbar = [role.addAction, '-', role.editAction, '-', role.deleteAction, '-',
		role.searchField];
/** 底部工具条 */
role.bbar = new Ext.PagingToolbar({
			pageSize : role.pageSize,
			store : role.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', role.pageSizeCombo]
		});
/** 基本信息-表格 */
role.grid = new Ext.grid.GridPanel({
	title : '角色列表',
	store : role.store,
	colModel : role.colModel,
	selModel : role.selModel,
	tbar : role.tbar,
	bbar : role.bbar,
	autoScroll : 'auto',
	region : 'center',
	loadMask : true,
	// autoExpandColumn :'roleDesc',
	stripeRows : true,
	listeners : {
		'cellclick' : function(obj, rowIndex, columnIndex, e) {
			var record = role.grid.getSelectionModel().getSelected();
			if (record) {
				role.treePanel.enable();
				// 先清空已选择的状态
				role.clearTreeNodeCheckFun(role.treePanel.root, false);
				// 当前选择用户的资源信息
				Share.AjaxRequest({
							url : role.myModules + record.data.id,
							callback : function(json) {
								// 勾选资源树的checkbox
								if (json.length > 0) {
									for (var i = 0; i < json.length; i++) {
										moduleId = json[i].moduleId;
										var treeNode = role.treePanel
												.getNodeById("_authority_"
														+ moduleId);
										if (treeNode)
											treeNode.getUI().checkbox.checked = true;
									}
								}
							}
						});
			}
		}
	},
	viewConfig : {}
});
/** 基本信息-详细信息的form */
role.formPanel = new Ext.form.FormPanel({
			frame : false,
			title : '角色信息',
			bodyStyle : 'padding:10px;border:0px',
			labelWidth : 80,
			defaultType : 'textfield',
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'id',
						anchor : '99%'
					}, {
						fieldLabel : '角色名称',
						maxLength : 64,
						allowBlank : false,
						name : 'roleName',
						anchor : '99%'
					}, {
						xtype : 'textarea',
						fieldLabel : '角色描述',
						maxLength : 128,
						height : 100,
						name : 'roleDesc',
						anchor : '99%'
					}]
		});
/** 编辑新建窗口 */
role.addWindow = new Ext.Window({
			layout : 'fit',
			width : 500,
			height : 250,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [role.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							role.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = role.formPanel.getForm();
							var id = form.findField("id").getValue();
							form.reset();
							if (id != '')
								form.findField("id").setValue(id);
						}
					}]
		});
role.alwaysFun = function() {
	Share.resetGrid(role.grid);
	role.deleteAction.disable();
	role.editAction.disable();
	role.saveMudulesAction.disable();
	role.clearTreeNodeCheckFun(role.treePanel.root, false);// 取消选择
	role.treePanel.setDisabled(true);// 设置树为无效
};
role.saveFun = function() {
	var form = role.formPanel.getForm();
	if (!form.isValid()) {
		return;
	}
	// 发送请求
	Share.AjaxRequest({
				url : role.save,
				params : form.getValues(),
				callback : function(json) {
					role.addWindow.hide();
					role.alwaysFun();
					role.store.reload();
				}
			});
};
role.delFun = function() {
	var record = role.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '确定要删除这条记录吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : role.del + record.data.id,
								callback : function(json) {
									role.alwaysFun();
									role.store.reload();
								}
							});
				}
			});
};

/** 父节点的选择 */
role.checkParentFun = function(treeNode) {
	var i;
	var check = false;
	var nocheck = false;
	if (treeNode.hasChildNodes()) {
		for (i = 0; i < treeNode.childNodes.length; i++) {
			if (treeNode.childNodes[i].getUI().checkbox.checked) {
				check = true;
			} else {
				nocheck = true;
			}
		}
	}
	if (check == true && nocheck == false) {// 可以全选
		treeNode.getUI().checkbox.checked = true;
	} else if (check == true && nocheck == true) {// 半选
		treeNode.getUI().checkbox.checked = true;
		// treeNode.getUI().iconNode.src =
		// '${ctx}/resources/extjs/ux/images/part.gif'
	} else if (check == false && nocheck == true) {// 全不选
		treeNode.getUI().checkbox.checked = false;
	}
};
/** 先清空已选择的状态 */
role.clearTreeNodeCheckFun = function(treeNode, checked) {
	var i;
	if (treeNode.hasChildNodes()) {
		for (i = 0; i < treeNode.childNodes.length; i++) {
			if (treeNode.childNodes[i].getUI().checkbox) {
				treeNode.childNodes[i].getUI().checkbox.checked = checked;
			}
		}
		for (i = 0; i < treeNode.childNodes.length; i++) {
			role.clearTreeNodeCheckFun(treeNode.childNodes[i], checked);
		}
	}
};
role.visitAllTreeNodeFun = function(treeNode) {
	var i;
	if (treeNode.hasChildNodes()) {
		for (i = 0; i < treeNode.childNodes.length; i++) {
			if (treeNode.childNodes[i].getUI().checkbox) {
				if (treeNode.childNodes[i].getUI().checkbox.checked) {
					// 去除前缀
					role.childNodes += treeNode.childNodes[i].id.replace(
							"_authority_", "")
							+ ',';
				}
			}
		}
		for (i = 0; i < treeNode.childNodes.length; i++) {
			role.visitAllTreeNodeFun(treeNode.childNodes[i]);
		}
	}
};
/** 保存角色的系统资源 */
role.saveMudulesAction = new Ext.Action({
			text : '保存',
			iconCls : 'save',
			disabled : true,
			handler : function() {
				role.childNodes = "";
				role.visitAllTreeNodeFun(role.treePanel.root);// 得到选择的菜单
				if (role.childNodes == "") {
					Ext.Msg.alert("提示", "请至少为此角色分配一个系统菜单!");
					return;
				}
				var record = role.grid.getSelectionModel().getSelected();
				var params = {
					'moduleIds' : role.childNodes,
					'roleId' : record.data.id
				};
				// 发送请求
				Share.AjaxRequest({
							url : role.saveModules,
							params : params,
							callback : function(json) {
								role.alwaysFun();
								role.store.reload();
							}
						});

			}
		});
role.treePanel = new Ext.tree.TreePanel({
			title : '当前角色可访问的资源',
			region : 'east',
			split : true,
			minSize : 200,
			maxSize : 900,
			useArrows : true,
			autoScroll : true,
			width : '50%',
			tbar : [role.saveMudulesAction, '-'],
			animate : true,
			enableDD : true,
			containerScroll : true,
			rootVisible : false,
			buttonAlign : 'left',
			frame : false,
			disabled : true,
			root : {
				nodeType : 'async'
			},
			dataUrl : role.allModules,
			listeners : {
				'checkchange' : function(node, checked) {
					// 保存按钮生效
					role.saveMudulesAction.enable();
					if (node.childNodes.length > 0) {
						for (var i = 0; i < node.childNodes.length; i++) {
							if (node.childNodes[i].getUI().checkbox) {
								node.childNodes[i].getUI().checkbox.checked = checked;
							}
						}
					}
					if (node.parentNode
							&& node.parentNode.getUI().checkbox != null) {
						role.checkParentFun(node.parentNode);
					}
				}
			}
		});
role.myPanel = new Ext.Panel({
			id : param + '_panel',
			renderTo : param,
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [role.grid, role.treePanel]
		});