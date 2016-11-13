Ext.ns('Ext.ux.grid'); // 声明命名空间
/**
 * 功能增强的列表，可以一下实现的功能 可配置的列表项 可配置自动生成表单 双击会弹出便捷编辑条 拥有本地记录功能，可以进行翻页状态记录
 * 可以装载记录的checkbox状态 可以默认根据列表生成添加，修改，删除的表单，并且可以进行配置
 *
 * @class Ext.ux.grid.CollectGrid
 * @extends Ext.grid.GridPanel
 */
Ext.ux.grid.CollectGrid = Ext.extend(Ext.grid.GridPanel, {

        // 配置列表checkbox是翻页是否进行记录
        keepSelectedOnPaging : false,
        // 用于记录选中状态的数组
        recordIds : null,
        // 默认的主键
        idColName : '',
        // 转载数据的url，当不配置url的时候，可以在外层自己配置store进行加载，如果配置url代表需要远程加载数据
        url : '',
        // 是否有序列号
        rowNumber : true,
        // 是否加入checkbox
        checkBox : true,
        // 列表的配置数据
        CM_JR_Record : null,
        // 是否加入分页工具条
        pagingBar : true,
        //分页记录数
        pageSize : 20,
        //分页条的关闭tabpanel按钮
        pageCloseButton : true,
        //自定义参数
        selfParams : null,
        // 是否有工具栏项
        toolbar : true,
        // 是否加入面罩效果
        loadMask : true,

        toolbarConfig : null,
        //是否进行汇总
        isSummary:false,

        //加入列表斑马线
        stripeRows:true,
        /*
         * 是否为单行选择
         */
    isSingleSelect : false,
        //是否启用后台排序
    isRemoteSort : true,
    //清除CheckboxSelectionModel的表头样式
    clearCheckBoxHeader : false,
        sortType : 'ASC',
        /*
         * 查询条件 added by gaojun
         */
        filterValue : "",
        filterTxt : "",
        dateFilter : "",
        endDate : "",
        startDate : "",
        sortField : "",
        showGridData : true,
        /*
         * 用于处理列表分页栏中的刷新按钮中响应方法，
         * 如果需要在刷新时根据工具中查询条件过滤信息，将查询方法赋值给属性即可
         * by gaojun
         * */
        doRefresh : null,

        // 是否支持分页删除，默认为false by gaojun
        enablePageRemove : false,

        /**
         * 使用RadioboxSelectionModel，当单选时可将属性设置为true by gaojun
         * @type Boolean
         */
        radiobox : false,
        viewConfig : {
                forceFit : true
        },
        // 分页工具栏通过ajax的方式去获得数据
        pagingConfig : {
                displayInfo : true,
                displayMsg : '当前 {0} - {1} 总计: {2}&nbsp;&nbsp;',
                emptyMsg : '<b>0</b> '
        },
        /**
         * 对extjs的底层数据进行重载
         */
        initComponent : function() {
                this.recordIds=[];
                Ext.applyIf(this, {
                                        urlAddData : "addData.do",
                                        urlDeleteData : "deleteData.do",
                                        urlUpdateData : "updateData.do",
                                        urlLoadData : this.url
                                });

                if (this.CM_JR_Record) {
                        this.init_SM_CM_DS();
                }
                if (this.toolbar) {
                        this.init_Toolbar();
                }
                if (this.pagingBar) {
                        this.init_PagingBar();
                }
                if (this.keepSelectedOnPaging) {
                        this.init_OnPaging();
                }
                this.setupAltAKey();
                //判断列表是否加载数据
                if(this.showGridData){
                        if (this.pagingBar) {
                                this.refresh(); // modified by gaojun
                        } else {
                                this.store.load();
                        }
                }
//              this.setBaseParams(); // 设置store的beforeLoad事件 added by gaojun
                Ext.ux.grid.CollectGrid.superclass.initComponent.call(this);
        },

        /**
         * 初始化列表栏
         */
        init_SM_CM_DS : function() {

                if(this.isSummary){
                        this.plugins=new Ext.ux.grid.GridSummary();
                }
                var gCm = new Array();
                var gRecord = new Array();

                if (this.rowNumber) {
                        gCm[gCm.length] = new Ext.grid.RowNumberer({header:"序号",width:35,align:"center"});
                }

                var grid = this;
                if(this.radiobox){
                        var sm = new Ext.grid.RadioboxSelectionModel({
                                listeners : {
                                        selectionchange : grid.selectionChangeAction.createDelegate(this)
                                }
                        });
                        gCm[gCm.length] = sm;
                        this.selModel = sm;
                }else if(this.checkBox) {
                        var checkboxSelMdelConfig={
                                handleMouseDown : Ext.emptyFn,
                                singleSelect :this.isSingleSelect,
                                listeners : {
                                        selectionchange : grid.selectionChangeAction.createDelegate(this)
                                }
                        };
                        if(this.clearCheckBoxHeader){
                                Ext.apply(checkboxSelMdelConfig,{
                                        header : ''
                                })
                        }
                        var sm = new Ext.grid.CheckboxSelectionModel(checkboxSelMdelConfig);
                        gCm[gCm.length] = sm;
                        this.selModel = sm;
                }

                for (var i = 0; i < this.CM_JR_Record.length; i++) {
                        var g = this.CM_JR_Record[i];
                        if (g.gridShow || g.gridShow == 'undefined' || g.gridShow == null) {
                                if (g.isProgress) {
                                        var pc = new Ext.ux.grid.ProgressColumn({
                                                                header : g.header,
                                                                dataIndex : g.dataIndex,
                                                                width : g.width,
                                                                textPst : '%', // string added to the end of
                                                                actionEvent : 'click', // click event (defaults
                                                                colored : true, // True for pretty colors, false
                                                                editor : new Ext.form.TextField()
                                                        });
                                        gCm[gCm.length] = pc;
                                } else {
                                        gCm[gCm.length] = g;
                                }
                        }
                        gRecord[gRecord.length] = {
                                name : g.dataIndex,
                                type : g.type || 'string'
                        }
                }

                this.cm = new Ext.grid.ColumnModel(gCm);

                //如果url不为空，则远程从数据库加载，如果url为空，则必须要从外侧配置store对象
                if(this.url){
                        // 创建json数据集
                        if(this.sortField != ''){
                                this.store = new Ext.data.Store({
                                        remoteSort : this.isRemoteSort, // 使用后台排序 by gaojun
                                        sortInfo : {
                                                                field : this.sortField,
                                                                direction : this.sortType
                                                        },
                                        proxy : new Ext.data.HttpProxy({
                                                                url : this.url,
                                                                method : 'post'
                                                        }),
                                        reader : new Ext.data.JsonReader({
                                                                totalProperty : 'totalProperty',
                                                                root : 'result'
                                                        }, Ext.data.Record.create(gRecord))
                                });
                        }else{
                                this.store = new Ext.data.Store({
                                        remoteSort : this.isRemoteSort, // 使用后台排序 by gaojun
                                        proxy : new Ext.data.HttpProxy({
                                                                url : this.url,
                                                                method : 'post'
                                                        }),
                                        reader : new Ext.data.JsonReader({
                                                                totalProperty : 'totalProperty',
                                                                root : 'result'
                                                        }, Ext.data.Record.create(gRecord))

                                });
                        }
                        this.pagingConfig.store = this.store;
                        this.setBaseParams();
                }
        },


        /**
         * 获得查询条件值，需要进行重载 adde by gaojun
         */
        doFilter : function() {
                this.store.baseParams = {
                        filterValue : '',
                        filterTxt : '',
                        dateFilter : '',
                        startDate : '',
                        endDate : ''
                };
        },
        /**
         * 查询时，设置grid store的参数 added by gaojun
         */
        setBaseParams : function() {
                        this.store.on('beforeload', function() {
                                if(this.filterValue==''&& this.filterTxt==''
                                        && this.dateFilter==''&& this.startDate==''
                                        &&this.endDate==''&& this.sortField==''){
                                                this.doFilter();
                                }else{
                                        Ext.apply(this.store.baseParams,{
                                                filterValue : this.filterValue,
                                                filterTxt : this.filterTxt,
                                                dateFilter : this.dateFilter,
                                                startDate : this.startDate,
                                                endDate : this.endDate,
                                                sort : this.sortField,
                                                dir : this.sortType
                                        });
                                }
                                if(this.selfParams){
                                        Ext.apply(this.store.baseParams,this.selfParams);
                                }
                        }.createDelegate(this));
                },

        /**
         * 对工具条进行初始化
         *
         */
        init_Toolbar : function() {
                if(this.toolbarConfig!=null){
                        for (var i = 0; i < this.toolbarConfig.length; i++) {
                                var g = this.toolbarConfig[i];
                                //增加按钮权限控制
                                var result = false;
                                if(g.itemEName != undefined && g.itemEName != 'undefined' && g.itemEName != ''){
                                    for(x=0;x<personButtonArray.length;x++){
                                        if(g.itemEName==personButtonArray[x]){
                                                result = true;
                                        }
                                    }
                                }else{
                                        result=true;
                                }
                                // 增加buttonType属性配置工具栏按钮的生成方式 by gaojun
                                if(result == true ||result == 'true'){
                                                if (Ext.util.JSON.encode(g.id).substring(1, 4) == 'add' || g.buttonType == 'add') {
                                                        this.toolbarConfig[i] = {
                                                                id : g.id,
                                                                text : g.text ? g.text : '新增',
                                                                tooltip : g.tooltip ? g.tooltip : '新增数据',
                                                                iconCls : g.iconCls ? g.iconCls : 'add',
                                                                handler : this.addData.createDelegate(this)
                                                        }
                                                } else if (Ext.util.JSON.encode(g.id).substring(1, 5) == 'save' || g.buttonType == 'save') {
                                                                this.toolbarConfig[i] = {
                                                                        id : g.id,
                                                                        text : g.text ? g.text : '保存',
                                                                        tooltip : g.tooltip ? g.tooltip : '保存数据',
                                                                        iconCls : g.iconCls ? g.iconCls : 'save',
                                                                        handler : this.onSave,
                                                                        scope : this
                                                                }
                                                } else if (Ext.util.JSON.encode(g.id).substring(1, 7) == 'update' || g.buttonType == 'update') {
                                                        this.toolbarConfig[i] = {
                                                                id : g.id,
                                                                text : g.text ? g.text : '修改',
                                                                tooltip : g.tooltip ? g.tooltip : '修改数据',
                                                                iconCls : g.iconCls ? g.iconCls : 'edit',
                //                                              ref : '../updateButton',
                                                                handleNumType : 'single',
                                                                disabled : true,
                                                                handler : this.updateData.createDelegate(this)
                                                        }
                                                        this.updateButton=this.toolbarConfig[i];
                                                } else if (Ext.util.JSON.encode(g.id).substring(1, 7) == 'select' || g.buttonType == 'select') {
                                                        this.toolbarConfig[i] = {
                                                                id : g.id,
                                                                text : g.text ? g.text : '高级查询',
                                                                tooltip : g.tooltip ? g.tooltip : '高级查询',
                                                                iconCls : g.iconCls ? g.iconCls : 'select',
                                                                handler : this.selectData.createDelegate(this)
                                                        }
                                                } else if (Ext.util.JSON.encode(g.id).substring(1, 7) == 'delete' || g.buttonType == 'delete') {
                                                        this.toolbarConfig[i] = {
                                                                id : g.id,
                                                                text : g.text ? g.text : '删除',
                                                                tooltip : g.tooltip ? g.tooltip : '删除数据',
                                                                iconCls : g.iconCls ? g.iconCls : 'delete',
                //                                              ref : '../removeButton',
                                                                disabled : true,
                                                                handleNumType : 'multitude',
                                                                handler : this.deleteData.createDelegate(this)
                                                        }
                                                        this.removeButton=this.toolbarConfig[i];
                                                } else if(g.handleNumType){
                                                        g.disabled = true;
                                                        this.toolbarConfig[i] = g;
                                                } else {
                                                        this.toolbarConfig[i] = g
                                                }
                                }else {
                                                this.toolbarConfig[i] = ""
                                }
                        }
                }
                /**
                 * update by esky 2010-09-06
                 * 修改：添加工具栏折叠功能
                 * 其他：编辑和删除按钮变灰条件判断由原来的ref方式改为使用getCmp获取button
                 */
                this.tbar = new Ext.Toolbar({
            enableOverflow: true,
            items:this.toolbarConfig
                });
        },

        setupAltAKey : function() {
                this.on('afterrender', function() {
                                        this.body.on('keyup', function(e) {
                                                                if (e.getKey() == 65 && e.altKey) {
                                                                        this.addData();
                                                                }
                                                        }, this);
                                }, this);
        },

        // 增加接口
        addData : function() {
        },

        // 更新接口
        updateData : function() {
                        this.addData();
        },

        // 查询接口
        selectData : function() {
        },
        // 删除记录的功能
        deleteData : function() {
                if (this.getSelected().length == 0) {
                        Ext.Msg.show({
                                                title : '提示',
                                                width : 250,
                                                closable : false,
                                                msg : ' 请选中要删除的数据',
                                                buttons : Ext.Msg.OK,
                                                icon : Ext.MessageBox.INFO
                                        });
                } else {
                        Ext.Msg.show({
                                                title : '提示',
                                                width : 250,
                                                closable : false,
                                                fn : this.deleteRecords.createDelegate(this),
                                                msg : ' 确定要删除这些数据吗?',
                                                buttons : Ext.Msg.YESNO,
                                                icon : Ext.MessageBox.QUESTION
                                        });
                }
        },

        deleteRecords : function(id) {
                if (id == 'yes') {
                        Ext.MessageBox.show({
                                title : "提示",
                                msg : "信息删除中...",
                                progress : true,
                                width : 300,
                                wait : true,
                                closable : true
                        });
                        var ids = "";
                        // 从选择的记录中获取id by gaojun
                        if(this.keepSelectedOnPaging && this.enablePageRemove){
                                ids += this.recordIds.join(',');
                        }else{
                                var s = this.getSelectionModel().getSelections();
                                for (var i = 0, r; r = s[i]; i++) {
                                        ids += r.get("id") + ","; // 获取id，批量删除 by gaojun
                                }
                        }
                        var requestConfig = {
                                        url : this.urlDeleteData,
                                        params : {
                                                ids : ids
                                        },
                                        success : function() {
                                                Ext.MessageBox.hide();
                                                //Ext.ux.LevitationMsgBox.msg('提示', '操作成功！');
                                                this.refresh(); // 刷新列表 by gaojun
                                                // 将修改删除按钮设置为disable by gaojun
                                                if(this.enablePageRemove && this.keepSelectedOnPaging){
                                                        this.clearRecordIdArray(); // 清除选中记录ID数组 by gaojun
                                                        var toolbar = this.getTopToolbar();
                                                        var updateButton = toolbar.find('buttonType','update')[0]
                                                                                                                        || Ext.getCmp(this.updateButton.id);
                                                        updateButton.disable();
                                                        var removeButton = toolbar.find('buttonType','update')[0]
                                                                                                                        || Ext.getCmp(this.removeButton.id);
                                                        removeButton.disable();
                                                }
                                        }.createDelegate(this),
                                        failure : function() {
                                                //Ext.ux.LevitationMsgBox.msg('提示', '操作失败！');
                                        }
                                }
                                Ext.Ajax.request(requestConfig);
                }
        },

        /**
         * 初始化分页工具条
         *
         */
        init_PagingBar : function() {
                /*
                 * updatr by esky 20100908
                 * 修改：分页条的关闭按钮和分页记录数可配置
                 */
                Ext.apply(this.pagingConfig,{
                        pageSize : this.pageSize
                });
                if(this.pageCloseButton){
                        Ext.apply(this.pagingConfig,{
                                items : ['-', {
                                        iconCls : 'cancel',
                                        tooltip : '关闭当前标签',
                                        handler : function() {
                                                this.findParentByType('tabpanel').getActiveTab().destroy();
                                        }
                                }]
                        })
                }else{
                        Ext.apply(this.pagingConfig,{
                                items : []
                        })
                }
                // by gaojun
                var pageConfigInfo = new Object;
                Ext.apply(pageConfigInfo,this.pagingConfig);
                if(this.doRefresh && Ext.isFunction(this.doRefresh)){
                        var grid = this;
                        Ext.apply(pageConfigInfo,{
                                doRefresh : grid.doRefresh.createDelegate(this)
                        });

                }
                var bbar = new Ext.PagingToolbar(pageConfigInfo);
                this.bbar = bbar;
        },
        /**
         * 分页记录选中状态的实现 在点击下页之前，会将选中的数据的id放入本机的数组中 当返回时，在装载数据时，服务器端传来的数据会与本地的数组进行比较
         * 还原之前的选中状态
         */
        init_OnPaging : function() {

                this.idColName = this.CM_JR_Record[0].dataIndex;
                if(this.checkBox){
                        this.selModel.on('rowdeselect', function(selMdl, rowIndex, rec) {

                                        for (var i = 0; i < this.recordIds.length; i++) {
                                                if (rec.data[this.idColName] == this.recordIds[i]) {
                                                        this.recordIds.splice(i, 1);
                                                        return;
                                                }
                                        }

                                }, this);
                        this.selModel.on('rowselect', function(selMdl, rowIndex, rec) {
                                        if (this.hasElement(this.recordIds)) {
                                                for (var i = 0; i < this.recordIds.length; i++) {
                                                        if (rec.data[this.idColName] == this.recordIds[i]) {
                                                                return;
                                                        }
                                                }
                                        }
                                        this.recordIds.unshift(rec.data[this.idColName]);

                                }, this);
                }
                        this.store.on('load', function(st, recs) {
                                        if (this.hasElement(this.recordIds)) {
                                                st.each(function(rec) {
                                                                        Ext.each(this.recordIds, function(item,
                                                                                                        index, allItems) {
                                                                                                if (rec.data[this.idColName] == item) {
                                                                                                        this.selModel
                                                                                                                        .selectRecords(
                                                                                                                                        [rec], true);
                                                                                                        return false;
                                                                                                }
                                                                                        }, this);
                                                                }, this);
                                        }

                                }, this);
        },

        hasElement : function(recIds) {
                if (recIds.length > 0)
                        return true;
                else
                        return false;
        },

        /**
         * 刷新列表
         * by gaojun
         */
        refresh : function() {
                this.store.load({
                        params : {
                                start : 0,
                                limit : this.pageSize ? this.pageSize : 20
                        },
                        callback : function(r, options, success) {
                                if (success == false) {
                                      //  Ext.ux.LevitationMsgBox.msg('提示', '无法装载数据！');
                                }
                        }
                });
        },
        // 清除记录的ID数组 by gaojun
        clearRecordIdArray : function() {
                if(this.hasElement(this.recordIds)){
                        this.recordIds.splice(0,this.recordIds.length);
                }
                this.getSelectionModel().clearSelections();
        },
        /**
         * 获取选择记录信息，如果keepSelectedOnPaging为true，则返回选择记录的id数组；
         * 反之，返回SelectionModel中选择的record数组 by gaojun
         * @return {}
         */
        getSelected : function(){
                if(this.keepSelectedOnPaging){
                        return this.recordIds;
                }else {
                        return this.getSelectionModel().getSelections();
                }
        },
        selectionChangeAction : function(sm) {
                var grid = this;
                var toolbar = grid.getTopToolbar();
                if(toolbar){
                        /*
                         * add by esky 2011-05-10
                         * handleNumType,按钮待处理数量类型，"single"时列表单选时可用,"multitude"单选或复选时可用
                         */
                        Ext.each(toolbar.findByType("button"),function(btn){
                                if(btn.handleNumType){
                                        if(btn.handleNumType=='single'){
                                                if (this.getSelected().length == 1) {
                                                        btn.enable();
                                                } else {
                                                        btn.disable();
                                                }
                                        }else if(btn.handleNumType=='multitude'){
                                                if (this.getSelected().length != 0) {
                                                        btn.enable();
                                                } else {
                                                        btn.disable();
                                                }
                                        }
                                }
                        },this)
                }
                //添加汇总面的列表，勾选时，可以汇总勾选字段的值
                if(this.isSummary){
                        this.plugins.refreshSummary();
                }

        }
});
Ext.reg('collectgrid',Ext.ux.grid.CollectGrid);