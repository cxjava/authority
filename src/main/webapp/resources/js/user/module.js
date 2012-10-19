Ext.ns("Ext.authority.modules"); // 自定义一个命名空间
module = Ext.authority.modules; // 定义命名空间的别名
module = {
	all : ctx + '/module/all',// 加载所有
	save : ctx + "/module/save",//保存
	del : ctx + "/module/del/",//删除
	MODULEMAP :MODULEMAP,
	pageSize : 20, // 每页显示的记录数
	LEAF : LEAF,
	EXPANDED : EXPANDED,
	ISDISPLAY:ISDISPLAY
};

/** 改变页的combo */
module.pageSizeCombo = new Share.pageSizeCombo({
			value : '20',
			listeners : {
				select : function(comboBox) {
					module.pageSize = parseInt(comboBox.getValue());
					module.bbar.pageSize = parseInt(comboBox.getValue());
					module.store.baseParams.limit = module.pageSize;
					module.store.baseParams.start = 0;
					module.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
module.pageSize = parseInt(module.pageSizeCombo.getValue());
/** 基本信息-数据源 */
module.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : module.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : module.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'results', // 记录总数
				root : 'rows' // Json中的列表数据根节点
			}, ['moduleId', 'moduleName', 'moduleUrl', 'parentId', 'leaf',
					'expanded', 'displayIndex', 'isDisplay', 'iconCss',
					'information']),
			listeners : {
				'load' : function(store, records, options) {
					module.alwaysFun();
				}
			}
		});
module.comboxParent = new Ext.form.ComboBox({
	emptyText : '请选择父模块...',
	fieldLabel : '父模块',
	hiddenName : 'parentId',
	name : 'parentId',
	triggerAction : 'all',
	mode : 'local',
	store : new Ext.data.ArrayStore({
				fields : ['v', 't'],
				data : Share.map2Ary(module.MODULEMAP)
			}),
	valueField : 'v',
	displayField : 't',
	allowBlank : false,
	editable : false,
	anchor : '99%'
});
/** 基本信息-选择模式 */
module.selModel = new Ext.grid.CheckboxSelectionModel({
			singleSelect : true,
			listeners : {
				'rowselect' : function(selectionModel, rowIndex, record) {
					module.deleteAction.enable();
					module.editAction.enable();
				},
				'rowdeselect' : function(selectionModel, rowIndex, record) {
					module.alwaysFun();
				}
			}
		});
/** 基本信息-数据列 */
module.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [module.selModel, {
						hidden : true,
						header : '模块编号',
						dataIndex : 'moduleId'
					}, {
						header : '模块名称',
						dataIndex : 'moduleName'
					}, {
						header : '模块URL',
						dataIndex : 'moduleUrl'
					}, {
						header : '父模块',
						dataIndex : 'parentId',
						renderer : function(v) {
							return Share.map(v, module.MODULEMAP, '');
						}
					}, {
						// (0:树枝节点;1:叶子节点)
						header : '节点类型',
						dataIndex : 'leaf',
						renderer : function(v) {
							return Share.map(v, module.LEAF, '');
						}
					}, {
						// (1:展开;0:收缩)
						header : '展开状态',
						dataIndex : 'expanded',
						renderer : function(v) {
							return Share.map(v, module.EXPANDED, '');
						}
					}, {
						header : '显示顺序',
						dataIndex : 'displayIndex'
					}, {
						header : '是否显示',
						dataIndex : 'isDisplay',
						renderer : function(v) {
							return Share.map(v,module.ISDISPLAY , '');
						}
					}, {
						header : 'CSS样式',
						dataIndex : 'iconCss'
					}, {
						header : '节点说明',
						// id : 'information',
						dataIndex : 'information',
						renderer : function(value, metadata, record) {
							metadata.attr = 'ext:qtitle="' + value + '"' + ' ext:qtip="' + value + '"';
							return value;
						}
					}]
		});
/** 新建 */
module.addAction = new Ext.Action({
			text : '新建',
			iconCls : 'module_add',
			handler : function() {
				module.addWindow.setIconClass('module_add'); // 设置窗口的样式
				module.addWindow.setTitle('新建模块'); // 设置窗口的名称
				module.addWindow.show().center(); // 显示窗口
				module.formPanel.getForm().reset(); // 清空表单里面的元素的值.
				module.comboxParent.clearValue();
			}
		});
/** 编辑 */
module.editAction = new Ext.Action({
			text : '编辑',
			iconCls : 'module_edit',
			disabled : true,
			handler : function() {
				var record = module.grid.getSelectionModel().getSelected();
				module.addWindow.setIconClass('module_edit'); // 设置窗口的样式
				module.addWindow.setTitle('编辑模块'); // 设置窗口的名称
				module.addWindow.show().center();
				module.formPanel.getForm().reset();
				module.formPanel.getForm().loadRecord(record);
			}
		});
