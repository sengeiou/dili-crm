<script type="text/javascript">
    var rangeCode = 179;
    var toolbar = [{
        text: '新增',
        iconCls: 'icon-add',
        handler: function () {
            $("#edit_type").val(0);
            $("#type").val(0);
            $('#dlg').dialog({
                title: '交易量权重（公斤）'
            });
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
            $("#edit_type").val(0);
            $("#type").val(1);
            $('#dlg').dialog({
                title: '交易额权重（元）'
            });
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
            $("#pay_edit_type").val(0);
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

    function saveNumber() {
        if (!$("#number_form").form('validate')) {
            return;
        }
        var edit_type = $("#edit_type").val();
        var type = $("#type").val();
        var c_type = $("#conditionType").combobox("getValue");
        var value = $("#value").val();
        var start_value = $("#startValue").val();
        var end_value = $("#endValue").val();
        var weight = $("#weight").val();
        var displayText = "";
        if ($("#conditionType").combobox("getValue") == rangeCode) {
            displayText = start_value + " - " + end_value;
        } else {
            displayText = $("#conditionType").combobox("getText") + value;
        }
        var dg = $("#dg_" + type);
        var row = {
            conditionType: c_type,
            value: value,
            startValue: start_value,
            endValue: end_value,
            weight: weight,
            displayText: displayText
        };
        if (edit_type == 0) {
            dg.datagrid('appendRow', row);
        } else {
            var index = dg.datagrid('getRowIndex');
            dg.datagrid('updateRow', {
                index: index,
                row: row
            });
        }
        $('#dlg').dialog('close');
        $('#number_form').form('clear');
    }


    function savePay() {
        var edit_type = $("#pay_edit_type").val();
        var dg = $("#dg_2");
        var row = {
            value: $("#payConditionType").combobox("getValue"),
            weight: $("#payWeight").numberbox('getValue'),
            displayText: $("#payConditionType").combobox("getText")
        };
        if (edit_type == 0) {
            dg.datagrid('appendRow', row);
        } else {
            var index = dg.datagrid('getRowIndex');
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
            $("#startValue").numberbox('enable');
            $("#endValue").numberbox('enable');
            $("#number_other").hide();
        } else {
            $("#number_range").hide();
            $("#value").numberbox('enable');
            $("#startValue").numberbox('disable');
            $("#endValue").numberbox('disable');
            $("#number_other").show();
        }
    }

    function submitForm(obj) {
        $('#_form').form('submit', {
            url: "${contextPath}/pointsRule/insert",
            onSubmit: function () {
                if ($("#_form").form('validate')) {
                    $(obj).linkbutton('disable');
                }
                $("#numberJson").val(JSON.stringify($("#dg_0").datagrid("getData").rows));
                $("#moneyJson").val(JSON.stringify($("#dg_1").datagrid("getData").rows));
                $("#payMethodJson").val(JSON.stringify($("#dg_2").datagrid("getData").rows));
                return $("#_form").form('validate');
            },
            success: function (data) {

            }
        });
    }


    $.extend($.fn.validatebox.defaults.rules, {
        big: {
            validator: function (value, param) {
                return value > $(param[0]).val();
            },
            message: '必须大于起始值'
        }
    });


    $(function () {
        $('#conditionType').combobox({
            onLoadSuccess: function () {
                success();
            },
            onChange: function (newValue) {
                change(newValue)
            }
        });
    });
</script>