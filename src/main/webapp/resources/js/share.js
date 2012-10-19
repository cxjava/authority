var Share = {};
/**
 * 处理超时错误
 */
Ext.Ajax.on('requestcomplete', function(conn, response, options) {
	if (typeof response.getResponseHeader == "function") {
		var _time;
		// 得到response里面的头信息
		// response.setHeader("session-status","timeout");
		var sessionStatus = response.getResponseHeader("session-status");
		if (sessionStatus == "timeout") { // timeout
			var win = window;
			while (win != win.parent) {
				win = win.parent;
			}
			_time = setTimeout(function() {
				win.location = ctx;
			}, 5 * 1000); // 5秒钟
			Ext.Msg.alert('提示', '<span style="color:red"><b>登录超时,请重新登录!</b></span>', function() {
				if (_time != null) {
					clearTimeout(_time);
				}
				win.location = ctx;
			});
			return false;
		}
		// 服务器出错
		if (sessionStatus == "pagenotfind") { // pagenotfind
			Ext.Msg.alert('提示', '<span style="color:red"><b>很抱歉，您要访问的页面不存在！</b></span>', function() {
			});
			return false;
		}
	}
}, this);

/**
 * Ajax请求方法
 * 
 * @param {Object}
 *            settings.params 参数对象，必须
 * @param {String}
 *            settings.url 请求地址，必须
 * @param {Function}
 *            settings.callback 成功后回调方法，必须
 * @param {boolean}
 *            settings.showMsg 处理成功时，是否显示提示信息 true:显示 false:不显示
 * @param {boolean}
 *            settings.showWaiting 是否显示等待条 true:显示 false:不显示
 * @param {Number}
 *            settings.timeout 超时时间
 * @param {String}
 *            settings.successMsg 成功消息
 * @param {String}
 *            settings.failureMsg 失败消息
 */
Share.AjaxRequest = function(settings) {
	// 参数对象
	var params = settings.params === undefined ? {} : settings.params, showWaiting = settings.showWaiting === undefined ? true
			: settings.showWaiting, showMsg = settings.showMsg === undefined ? true : settings.showMsg, timeout = settings.timeout === undefined ? 60 * 1000
			: settings.timeout,
	// 发送请求
	waiting = null;
	if (showWaiting) {
		waiting = Ext.Msg.wait('正在处理，请稍等...', '', '');
	}
	Ext.Ajax.request({
		url : settings.url,
		params : params,
		timeout : timeout,
		success : function(response, options) {
			if (waiting != null) {
				waiting.hide();
			}
			var json = Ext.decode(response.responseText);
			if (json.success == true) {
				if (showMsg == true) { // 显示提示信息
					// 请求成功时的提示文字
					var successMsg = '操作成功.';
					if (json.msg && json.msg != '') {
						successMsg = json.msg;
					} else if (settings.successMsg && settings.successMsg != '') {
						successMsg = settings.successMsg;
					}
					Ext.Msg.alert('提示', successMsg, function() {
						if (settings.callback) { // 回调方法
							settings.callback(json);
						}
					});
				} else {
					if (settings.callback) { // 回调方法
						settings.callback(json);
					}
				}
			} else if (json.success == false) {
				var message = '发生异常.';
				if (json.msg && json.msg != '') { // 后台设定的业务消息
					message = json.msg;
				} else if (settings.failureMsg && settings.failureMsg != '') { // 前台指定的错误信息
					message = settings.failureMsg;
				}
				if (json.exceptionMessage && json.exceptionMessage != '') { // 有异常信息
					Share.ExceptionWindow(message, json.exceptionMessage);
				} else {
					Ext.Msg.alert('错误', message, function() {
						if (typeof settings.falseFun == "function") {// 失败后想做的个性化操作函数
							settings.falseFun(json);
						}
					});
				}
			} else if (typeof response.getResponseHeader == "function") {
				// 得到response里面的头信息
				// response.setHeader("session-status","timeout");
				var sessionStatus = response.getResponseHeader("session-status");
				if (sessionStatus == "timeout") { // timeout
					Ext.Msg.alert('提示', '<span style="color:red"><b>登录超时,请重新登录!</b></span>', function() {
						var win = window;
						while (win != win.parent) {
							win = win.parent;
						}
						win.location = ctx;
					});
					return false;
				}
				// 服务器出错
				if (sessionStatus == "pagenotfind") { // pagenotfind
					Ext.Msg.alert('提示', '<span style="color:red"><b>很抱歉，您要访问的页面不存在！</b></span>', function() {
					});
					return false;
				}
				if (settings.callback) { // 回调方法
					settings.callback(json);
				}
			}
		},
		failure : function(response, options) {
			if (waiting != null) {
				waiting.hide();
			}
			Share.ExceptionWindow('错误：' + response.status + ' ' + response.statusText, response.responseText);
		}
	});
};

