// 打开选择用户弹出框
function _selectCustomer(callback, args) {
if (callback) {
eval("(" + callback + "(args))");
} else {
showCustomerDlg();
}
}
// 确认选择事件
function confirmCustomerBtn() {
var selected = $('#selectCustomerGrid').datagrid('getSelected');
if (null == selected) {
swal('警告','请选中一条数据', 'warning');
return;
}
$('#${controlId}').textbox('initValue', selected.id);
$('#${controlId}').textbox('setText', selected.name);
var icon = $('#${controlId}').textbox('getIcon',0);
icon.css('visibility','visible');
$('#${dlgId}').dialog('close');
}
//关闭用户选择窗口
function closeCustomerSelectDlg(){
$('#${dlgId}').dialog('close');
}
// 根据id打开用户选择
function showCustomerDlg() {
$('#${dlgId}').dialog({
title : '客户选择',
width : 800,
height : 400,
queryParams : {
textboxId : ""
<%if(has(dataUrl) && dataUrl != ""){%>
,dataUrl:'${dataUrl}'
<%}%>
<%if(has(customerId) && customerId != ""){%>
,id:'${customerId}'
<%}%>
},
href : '${contextPath!}/selectDialog/customer.html',
modal : true,
shadow:true
});
}
$(function() {
$('#${controlId}').textbox('addClearBtn', 'icon-clear');
})