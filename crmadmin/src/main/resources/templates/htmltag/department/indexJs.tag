// 编辑行索引
var editId = undefined;

function isEditing() {
	return undefined != editId;
}

// 结束行编辑
function endEditing() {
	if (editId == undefined) {
		return true
	}
	if (deptGrid.treegrid('validateRow', editId)) {
		deptGrid.treegrid('endEdit', editId);
		editId = undefined;
		return true;
	} else {
		return false;
	}
}

// 新增一行空数据并开启编辑模式
function openInsert() {
	if (!dataAuth.addDept) {
		return;
	}
	if (!endEditing()) {
		$.messager.alert('警告', '有数据正在编辑');
		return;
	}
	var node = deptGrid.treegrid('getSelected');
	if (!node) {
		$.messager.alert('警告', '请选择一条数据');
		return;
	}
	editId = 'temp';
	deptGrid.treegrid('append', {
				parent : node.id,
				data : [{
							id : 'temp'
						}]
			});

	deptGrid.treegrid('select', 'temp');
	deptGrid.treegrid('beginEdit', 'temp');
}

// 开启选中行的编辑模式
function openUpdate() {
	if (!dataAuth.editDept) {
		return;
	}
	var selected = deptGrid.treegrid("getSelected");
	if (!selected) {
		$.messager.alert('警告', '请选中一条数据');
		return;
	}
	if (endEditing()) {
		deptGrid.treegrid('select', selected.id).treegrid('beginEdit', selected.id);
		editId = selected.id;
	}
}