/** 删除 */
module.deleteAction = new Ext.Action({
			text : '删除',
			iconCls : 'module_delete',
			disabled : true,
			handler : function() {
				module.delFun();
			}
		});
/** 查询 */
module.searchField = new Ext.ux.form.SearchField({
			store : module.store,
			paramName : 'moduleName',
			emptyText : '请输入模块名称',
			style : 'margin-left: 5px;'
		});
/** 顶部工具栏 */
module.tbar = [module.addAction, '-', module.editAction, '-',
		module.deleteAction, '-', module.searchField];
/** 底部工具条 */
module.bbar = new Ext.PagingToolbar({
			pageSize : module.pageSize,
			store : module.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', module.pageSizeCombo]
		});
/** 基本信息-表格 */
module.grid = new Ext.grid.GridPanel({
			// title : '模块列表',
			store : module.store,
			colModel : module.colModel,
			selModel : module.selModel,
			tbar : module.tbar,
			bbar : module.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'moduleDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});
module.leafCombo = new Ext.form.ComboBox({
			fieldLabel : '节点类型',
			hiddenName : 'leaf',
			name : 'leaf',
			triggerAction : 'all',
			mode : 'local',
			store : new Ext.data.ArrayStore({
						fields : ['v', 't'],
						data : Share.map2Ary(module.LEAF)
					}),
			valueField : 'v',
			displayField : 't',
			allowBlank : false,
			editable : false,
			anchor : '99%'
		});
module.expandedCombo = new Ext.form.ComboBox({
			fieldLabel : '展开状态',
			hiddenName : 'expanded',
			name : 'expanded',
			triggerAction : 'all',
			mode : 'local',
			store : new Ext.data.ArrayStore({
						fields : ['v', 't'],
						data : Share.map2Ary(module.EXPANDED)
					}),
			valueField : 'v',
			displayField : 't',
			allowBlank : false,
			editable : false,
			anchor : '99%'
		});
module.isDisplayradiogroup = new Ext.form.RadioGroup({
			fieldLabel : '是否显示',
			allowBlank : false,
			name : 'isDisplay',
			items : [{
						boxLabel : '是',
						checked : true,
						name : 'isDisplay',
						inputValue : 1
					}, {
						boxLabel : '否',
						name : 'isDisplay',
						inputValue : 0
					}],
			anchor : '99%'
		});
/** 基本信息-详细信息的form */
module.formPanel = new Ext.form.FormPanel({
			frame : false,
			title : '模块信息',
			bodyStyle : 'padding:10px;border:0px',
			labelWidth : 70,
			defaultType : 'textfield',
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'moduleId',
						anchor : '99%'
					}, {
						fieldLabel : '模块名称',
						maxLength : 64,
						allowBlank : false,
						name : 'moduleName',
						anchor : '99%'
					},{
						fieldLabel : '模块URL',
						maxLength : 64,
						// allowBlank : false,
						name : 'moduleUrl',
						anchor : '99%'
					}, module.comboxParent, {
						fieldLabel : '显示顺序',
						xtype : 'numberfield',
						maxLength : 2,
						allowBlank : false,
						name : 'displayIndex',
						anchor : '99%'
					}, module.leafCombo, module.expandedCombo,
					module.isDisplayradiogroup, {
						fieldLabel : 'CSS样式',
						maxLength : 128,
						allowBlank : false,
						name : 'iconCss',
						anchor : '99%'
					}, {
						xtype : 'textarea',
						fieldLabel : '节点说明',
						maxLength : 128,
						height : 100,
						name : 'information',
						anchor : '99%'
					}]
		});
/** 编辑新建窗口 */
module.addWindow = new Ext.Window({
			layout : 'fit',
			width : 500,
			height : 420,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [module.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							module.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = module.formPanel.getForm();
							var id = form.findField("moduleId").getValue();
							form.reset();
							if (id != '')
								form.findField("moduleId").setValue(id);
						}
					}]
		});
module.alwaysFun = function() {
	Share.resetGrid(module.grid);
	module.deleteAction.disable();
	module.editAction.disable();
};
module.saveFun = function() {
	var form = module.formPanel.getForm();
	if (!form.isValid()) {
		return;
	}
	// 发送请求
	Share.AjaxRequest({
				url : module.save,
				params : form.getValues(),
				callback : function(json) {
					module.addWindow.hide();
					module.alwaysFun();
					module.store.reload();
				}
			});
};
module.delFun = function() {
	var record = module.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '你真的要删除选中菜单及其包含的所有子菜单吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : module.del + record.data.moduleId,
								callback : function(json) {
									module.alwaysFun();
									module.store.reload();
								}
							});
				}
			});
};
module.myPanel = new Ext.Panel({
			id : param + '_panel',
			renderTo : param,
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [module.grid]
		});