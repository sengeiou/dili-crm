// 打开选择用户弹出框
function _selectUser(callback, args) {
	showUserDlg();
}
// 确认选择事件
function confirmUserBtn() {
	var selected = $('#selectUserGrid').datagrid('getSelected');
	if (null == selected) {
		$.messager.alert('警告','请选中一条数据');
		return;
	}
	$('#${controlId}').textbox('initValue', selected.id);
	$('#${controlId}').textbox('setText', selected.realName);
	var icon = $('#${controlId}').textbox('getIcon',0);
	icon.css('visibility','visible');
	$('#${dlgId}').dialog('close');
}
//关闭用户选择窗口
function closeUserSelectDlg(){
	$('#${dlgId}').dialog('close');
}
// 根据id打开用户选择
//查询数据权限拥有市场下的用户
function showUserDlg(firmCode) {
	if(firmCode == null){
		//默认取当前用户的归属市场
		//firmCode = "${@com.dili.uap.sdk.session.SessionContext.getSessionContext().getUserTicket().getFirmCode()}";
		firmCode = "";
	}
	$('#${dlgId}').dialog({
				title : '用户选择',
				width : 800,
				height : 400,
				queryParams : {
					firmCode : firmCode
				},
				href : '${contextPath!}/selectDialog/user.html',
				modal : true,
				shadow:true
			});
}

$(function() {
	$('#${controlId}').textbox('addClearBtn', 'icon-clear');
})