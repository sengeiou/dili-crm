// 编辑行索引
var editId = undefined;

// 结束行编辑
function endEditing() {
	if (editId == undefined) {
		return true
	}
	if (projectGrid.treegrid('validateRow', editId)) {
		projectGrid.treegrid('endEdit', editId);
		editId = undefined;
		return true;
	} else {
		return false;
	}
}

// 打开新增窗口
function openInsert() {
	if (!endEditing()) {
		$.messager.alert('警告', '有数据正在编辑');
		return;
	}
	var node = projectGrid.treegrid('getSelected');
	editId = 'temp';
	projectGrid.treegrid('append', {
				parent : node ? node.id : undefined,
				data : [{
							id : 'temp'
						}]
			});

	projectGrid.treegrid('select', 'temp');
	projectGrid.treegrid('beginEdit', 'temp');
}

// 打开修改窗口
function openUpdate() {
	var selected = projectGrid.treegrid("getSelected");
	if (!selected) {
		$.messager.alert('警告', '请选中一条数据');
		return;
	}
	if (endEditing()) {
		projectGrid.treegrid('select', selected.id).treegrid('beginEdit', selected.id);
		editId = selected.id;
	}
}

function saveOrUpdate() {
	if (!$('#_form').form("validate")) {
		return;
	}
	var _formData = removeKeyStartWith($("#_form").serializeObject(), "_");
	var _url = null;
	// 没有id就新增
	if (_formData.id == null || _formData.id == "") {
		_url = "${contextPath}/project/insert";
	} else {// 有id就修改
		_url = "${contextPath}/project/update";
	}
	$.ajax({
				type : "POST",
				url : _url,
				data : _formData,
				processData : true,
				dataType : "json",
				async : true,
				success : function(data) {
					if (data.code == "200") {
						$("#grid").datagrid("reload");
						$('#dlg').dialog('close');
					} else {
						$.messager.alert('错误', data.result);
					}
				},
				error : function() {
					$.messager.alert('错误', '远程访问失败');
				}
			});
}

// 根据主键删除
function del() {
	var selected = $("#grid").datagrid("getSelected");
	if (null == selected) {
		$.messager.alert('警告', '请选中一条数据');
		return;
	}
	$.messager.confirm('确认', '您确认想要删除记录吗？', function(r) {
				if (r) {
					$.ajax({
								type : "POST",
								url : "${contextPath}/project/delete",
								data : {
									id : selected.id
								},
								processData : true,
								dataType : "json",
								async : true,
								success : function(data) {
									if (data.code == "200") {
										$("#grid").datagrid("reload");
										$('#dlg').dialog('close');
									} else {
										$.messager.alert('错误', data.result);
									}
								},
								error : function() {
									$.messager.alert('错误', '远程访问失败');
								}
							});
				}
			});

}
// 表格查询
function queryGrid() {
	var opts = $("#grid").datagrid("options");
	if (null == opts.url || "" == opts.url) {
		opts.url = "${contextPath}/project/listPage";
	}
	if (!$('#form').form("validate")) {
		return;
	}
	var param = bindMetadata("grid", true);
	var formData = $("#form").serializeObject();
	$.extend(param, formData);
	$("#grid").datagrid("load", param);
}

// 清空表单
function clearForm() {
	$('#form').form('clear');
}

// 表格表头右键菜单
function headerContextMenu(e, field) {
	e.preventDefault();
	if (!cmenu) {
		createColumnMenu("grid");
	}
	cmenu.menu('show', {
				left : e.pageX,
				top : e.pageY
			});
}

$.messager.progress({
			title : "提示",
			msg : "加载中,请稍候...",
			value : '10',
			text : '{value}%',
			interval : 200
		});
$.parser.onComplete = function() {
	$.messager.progress("close");
}
// 全局按键事件
function getKey(e) {
	e = e || window.event;
	var keycode = e.which ? e.which : e.keyCode;
	if (keycode == 46) { // 如果按下删除键
		var selected = $("#grid").datagrid("getSelected");
		if (selected && selected != null) {
			del();
		}
	}
}

function selectMember() {
	$('#smDialog').dialog({
				title : '用户选择',
				width : 400,
				height : 200,
				href : '${contextPath!}/project/members',
				modal : true,
				buttons : [{
							text : '确定',
							handler : function() {
								var selected = $('#smGridList').datagrid('getSelected');
								console.log(selected);
							}
						}, {
							text : '取消',
							handler : function() {
							}
						}]
			});
}

/**
 * 绑定页面回车事件，以及初始化页面时的光标定位
 * 
 * @formId 表单ID
 * @elementName 光标定位在指点表单元素的name属性的值
 * @submitFun 表单提交需执行的任务
 */
$(function() {
			bindFormEvent("form", "parentId", queryGrid);
			if (document.addEventListener) {
				document.addEventListener("keyup", getKey, false);
			} else if (document.attachEvent) {
				document.attachEvent("onkeyup", getKey);
			} else {
				document.onkeyup = getKey;
			}
			window.projectGrid = $('#grid');
		});