/**
 * 显示异常信息的窗口
 * 
 * @param message
 *            异常信息
 * @param exceptionMsg
 *            异常详细信息
 */
Share.ExceptionWindow = function(message, exceptionMessage) {
	var _message = message === undefined ? '发生异常!' : message == '' ? '发生异常!' : message, _exceptionMessage = exceptionMessage === undefined ? '发生异常!'
			: exceptionMessage == '' ? '发生异常!' : exceptionMessage, _exceptionWindow = Ext.getCmp('_exceptionWindow');
	if (!_exceptionWindow) {
		_exceptionWindow = new Ext.Window({
			id : '_exceptionWindow',
			title : '错误',
			width : 400,
			autoHeight : true,
			modal : true,
			layout : 'fit',
			items : [ new Ext.form.Label({
				html : '<div style="padding:5px;">' + _message + '</div>'
			}), new Ext.Panel({
				title : '详细信息',
				// bodyStyle:'overflow-x:visible;overflow-y:scroll;',
				// bodyStyle:'overflow-x:hidden;;overflow-y:scroll;',
				collapsible : true,
				collapsed : true,
				autoScroll : true,
				height : 200,
				html : _exceptionMessage
			}) ]
		}).show();
	} else {
		_exceptionWindow.show();
	}
};

/**
 * map转数组。
 * 
 * @param {Map}map
 *            map对象
 * @return 数组
 */
Share.map2Ary = function(map) {
	var list = [];
	for ( var key in map) {
		list.push([ key, map[key] ]);
	}
	return list;
};
/**
 * 获取map中的值。
 * 
 * @param {String}value
 *            要渲染的值
 * @param {Map}mapping
 *            Map对象
 * @param {String}defaultText
 *            没有对应的key时的默认值
 * @return {String}对应的value值
 */
Share.map = function(value, mapping, defaultText) {
	return mapping[value] || (defaultText === undefined || defaultText === null ? value : defaultText);
};
/**
 * 设置系统主题
 */
Share.swapStyle = function(themeCss) {
	// Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
	// 设置主题
	var theme = themeCss === undefined ? Ext.state.Manager.get('Cxjava_theme') : themeCss;
	if (theme && theme != '') {
		// 实际改变风格样式的处理
		Ext.util.CSS.swapStyleSheet('theme', ctx + '/resources/extjs/resources/css/' + theme);
		Ext.state.Manager.set('Cxjava_theme', theme);
	} else {
		Ext.util.CSS.swapStyleSheet('theme', ctx + '/resources/extjs/resources/css/xtheme-gray.css');
		Ext.state.Manager.set('Cxjava_theme', 'xtheme-gray.css');
	}
};
/**
 * 打开tab
 * 
 * @param {Object}node
 *            结点对象
 * @param {String}url
 *            链接
 * @param {String}iconCls
 *            样式
 */
