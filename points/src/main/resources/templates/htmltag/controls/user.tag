// 打开选择用户弹出框
function _selectUser(callback, args) {
	if (callback) {
		eval("(" + callback + "(args))");
	} else {
		showUserDlg($(this)[0].id);
	}
}
// 确认选择事件
function confirmUserBtn(id) {
	var selected = $('#selectUserGrid').datagrid('getSelected');
	if (null == selected) {
		$.messager.alert('警告','请选中一条数据');
		return;
	}
	$('#' + id).textbox('initValue', selected.id);
	$('#' + id).textbox('setText', selected.realName);
	var icon = $('#' + id).textbox('getIcon',0);
	icon.css('visibility','visible');
	$('#${dlgId}').dialog('close');
}
//关闭用户选择窗口
function closeUserSelectDlg(){
	$('#${dlgId}').dialog('close');
}
// 根据id打开用户选择
function showUserDlg(id) {
	$('#${dlgId}').dialog({
				title : '用户选择',
				width : 800,
				height : 400,
				queryParams : {
					textboxId : id
				},
				href : '${contextPath!}/selectDialog/user.html',
				modal : true,
				shadow:true
			});
}

$(function() {
	$('#${controlId}').textbox('addClearBtn', 'icon-clear');
})