<script type="text/javascript">
    var rangeCode = 60; //区间条件CODE
    var toolbar = [{
        text: '新增',
        iconCls: 'icon-add',
        handler: function () {
            $('#number_form').form('clear');
            $("#edit_type").val(0);
            $("#type").val(0);
            $('#dlg').dialog({
                title: '交易量权重（公斤）'
            });
            $('#dlg').dialog('center');
            $('#dlg').dialog('open');
        }
    }, {
        text: '修改',
        iconCls: 'icon-edit',
        handler: function () {
            var row = $('#dg_0').datagrid('getSelected');
            if (null == row) {
                $.messager.alert('警告', '请选中一条数据');
                return;
            }
            $('#number_form').form('load', row);
            $("#edit_type").val(1);
            $("#type").val(0);
            $('#dlg').dialog({
                title: '交易量权重（公斤）'
            });
            $('#dlg').dialog('center');
            $('#dlg').dialog('open');
        }
    }, {
        text: '删除',
        iconCls: 'icon-remove',
        handler: function () {
            var row = $('#dg_0').datagrid('getSelected');
            if (null == row) {
                $.messager.alert('警告', '请选中一条数据');
                return;
            }
            var index = $('#dg_0').datagrid('getRowIndex');
            $('#dg_0').datagrid('deleteRow', index);
        }
    }];

    var toolbarMoney = [{
        text: '新增',
        iconCls: 'icon-add',
        handler: function () {
            $('#number_form').form('clear');
            $("#edit_type").val(0);
            $("#type").val(1);
            $('#dlg').dialog({
                title: '交易额权重（元）'
            });
            $('#dlg').dialog('center');
            $('#dlg').dialog('open');
        }
    }, {
        text: '修改',
        iconCls: 'icon-edit',
        handler: function () {
            var row = $('#dg_1').datagrid('getSelected');
            if (null == row) {
                $.messager.alert('警告', '请选中一条数据');
                return;
            }
            $('#number_form').form('load', row);
            $("#edit_type").val(1);
            $("#type").val(1);
            $('#dlg').dialog({
                title: '交易额权重（元）'
            });
            $('#dlg').dialog('center');
            $('#dlg').dialog('open');
        }
    }, {
        text: '删除',
        iconCls: 'icon-remove',
        handler: function () {
            var row = $('#dg_1').datagrid('getSelected');
            if (null == row) {
                $.messager.alert('警告', '请选中一条数据');
                return;
            }
            var index = $('#dg_1').datagrid('getRowIndex');
            $('#dg_1').datagrid('deleteRow', index);
        }
    }];

    var toolbarPay = [{
        text: '新增',
        iconCls: 'icon-add',
        handler: function () {
            $('#pay_form').form('clear');
            $("#pay_edit_type").val(0);
            $('#dlg_pay').dialog('center');
            $('#dlg_pay').dialog('open');
        }
    }, {
        text: '修改',
        iconCls: 'icon-edit',
        handler: function () {
            var row = $('#dg_2').datagrid('getSelected');
            if (null == row) {
                $.messager.alert('警告', '请选中一条数据');
                return;
            }
            $('#pay_form').form('load', row);
            $("#pay_edit_type").val(1);
            $('#dlg_pay').dialog('center');
            $('#dlg_pay').dialog('open');
        }
    }, {
        text: '删除',
        iconCls: 'icon-remove',
        handler: function () {
            var row = $('#dg_2').datagrid('getSelected');
            if (null == row) {
                $.messager.alert('警告', '请选中一条数据');
                return;
            }
            var index = $('#dg_2').datagrid('getRowIndex');
            $('#dg_2').datagrid('deleteRow', index);
        }
    }];

    // 保存交易额，交易量
    function saveNumber() {
        if (!$("#number_form").form('validate')) {
            return;
        }
        var dg = $("#dg_" + $("#type").val());
        var row = {
            conditionType: $("#conditionType").combobox("getValue"),
            value: $("#value").val(),
            startValue: $("#startValue").val(),
            endValue: $("#endValue").val(),
            weight: $("#weight").val(),
            modified: new Date()
        };
        if ($("#edit_type").val() == 0) {
            dg.datagrid('appendRow', row);
        } else {
            var index = dg.datagrid('getRowIndex',dg.datagrid('getSelected'));
            dg.datagrid('updateRow', {
                index: index,
                row: row
            });
        }
        $('#dlg').dialog('close');
        $('#number_form').form('clear');
    }


    function savePay() {
        if (!$("#pay_form").form('validate')) {
            return;
        }
        var dg = $("#dg_2");
        var row = {
            value: $("#payConditionType").combobox("getValue"),
            weight: $("#payWeight").numberbox('getValue'),
            conditionType: $("#payConditionType").combobox("getValue"),
            displayText: $("#payConditionType").combobox("getText"),
            modified: new Date()
        };
        if ($("#pay_edit_type").val() == 0) {
            dg.datagrid('appendRow', row);
        } else {
            var index = dg.datagrid('getRowIndex',dg.datagrid('getSelected'));
            dg.datagrid('updateRow', {
                index: index,
                row: row
            });
        }

        $('#dlg_pay').dialog('close');
        $('#pay_form').form('clear');
    }

    function success() {
        var value = $("#conditionType").combobox("getValue");
        setDisplay(value);
    }

    function change(newValue) {
        setDisplay(newValue);
    }

    function setDisplay(value) {
        if (value == rangeCode) {
            $("#number_range").show();
            $("#value").numberbox('disable');
            $("#value").numberbox('setValue','');
            $("#startValue").numberbox('enable');
            $("#endValue").numberbox('enable');
            $("#number_other").hide();
        } else {
            $("#number_range").hide();
            $("#value").numberbox('enable');
            $("#startValue").numberbox('disable');
            $("#startValue").numberbox('setValue','');
            $("#endValue").numberbox('disable');
            $("#endValue").numberbox('setValue','');
            $("#number_other").show();
        }
    }
    function formatDisplay(value, row, index) {
        if (row.conditionType == rangeCode) {
            return row.startValue + " - " + row.endValue;
        } else{
           var data =  $("#conditionType").combobox("getData");
            for(var d in data){
                if(data[d].value == row.conditionType ){
                    return data[d].text + "" + row.value;
                }
            }
            return "";
        }
    }


    $.extend($.fn.validatebox.defaults.rules, {
        big: {
            validator: function (value, param) {
                return parseFloat(value) > parseFloat($(param[0]).val());
            },
            message: '必须大于起始值'
        }
    });

    $.extend($.fn.validatebox.defaults.rules, {
        notZero: {
            validator: function (value) {
                if(parseFloat(value) <=0){
                    return false;
                }
                return true;
            },
            message: '计算参数需大于0'
        }
    });
    $(function () {
        var _comboProviderParamObj_conditionType = {};
        _comboProviderParamObj_conditionType.provider = 'dataDictionaryValueProvider';
        //仅为simpleValueProvider传入默认参数
        if (_comboProviderParamObj_conditionType.provider == "simpleValueProvider") {
            _comboProviderParamObj_conditionType.queryParams = '{dd_id:"23"}';
            _comboProviderParamObj_conditionType.valueField = 'value';
            _comboProviderParamObj_conditionType.textField = 'text';
        } else {
            _comboProviderParamObj_conditionType.queryParams = '{dd_id:"23"}';
            _comboProviderParamObj_conditionType.valueField = '';
            _comboProviderParamObj_conditionType.textField = '';
        }
        _comboProviderParamObj_conditionType.table = '';

        //注意，这里只能取到value属性中的值，而无法取到combobox的当前值，因为还没有渲染，渲染以后应该使用getValue方法取值
        _comboProviderParamObj_conditionType.value = $("#conditionType").val();
        $("#conditionType").combobox({
            onLoadSuccess: function () {
                success();
            },
            onChange: function (newValue) {
                change(newValue)
            },
            url: "/provider/getLookupList"
            , method: "POST"
            , valueField: "value"
            , textField: "text"
            , queryParams: _comboProviderParamObj_conditionType
        });
    });
</script>