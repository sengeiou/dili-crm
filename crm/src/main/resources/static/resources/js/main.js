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
 * 禁用form中的所有控件
 * @param formId 表单ID
 * @param type 操作类型:disable | readonly | editable
 * @param value 值：true | false
 */
function ableEasyUiForm(formId,type,value) {
    var formId = ("#"+formId);
    $(formId+" .easyui-textbox,"+formId+" .easyui-datetimebox,"+formId+" .easyui-combobox").textbox(type,value);
    $(formId+" .textbox-label-disabled").removeClass("textbox-label-disabled");
}