// 根据主键删除
function del() {
	if (!dataAuth.deleteDept) {
		return;
	}
	var selected = deptGrid.treegrid("getSelected");
	if (null == selected) {
		$.messager.alert('警告', '请选中一条数据');
		return;
	}
	$.messager.confirm('确认', '您确认想要删除记录吗？', function(r) {
				if (r) {
					$.ajax({
								type : "POST",
								url : '${contextPath!}/department/delete',
								data : {
									id : selected.id
								},
								processData : true,
								dataType : "json",
								async : true,
								success : function(data) {
									if (data.code == "200") {
										deptGrid.treegrid('remove', selected.id);
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

var columnFormatter = function(value, row) {
	var content = '<input type="button" id="btnSave' + row.id + '" style="margin:0px 4px;display:none;" value="保存" onclick="javascript:endEditing();">';
	content += '<input type="button" id="btnCancel' + row.id + '" style="margin:0px 4px;display:none;" value="取消" onclick="javascript:cancelEdit();">';
	return content;
};

// 全局按键事件
function getKey(e) {
	e = e || window.event;
	var keycode = e.which ? e.which : e.keyCode;
	switch (keycode) {
		case 46 :
			if (isEditing())
				return;
			var selected = deptGrid.treegrid("getSelected");
			if (selected && selected != null) {
				del();
			}
			break;
		case 13 :
			endEditing();
			break;
		case 27 :
			cancelEdit();
			break;
	}
}

/**
 * 双击行的处理方法
 * 
 * @param {}
 *            index 行索引
 * @param {}
 *            field 行数据
 */
function onDblClickRow(row) {
	openUpdate();
}

/**
 * 显示编辑行最后一列的操作按钮
 * 
 * @param {}
 *            index 行索引
 */
function showOptButtons(id) {
	$('#btnSave' + id + ',#btnCancel' + id).show();
}

/**
 * 显示编辑行最后一列的操作按钮
 * 
 * @param {}
 *            index 行索引
 */
function hideOptButtons(id) {
	$('#btnSave' + id + ',#btnCancel' + id).hide();
}

/**
 * 开始编辑行的毁掉函数
 * 
 * @param {}
 *            index 行索引
 * @param {}
 *            row 行数据
 */
function onBeforeEdit(row) {
	if (row.id != 'temp') {
		window.oldRecord = new Object();
		$.extend(true, oldRecord, row);
	}
	showOptButtons(row.id);
	deptGrid.treegrid('showColumn', 'opt');
}

/**
 * 结束编辑回调函数
 * 
 * @param {}
 *            index 行索引
 * @param {}
 *            row 行数据
 * @param {}
 *            changes 当前行被修改的数据
 */
function onAfterEdit(row, changes) {
	var isValid = deptGrid.treegrid('validateRow', row.id);
	if (!isValid) {
		return false;
	}
	hideOptButtons(row.id);
	showOptButtons(row.id);
	insertOrUpdateDept(row, changes);
	oldRecord = undefined;
}

/**
 * 插入或者修改资源信息
 * 
 * @param {}
 *            node 菜单树被选中的节点
 * @param {}
 *            index 行索引
 * @param {}
 *            row 行数据
 * @param {}
 *            changes 被修改的数据
 */
function insertOrUpdateDept(row, changes) {
	var oldRecord = window.oldRecord;
	var postData = new Object();
	$.extend(true, postData, row);
	var url = '${contextPath!}/department/';
	if (postData.id == 'temp') {
		postData.id = undefined;
		postData.parentId = row._parentId
		url += 'insert';
	} else {
		url += 'update'
	}
	$.post(url, postData, function(data) {
				if (data.code != 200) {
					if (oldRecord) {
						deptGrid.treegrid('update', {
									id : row.id,
									row : oldRecord
								});
					} else {
						deptGrid.treegrid('remove', row.id);
					}
					$.messager.alert('提示', data.result);
					return;
				}
				if (postData.id == undefined) {
					deptGrid.treegrid('remove', 'temp');
					deptGrid.treegrid('append', {
								parent : data.data.parentId,
								data : [data.data]
							});
				} else {
					deptGrid.treegrid('update', {
								id : postData.id,
								row : data.data
							});
				}
			}, 'json');
}

/**
 * 单击含回调方法，逻辑是结束之前的行编辑模式
 * 
 * @param {}
 *            index
 * @param {}
 *            row
 */
function onClickRow(row) {
	cancelEdit();
}

var loadFilter = function(data, parentId) {
	if (parentId != undefined) {
		return data;
	}
	var getChildren = function(parent) {
		var children = new Array();
		$(data).each(function(i, el) {
					if (parent.id == el.parentId) {
						var obj = new Object();
						$.extend(true, obj, el);
						obj.children = getChildren(obj);
						children.push(obj);
					}
				});
		return children;
	};
	var target = new Array();
	$(data).each(function(i, el) {
				if (el.id == -1) {
					var obj = new Object();
					$.extend(true, obj, el);
					obj.children = getChildren(obj);
					target.push(obj);
					return false;
				}
			});
	return target;
}

/**
 * 取消行编辑
 */
function cancelEdit() {
	oldRecord = undefined;
	if (editId == undefined) {
		return;
	}
	deptGrid.treegrid('cancelEdit', editId);
};

/**
 * 取消行编辑回调方法
 * 
 * @param {}
 *            index
 * @param {}
 *            row
 */
function onCancelEdit(row) {
	editId = undefined;
	if (row.id == 'temp') {
		deptGrid.treegrid('remove', row.id);
		return;
	}
	hideOptButtons(row.id);
	deptGrid.treegrid('hideColumn', 'opt');
}

/**
 * 结束编辑回调方法
 * 
 * @param {}
 *            index
 * @param {}
 *            row
 */
function onEndEdit(row) {
	hideOptButtons(row.id);
	deptGrid.treegrid('hideColumn', 'opt');
}

/**
 * 绑定页面回车事件，以及初始化页面时的光标定位
 * 
 * @formId 表单ID
 * @elementName 光标定位在指点表单元素的name属性的值
 * @submitFun 表单提交需执行的任务
 */
$(function() {
			window.deptGrid = $('#grid');
			if (document.addEventListener) {
				document.addEventListener("keyup", getKey, false);
			} else if (document.attachEvent) {
				document.attachEvent("onkeyup", getKey);
			} else {
				document.onkeyup = getKey;
			}

		});