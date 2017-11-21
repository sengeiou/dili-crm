// 打开选择用户弹出框
function selectUser(callback, args) {
	if (callback) {
		eval("(" + callback + "(args))");
	} else {
		showUserDlg($(this)[0].id);
	}
}
// 确认选择事件
function confirmUserBtn(id) {
	var selected = $('#selectUserGrid').datagrid('getSelected');
	$('#' + id).textbox('setValue', selected.id);
	$('#' + id).textbox('setText', selected.realName);
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
				buttons : [{
							text : '确定',
							handler : function() {
								confirmUserBtn(id);
							}
						}, {
							text : '取消',
							handler : function() {
								$('#${dlgId}').dialog('close');
							}
						}]
			});
}