function trim(str){ //删除左右两端的空格
    return str.replace(/(^\s*)|(\s*$)/g, "");
}

/**
 * 禁用form中的所有控件
 * @param formId 表单ID
 * @param type 操作类型:disabled | readonly | editable
 * @param value 值：true | false
 */
function ableForm(formId,type,value) {
    var formId = ("#"+formId);
    $(formId+":input").prop(type,value);
}

/**
 * 禁/启用easyUiForm中的所有控件
 * @param formId 表单ID
 * @param value 值：true | false
 */
function disableEasyUiForm(formId,value) {
    var formId = ("#" + formId);
    if (value) {
        $(formId + " .easyui-textbox," + formId + " .easyui-datetimebox," + formId + " .easyui-combobox").textbox('disable');
        $(formId + " .textbox-label-disabled").removeClass("textbox-label-disabled");
    }else{
        $(formId + " .easyui-textbox," + formId + " .easyui-datetimebox," + formId + " .easyui-combobox").textbox('enable');
    }
}

/**
 * 设置easyUiForm中的所有控件是否可读，可编辑
 * @param formId 表单ID
 * @param type 操作类型: readonly | editable
 * @param value 值：true | false
 */
function readOrEditEasyUiForm(formId,type,value) {
    var formId = ("#" + formId);
    $(formId + " .easyui-textbox," + formId + " .easyui-datetimebox," + formId + " .easyui-combobox").textbox(type,value);
}

/**
 * 是否禁用easyUi的控件
 * @param controlId 控件ID
 * @param isDisable 是否禁用(true-禁用)
 */
function disableEasyUiControl(controlId,isDisable) {
    if (isDisable) {
        $("#" + controlId).linkbutton("disable");
    }else{
        $("#" + controlId).linkbutton("enable");
    }
}