Share.openTab = function(node, url, parentTab, iconCls) {
	var id = node.id, title = node.text,
	// 设置在哪个元素打开
	tabPanel = parentTab === undefined ? index.tabPanel : parentTab, css = iconCls === undefined ? node.attributes.iconCls : iconCls;
	// 参数验证
	if (!tabPanel || title == "" || url == "" || id == "") {
		Ext.Msg.alert("错误", "参数错误.", function() {
			return false;
		});
		return;
	}
	var url = encodeURI(encodeURI(url)),
	// tab页不存在的场合
	tab = tabPanel.get(id), autoLoad = {
		url : url,
		params : {
			id : id + "_div"
		},
		method : "GET",
		scripts : true,
		// scope : this,
		nocache : true
	};
	if (!tab) {
		var newTab = {
			id : id,
			title : title,
			iconCls : css,
			closable : true,
			autoScroll : true,
			autoLoad : autoLoad,
			listeners : {
				activate : function(Panel) {
					// 自动调节高度和宽度
					var inPanel = Ext.getCmp(Panel.id + "_div_panel");
					if (inPanel) {
						inPanel.doLayout(true, true);
						inPanel.setHeight(index.tabPanel.getInnerHeight() - 1);
						inPanel.setWidth(Panel.getWidth());
					}
				}
			}
		};
		tabPanel.add(newTab).show();
		// 限制最多能开10个tab
		if (tabPanel.items.length > 10) {
			var firstTab = tabPanel.get(tabPanel.items.items[1]);
			if (firstTab) {
				tabPanel.remove(firstTab);
			}
		}
	} else {
		// tab页已经存在的场合
		tab.getUpdater().update(autoLoad);
		tabPanel.setActiveTab(tab);
	}
}/** 得到最顶层的window对象 */
Share.getWin = function() {
	var win = window;
	while (win != win.parent) {
		win = win.parent;
	}
	return win;
}/** 清空选择的grid */
Share.resetGrid = function(grid) {
	// 清空选中的记录
	grid.getSelectionModel().clearSelections();
	grid.getEl().select('div.x-grid3-hd-checker').removeClass('x-grid3-hd-checker-on');
}/** 问候 */
Share.sayHello = function() {
	var hour = new Date().getHours(), hello = '';
	if (hour < 6) {
		hello = '凌晨好';
	} else if (hour < 9) {
		hello = '早上好';
	} else if (hour < 12) {
		hello = '上午好';
	} else if (hour < 14) {
		hello = '中午好';
	} else if (hour < 17) {
		hello = '下午好';
	} else if (hour < 19) {
		hello = '傍晚好';
	} else if (hour < 22) {
		hello = '晚上好';
	} else {
		hello = '夜里好';
	}
	return hello + '！';
}/** 每页显示条数下拉选择框 */
Share.pageSizeCombo = Ext.extend(Ext.form.ComboBox, {
	name : 'pagesize',
	triggerAction : 'all',
	mode : 'local',
	store : new Ext.data.ArrayStore({
		fields : [ 'value', 'text' ],
		data : Share.map2Ary(pagesize)
	// 见views/commons/yepnope.jsp
	}),
	valueField : 'value',
	displayField : 'text',
	value : '10',
	editable : false,
	width : 85
});
/**
 * 扩展基础类 判断以什么结尾
 */
String.prototype.endsWith = function(suffix) {
	return this.indexOf(suffix, this.length - suffix.length) !== -1;
};
/**
 * 扩展基础类 得到字符串的长度，包括中文和英文
 */
String.prototype.charlen = function() {
	var arr = this.match(/[^\x00-\xff]/ig);
	return this.length + (arr == null ? 0 : arr.length);
}
/**
 * 扩展基础类 字符串首尾去空格
 */
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
}

/**
 * 扩展基础类 字符串包含字符串判断
 */
String.prototype.contains = function(sub) {
	return this.indexOf(sub) != -1;
}
Date.prototype.format = function(format) {
	// author: meizz
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		// millisecond
		"S" : this.getMilliseconds()
	};
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
	return format;
};