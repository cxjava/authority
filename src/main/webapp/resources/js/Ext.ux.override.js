Ext.EventManager.on(Ext.isIE ? document : window, 'keydown', function (e, i) {
/** 屏蔽backspace键 */
    var t = e.target.tagName;
    if (e.getKey() == e.BACKSPACE && t !== "INPUT" && t !== "TEXTAREA") {
        e.stopEvent();
    } else if (e.getKey() == e.BACKSPACE && (i.disabled || i.readOnly)) {
        e.stopEvent();
    }
});

/** 密码输入一致 */
/**
 * vtype : "password",// 自定义的验证类型 vtypeText : "两次输入的密码不一致！", confirmTo :
 * "password",// 要比较的另外一个的组件的id
 */
Ext.apply(Ext.form.VTypes, {
    password: function (val, field) { // val指这里的文本框值，field指这个文本框组件，大家要明白这个意思
        if (field.confirmTo) { // confirmTo是我们自定义的配置参数，一般用来保存另外的组件的id值
            var pwd = Ext.get(field.confirmTo); // 取得confirmTo的那个id的值
            return (val == pwd.getValue());
        }
        return true;
    }
}); /** 日期限制 */
/**
 * inspectionscheme.startTime = new Ext.form.DateField({ id : 'startTime', name :
 * 'startTime', width : 85, value : new Date().getFirstDateOfMonth(), format :
 * 'Y-m-d', vtype : 'daterange', endDateField : 'endTime' });
 * inspectionscheme.endTime = new Ext.form.DateField({ id : 'endTime', name :
 * 'endTime', width : 85, value : new Date(), format : 'Y-m-d', vtype :
 * 'daterange', startDateField : 'startTime' }); var s =
 * inspectionscheme.startTime; var e = inspectionscheme.endTime; var st =
 * s.formatDate(s.getValue()); var et = e.formatDate(e.getValue()); st =
 * st.length > 0 ? st + " 00:00:00" : ""; et = et.length > 0 ? et + " 23:59:59" :
 * "";
 */
Ext.apply(Ext.form.VTypes, {
    daterange: function (val, field) {
        var date = field.parseDate(val);
        if (!date) {
            return false;
        }
        if (field.startDateField && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax.getTime()))) {
            var start = Ext.getCmp(field.startDateField);
            start.setMaxValue(date);
            this.dateRangeMax = date;
            start.validate();
        } else if (field.endDateField && (!this.dateRangeMin || (date.getTime() != this.dateRangeMin.getTime()))) {
            var end = Ext.getCmp(field.endDateField);
            end.setMinValue(date);
            this.dateRangeMin = date;
            end.validate();
        }
        return true;
    }
}); /** 必填提示 */
Ext.override(Ext.form.TextField, {
    initComponent: Ext.form.TextField.prototype.initComponent.createInterceptor(function () {
        if (this.allowBlank === false && this.fieldLabel) {
            this.fieldLabel = '<font color=red>*</font>' + this.fieldLabel;
        }
    })
}); /** 必填提示 */
Ext.override(Ext.form.RadioGroup, {
    initComponent: Ext.form.RadioGroup.prototype.initComponent.createInterceptor(function () {
        if (this.allowBlank === false && this.fieldLabel) {
            this.fieldLabel = '<font color=red>*</font>' + this.fieldLabel;
        }
    })
}); /** 必填提示 */
Ext.override(Ext.form.CheckboxGroup, {
    initComponent: Ext.form.CheckboxGroup.prototype.initComponent.createInterceptor(function () {
        if (this.allowBlank === false && this.fieldLabel) {
            this.fieldLabel = '<font color=red>*</font>' + this.fieldLabel;
        }
    })
});
/**
 * 重写Ext.form.TextField的字符串长度验证 一个中文算2个字符长度 适用于Extjs 3.3
 */
