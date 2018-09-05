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

/**
 * 号码加星隐藏
 * @param str
 * @param frontLen 前面保留位数
 * @param endLen 后面保留位数
 * @return {string}
 */
function plusXing(str,frontLen,endLen) {
    if(str == undefined || str == null){
        return null;
    }
    var len = str.length-frontLen-endLen;
    if(len <= 0){
        return str;
    }
    var xing = '';
    for (var i=0;i<len;i++) {
        xing+='*';
    }
    return str.substring(0,frontLen)+xing+str.substring(str.length-endLen);
}


/**
 * 证件号码格式化
 * 如果提供了证件类型(certificateType)， 可以判断`certificateType != id` 为其它证件号
 * 1.	身份证隐藏规则：如果为15位证件号隐藏第7-12位用*表示，例如342123******123；如果为18位证件号隐藏第7-14位用*表示，例如：342112********1234；
 * 2.	其他证件号隐藏规则：保留最后四位显示状态，前面都用*表示，例如：*********1234；
 */

function certificateNumberFmt(value,row,index) {
    if(value == null || typeof(value)=="undefined"){
        return "";
    }
    var certificateType = row == null ? "id" : row[orginal_key_prefix+"certificateType"] ? row[orginal_key_prefix+"certificateType"] : row["certificateType"];
    if(certificateType != null && certificateType != "id"){
        return plusXing(value, 0, 4);
    }
    // 如果为15位证件号隐藏第7-12位用*表示，例如342123******123
    if(value.length <= 15){
        return plusXing(value, 6, 3);
    }//如果为18位证件号隐藏第7-14位用*表示，例如：342112********1234
    else{
        return plusXing(value, 6, 4);
    }
}

/**
 * 手机号码格式化
 * 隐藏手机号码第4-7位用*表示，例如：136****1212
 */
function mobilePhoneFmt(value,row,index) {
    if (value == null || value.length == 0) {
        return null;
    }
    if (value.length == 11) {
        var reg = /^(\d{3})\d{4}(\d{4})$/;
        return value.replace(reg, "$1****$2");
    }
    if (value.length > 3 && value.length < 7 ){
        return plusXing(value, 0, 3);
    }
    if(value.length >=7){
        return plusXing(value, value.length-7, 3);
    }
    return value;
}

/**
 * 座机号码格式化
 * 座机号码隐藏第5-7位用*表示，例如：0288****212
 */
function telephoneFmt(value,row,index) {
    if (value == null || value.length == 0) {
        return null;
    }
    if (value.length > 3 && value.length < 7 ){
        return plusXing(value, 0, 3);
    }
    if(value.length >=7){
        return plusXing(value, value.length-7, 3);
    }
    return value;
}