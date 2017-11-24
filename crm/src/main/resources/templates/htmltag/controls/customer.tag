// 打开选择用户弹出框
function _selectCustomer(callback, args) {
	if (callback) {
		eval("(" + callback + "(args))");
	} else {
		showCustomerDlg($(this)[0].id);
	}
}
// 确认选择事件
function confirmCustomerBtn(id) {
	var selected = $('#selectCustomerGrid').datagrid('getSelected');
	$('#' + id).textbox('initValue', selected.id);
	$('#' + id).textbox('setText', selected.name);
	var icon = $('#' + id).textbox('getIcon',0);
	icon.css('visibility','visible');
	$('#${dlgId}').dialog('close');
}
//关闭用户选择窗口
function closeCustomerSelectDlg(){
	$('#${dlgId}').dialog('close');
}
// 根据id打开用户选择
function showCustomerDlg(id) {
	$('#${dlgId}').dialog({
				title : '客户选择',
				width : 800,
				height : 400,
				queryParams : {
					textboxId : id
				},
				href : '${contextPath!}/selectDialog/customer.html',
				modal : true,
				buttons : [{
							text : '确定',
							handler : function() {
								confirmCustomerBtn(id);
							}
						}, {
							text : '取消',
							handler : function() {
								closeCustomerSelectDlg();
							}
						}]
			});
}
$(function() {
$('#${controlId}').textbox('addClearBtn', 'icon-clear');
})