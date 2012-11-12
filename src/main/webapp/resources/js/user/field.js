Ext.ns("Ext.authority.field"); // 自定义一个命名空间
field = Ext.authority.field; // 定义命名空间的别名
field = {
	all : ctx + '/field/all',// 加载所有
	save : ctx + "/field/save",//保存
	del : ctx + "/field/del/",//删除
	synchro : ctx + "/field/synchro",//内存同步
	ENABLED : ENABLED,
	pageSize : 20 // 每页显示的记录数
};
/** 改变页的combo */
field.pageSizeCombo = new Share.pageSizeCombo({
			value : '20',
			listeners : {
				select : function(comboBox) {
					field.pageSize = parseInt(comboBox.getValue());
					field.bbar.pageSize = parseInt(comboBox.getValue());
					field.store.baseParams.limit = field.pageSize;
					field.store.baseParams.start = 0;
					field.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
field.pageSize = parseInt(field.pageSizeCombo.getValue());
/** 基本信息-数据源 */
field.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : field.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : field.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'results', // 记录总数
				root : 'rows' // Json中的列表数据根节点
			}, ['id', 'fieldName', 'field', 'valueField', 'displayField',
					'enabled', 'sort']),
			listeners : {
				'load' : function(store, records, options) {
					field.alwaysFun();
				}
			}
		});
/** 基本信息-选择模式 */
field.selModel = new Ext.grid.CheckboxSelectionModel({
			singleSelect : true,
			listeners : {
				'rowselect' : function(selectionModel, rowIndex, record) {
					field.deleteAction.enable();
					field.editAction.enable();
				},
				'rowdeselect' : function(selectionModel, rowIndex, record) {
					field.alwaysFun();
				}
			}
		});
/** 基本信息-数据列 */
field.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [field.selModel, {
						hidden : true,
						header : '字段ID',
						dataIndex : 'id'
					}, {
						header : '字段',
						dataIndex : 'field'
					}, {
						header : '字段名称',
						dataIndex : 'fieldName'
					}, {
						header : '字段值',
						dataIndex : 'valueField'
					}, {
						header : '字段显示值',
						dataIndex : 'displayField'
					}, {
						// (0:禁用;1启用)
						header : '是否启用',
						dataIndex : 'enabled',
						renderer : function(v) {
							return Share.map(v,field.ENABLED , '');
						}
					}, {
						header : '显示顺序',
						dataIndex : 'sort'
					}]
		});
/** 新建 */
field.addAction = new Ext.Action({
			text : '新建',
			iconCls : 'field_add',
			handler : function() {
				field.addWindow.setIconClass('field_add'); // 设置窗口的样式
				field.addWindow.setTitle('新建字段'); // 设置窗口的名称
				field.addWindow.show().center(); // 显示窗口
				field.formPanel.getForm().reset(); // 清空表单里面的元素的值.
				field.enabledCombo.clearValue();
			}
		});
/** 编辑 */
field.editAction = new Ext.Action({
			text : '编辑',
			iconCls : 'field_edit',
			disabled : true,
			handler : function() {
				var record = field.grid.getSelectionModel().getSelected();
				field.addWindow.setIconClass('field_edit'); // 设置窗口的样式
				field.addWindow.setTitle('编辑字段'); // 设置窗口的名称
				field.addWindow.show().center();
				field.formPanel.getForm().reset();
				field.formPanel.getForm().loadRecord(record);
			}
		});
/** 删除 */
field.deleteAction = new Ext.Action({
			text : '删除',
			iconCls : 'field_delete',
			disabled : true,
			handler : function() {
				field.delFun();
			}
		});
/** 查询 */
field.searchField = new Ext.ux.form.SearchField({
			store : field.store,
			paramName : 'fieldName',
			emptyText : '请输入字段名称',
			style : 'margin-left: 5px;'
		});
/** 内存同步 */
field.synchroAction = new Ext.Action({
			text : '内存同步',
			iconCls : 'synchro',
			handler : function() {
				Ext.Msg.confirm('提示', '确定要对数据进行内存同步操作吗?', function(btn, text) {
					if (btn == 'yes') {
						// 发送请求
						Share.AjaxRequest({
									url : field.synchro,
									callback : function(json) {
									}
								});
					}
				});
			}
		});
/** 提示 */
field.tips = '&nbsp;<font color="red"><b>提示:维护字段后必须执行内存同步</b></font>';
/** 顶部工具栏 */
field.tbar = [field.addAction, '-', field.editAction, '-',
		field.deleteAction, '-', field.searchField,'-',field.synchroAction,field.tips];
/** 底部工具条 */
field.bbar = new Ext.PagingToolbar({
			pageSize : field.pageSize,
			store : field.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', field.pageSizeCombo]
		});
/** 基本信息-表格 */
field.grid = new Ext.grid.GridPanel({
			store : field.store,
			colModel : field.colModel,
			selModel : field.selModel,
			tbar : field.tbar,
			bbar : field.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'fieldDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});
field.enabledCombo = new Ext.form.ComboBox({
			fieldLabel : '是否启用',
			hiddenName : 'enabled',
			name : 'enabled',
			triggerAction : 'all',
			mode : 'local',
			store : new Ext.data.ArrayStore({
						fields : ['v', 't'],
						data : Share.map2Ary(field.ENABLED)
					}),
			valueField : 'v',
			displayField : 't',
			allowBlank : false,
			editable : false,
			anchor : '99%'
		});
/** 基本信息-详细信息的form */
field.formPanel = new Ext.form.FormPanel({
			frame : false,
			title : '字段信息',
			bodyStyle : 'padding:10px;border:0px',
			labelWidth : 80,
			defaultType : 'textfield',
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'id',
						anchor : '99%'
					}, {
						fieldLabel : '字段',
						maxLength : 64,
						allowBlank : false,
						name : 'field',
						anchor : '99%'
					},{
						fieldLabel : '字段名称',
						maxLength : 128,
						allowBlank : false,
						name : 'fieldName',
						anchor : '99%'
					},{
						fieldLabel : '字段值',
						maxLength : 128,
						allowBlank : false,
						name : 'valueField',
						anchor : '99%'
					},{
						fieldLabel : '字段显示值',
						maxLength : 128,
						allowBlank : false,
						name : 'displayField',
						anchor : '99%'
					}, field.enabledCombo, {
						fieldLabel : '显示顺序',
						xtype : 'numberfield',
						maxLength : 2,
						allowBlank : false,
						name : 'sort',
						anchor : '99%'
					}]
		});
/** 编辑新建窗口 */
field.addWindow = new Ext.Window({
			layout : 'fit',
			width : 400,
			height : 280,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [field.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							field.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = field.formPanel.getForm();
							var id = form.findField("id").getValue();
							form.reset();
							if (id != '')
								form.findField("id").setValue(id);
						}
					}]
		});
field.alwaysFun = function() {
	Share.resetGrid(field.grid);
	field.deleteAction.disable();
	field.editAction.disable();
};
field.saveFun = function() {
	var form = field.formPanel.getForm();
	if (!form.isValid()) {
		return;
	}
	// 发送请求
	Share.AjaxRequest({
				url : field.save,
				params : form.getValues(),
				callback : function(json) {
					//field.addWindow.hide();
					field.alwaysFun();
					field.store.reload();
				}
			});
};
field.delFun = function() {
	var record = field.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '确定要删除这条记录吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : field.del + record.data.id,
								callback : function(json) {
									field.alwaysFun();
									field.store.reload();
								}
							});
				}
			});
};
field.myPanel = new Ext.Panel({
			id : param + '_panel',
			renderTo : param,
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [field.grid]
		});