Ext.override(Ext.form.TextField, {
    _CheckLength: function (strTemp) {
        var i, sum;
        sum = 0;
        for (i = 0; i < strTemp.length; i++) {
            if ((strTemp.charCodeAt(i) >= 0) && (strTemp.charCodeAt(i) <= 255)) sum = sum + 1;
            else sum = sum + 2;
        }
        return sum;
    },
    getErrors: function (value) {
        var errors = Ext.form.TextField.superclass.getErrors.apply(this, arguments);
        value = Ext.isDefined(value) ? value : this.processValue(this.getRawValue());
        if (Ext.isFunction(this.validator)) {
            var msg = this.validator(value);
            if (msg !== true) {
                errors.push(msg);
            }
        }
        if (value.length < 1 || value === this.emptyText) {
            if (this.allowBlank) {
                // if value is blank and allowBlank is true, there
                // cannot be any additional errors
                return errors;
            } else {
                errors.push(this.blankText);
            }
        }
        if (!this.allowBlank && (value.length < 1 || value === this.emptyText)) {
            // if it's blank
            errors.push(this.blankText);
        }
        var valueLength = this._CheckLength(value);
        if (valueLength < this.minLength) {
            errors.push(String.format(this.minLengthText + ',实际输入长度 ' + valueLength, this.minLength));
        }
        if (valueLength > this.maxLength) {
            errors.push(String.format(this.maxLengthText + ',实际输入长度 ' + valueLength, this.maxLength));
        }
        if (this.vtype) {
            var vt = Ext.form.VTypes;
            if (!vt[this.vtype](value, this)) {
                errors.push(this.vtypeText || vt[this.vtype + 'Text']);
            }
        }
        if (this.regex && !this.regex.test(value)) {
            errors.push(this.regexText);
        }
        return errors;
    }
}); /** 多选框 */
Ext.override(Ext.grid.CheckboxSelectionModel, {
    onMouseDown: function (e, t) {
        if (e.button === 0 && t.className == 'x-grid3-row-checker') {
            e.stopEvent();
            var row = e.getTarget('.x-grid3-row');
            // mouseHandled flag check for a duplicate selection
            // (handleMouseDown) call
            if (!this.mouseHandled && row) {
                // alert(this.grid.store.getCount());
                var gridEl = this.grid.getEl(); // 得到表格的EL对象
                var hd = gridEl.select('div.x-grid3-hd-checker'); // 得到表格头部的全选CheckBox框
                var index = row.rowIndex;
                if (this.isSelected(index)) {
                    this.deselectRow(index);
                    var isChecked = hd.hasClass('x-grid3-hd-checker-on');
                    // 判断头部的全选CheckBox框是否选中，如果是，则删除
                    if (isChecked) {
                        hd.removeClass('x-grid3-hd-checker-on');
                    }
                } else {
                    this.selectRow(index, true);
                    // 判断选中当前行时，是否所有的行都已经选中，如果是，则把头部的全选CheckBox框选中
                    if (gridEl.select('div.x-grid3-row-selected').elements.length == gridEl.select('div.x-grid3-row').elements.length) {
                        hd.addClass('x-grid3-hd-checker-on');
                    };
                }
            }
        }
        this.mouseHandled = false;
    },
    onHdMouseDown: function (e, t) {
        /**
         * 大家觉得上面重写的代码应该已经实现了这个功能了，可是又发现下面这行也重写了
         * 由原来的t.className修改为t.className.split(' ')[0]
         * 为什么呢？这个是我在快速点击头部全选CheckBox时，
         * 操作过程发现，有的时候x-grid3-hd-checker-on这个样式还没有删除或者多一个空格，结果导致下面这个判断不成立
         * 去全选或者全选不能实现
         */
        if (t.className.split(' ')[0] == 'x-grid3-hd-checker') {
            e.stopEvent();
            var hd = Ext.fly(t.parentNode);
            var isChecked = hd.hasClass('x-grid3-hd-checker-on');
            if (isChecked) {
                hd.removeClass('x-grid3-hd-checker-on');
                this.clearSelections();
            } else {
                hd.addClass('x-grid3-hd-checker-on');
                this.selectAll();
            }
        }
    },
    handleMouseDown: function (g, rowIndex, e) {
        if (e.button !== 0 || this.isLocked()) {
            return;
        }
        var view = this.grid.getView();
        if (e.shiftKey && !this.singleSelect && this.last !== false) {
            var last = this.last;
            this.selectRange(last, rowIndex, e.ctrlKey);
            this.last = last; // reset the last
            view.focusRow(rowIndex);
        } else {
            var gridEl = this.grid.getEl(); // 得到表格的EL对象
            var hd = gridEl.select('div.x-grid3-hd-checker'); // 得到表格头部的全选CheckBox框
            var isSelected = this.isSelected(rowIndex);
            if (isSelected) {
                this.deselectRow(rowIndex);
                var isChecked = hd.hasClass('x-grid3-hd-checker-on');
                // 判断头部的全选CheckBox框是否选中，如果是，则删除
                if (isChecked) {
                    hd.removeClass('x-grid3-hd-checker-on');
                }
            } else if (!isSelected || this.getCount() > 1) {
                this.selectRow(rowIndex, true);
                // 判断选中当前行时，是否所有的行都已经选中，如果是，则把头部的全选CheckBox框选中
                if (gridEl.select('div.x-grid3-row-selected').elements.length == gridEl.select('div.x-grid3-row').elements.length) {
                    hd.addClass('x-grid3-hd-checker-on');
                };
                view.focusRow(rowIndex);
            }
        }
    }
});