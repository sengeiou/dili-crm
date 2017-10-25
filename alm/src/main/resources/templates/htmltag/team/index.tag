// 编辑行索引
var editIndex = undefined;

function isEditing() {
	return undefined != editIndex;
}

// 结束行编辑
function endEditing() {
	if (editIndex == undefined) {
		return true
	}
	if (teamGrid.datagrid('validateRow', editIndex)) {
		teamGrid.datagrid('endEdit', editIndex);
		editIndex = undefined;
		return true;
	} else {
		return false;
	}
}

// 新增一行空数据并开启编辑模式
function openInsert() {
	if (!dataAuth.addMenu) {
		return;
	}
	if (!endEditing()) {
		$.messager.alert('警告', '有数据正在编辑');
		return;
	}
	editIndex = teamGrid.datagrid('getRows').length;
	teamGrid.datagrid('appendRow', {
				type : 0
			});
	teamGrid.datagrid('selectRow', editIndex);
	teamGrid.datagrid('beginEdit', editIndex);
}

// 开启选中行的编辑模式
function openUpdate() {
	if (!dataAuth.editMenu) {
		return;
	}
	var selected = teamGrid.datagrid("getSelected");
	if (!selected) {
		$.messager.alert('警告', '请选中一条数据');
		return;
	}
	var index = teamGrid.datagrid('getRowIndex', selected);
	if (endEditing()) {
		teamGrid.datagrid('selectRow', index).datagrid('beginEdit', index);
		editIndex = index;
	}
}

// 根据主键删除
function del() {
	if (!dataAuth.deleteMenu) {
		return;
	}
	var selected = teamGrid.datagrid("getSelected");
	if (null == selected) {
		$.messager.alert('警告', '请选中一条数据');
		return;
	}
	$.messager.confirm('确认', '您确认想要删除记录吗？', function(r) {
				if (r) {
					$.ajax({
								type : "POST",
								url : '${contextPath!}/team/delete',
								data : {
									id : selected.id
								},
								processData : true,
								dataType : "json",
								async : true,
								success : function(data) {
									if (data.code == "200") {
										teamGrid.datagrid('deleteRow', teamGrid.datagrid('getRowIndex', selected));
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

/**
 * 查询表格数据，因为menu下面包含resource，所以menu和resource的列头是不一样的
 * 
 * @param {}
 *            node 页面布局左边树形菜单节点数据，根据这个参数来判断是显示menu还是resource
 */
function queryGrid() {
	cancelEdit();
	teamGrid.datagrid({
				url : '${contextPath!}/resource/list',
				queryParams : {
					menuId : node.id
				},
				columns : [[{
							field : 'id',
							title : 'id',
							hidden : true
						}, {
							field : 'name',
							title : '权限名称',
							width : '20%',
							editor : {
								type : 'textbox',
								options : {
									required : true
								}
							}
						}, {
							field : 'code',
							title : '权限代码',
							width : '20%',
							editor : {
								type : 'textbox',
								options : {
									required : true
								}
							}
						}, {
							field : 'description',
							title : '描述',
							width : '60%',
							editor : 'textbox'

						}, {
							field : 'opt',
							title : '操作',
							width : '10%',
							hidden : true,
							formatter : function(value, row, index) {
								var content = '<input type="button" id="btnSave' + index + '" style="margin:0px 4px;display:none;" value="保存" onclick="javascript:endEditing();">';
								content += '<input type="button" id="btnCancel' + index + '" style="margin:0px 4px;display:none;" value="取消" onclick="javascript:cancelEdit();">';
								return content;
							}
						}]]
			});
}

// 全局按键事件
function getKey(e) {
	e = e || window.event;
	var keycode = e.which ? e.which : e.keyCode;
	switch (keycode) {
		case 46 :
			if (isEditing())
				return;
			var selected = teamGrid.datagrid("getSelected");
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
		case 38 :
			if (!endEditing()) {
				return;
			}
			var selected = teamGrid.datagrid("getSelected");
			if (!selected) {
				return;
			}
			var selectedIndex = teamGrid.datagrid('getRowIndex', selected);
			if (selectedIndex <= 0) {
				return;
			}
			endEditing();
			teamGrid.datagrid('selectRow', --selectedIndex);
			break;
		case 40 :
			if (!endEditing()) {
				return;
			}
			if (teamGrid.datagrid('getRows').length <= 0) {
				openInsert();
				return;
			}
			var selected = teamGrid.datagrid("getSelected");
			if (!selected) {
				teamGrid.datagrid('selectRow', 0);
				return;
			}
			var selectedIndex = teamGrid.datagrid('getRowIndex', selected);
			if (selectedIndex == teamGrid.datagrid('getRows').length - 1) {
				openInsert();
			} else {
				teamGrid.datagrid('selectRow', ++selectedIndex);
			}
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
function onDblClickRow(index, field) {
	openUpdate();
}

/**
 * 显示编辑行最后一列的操作按钮
 * 
 * @param {}
 *            index 行索引
 */
function showOptButtons(index) {
	$('#btnSave' + index + ',#btnCancel' + index).show();
}

/**
 * 显示编辑行最后一列的操作按钮
 * 
 * @param {}
 *            index 行索引
 */
function hideOptButtons(index) {
	$('#btnSave' + index + ',#btnCancel' + index).hide();
}

/**
 * 开始编辑行的毁掉函数
 * 
 * @param {}
 *            index 行索引
 * @param {}
 *            row 行数据
 */
function onBeginEdit(index, row) {
	showOptButtons(index);
	if (gridType == 1) {
		teamGrid.datagrid('resizeColumn', {
					field : 'description',
					width : '40%'
				});
	} else {
		teamGrid.datagrid('resizeColumn', [{
							field : 'menuUrl',
							width : '30%'
						}, {
							field : 'description',
							width : '35%'
						}]);
	}

	teamGrid.datagrid('showColumn', 'opt');
	var editors = teamGrid.datagrid('getEditors', index);
	editors[0].target.trigger('focus');
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
function onAfterEdit(index, row, changes) {
	var isValid = teamGrid.datagrid('validateRow', index);
	if (!isValid) {
		return false;
	}
	hideOptButtons(index);
	insertOrUpdateMenu(selectedTreeNode, index, row, changes);
}

/**
 * 插入或者修改菜单信息
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
function insertOrUpdateMenu(node, index, row, changes) {
	var oldRecord;
	var url = '${contextPath!}/menu/';
	var parentId = node.id;
	if (!row.id) {
		row.parentId = parentId;
		url += 'insert';
	} else {
		oldRecord = new Object();
		$.extend(true, oldRecord, row);
		url += 'update'
	}
	$.post(url, row, function(data) {
				if (data.code != 200) {
					if (oldRecord) {
						teamGrid.datagrid('updateRow', {
									index : index,
									row : oldRecord
								});
					} else {
						teamGrid.datagrid('deleteRow', index);
					}
					$.messager.alert('提示', data.result);
					return;
				}
				if (!row.id) {
					row.id = data.data.id;
				}
				teamGrid.datagrid('updateRow', {
							index : index,
							row : row
						});
				if (changes.orderNumber) {
					teamGrid.datagrid('orderNumber', {
								sortName : 'orderNumber',
								sortOrder : 'asc'
							});
				}
				teamGrid.datagrid('refreshRow', index);
			}, 'json');
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
function insertOrUpdateResource(node, index, row, changes) {
	var oldRecord;
	var url = '${contextPath!}/resource/';
	var menuId = node.id;
	if (!row.id) {
		row.menuId = menuId;
		url += 'insert';
	} else {
		url += 'update'
		oldRecord = new Object();
		$.extend(true, oldRecord, row);
	}
	$.post(url, row, function(data) {
				if (data.code != 200) {
					if (oldRecord) {
						teamGrid.datagrid('updateRow', {
									index : index,
									row : oldRecord
								});
					} else {
						teamGrid.datagrid('deleteRow', index);
					}
					$.messager.alert('提示', data.result);
					return;
				}
				if (!row.id) {
					row.id = data.data.id;
				}
				teamGrid.datagrid('updateRow', {
							index : index,
							row : row
						});
				teamGrid.datagrid('refreshRow', index);
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
function onClickRow(index, row) {
	if (editIndex == index) {
		return;
	}
	if (endEditing()) {
		return;
	}
	teamGrid.datagrid('cancelEdit', editIndex);
	if (!row.id) {
		teamGrid.datagrid('deleteRow', editIndex);
		editIndex = undefined;
	}
}

/**
 * 取消行编辑
 */
function cancelEdit() {
	if (editIndex == undefined) {
		return;
	}
	teamGrid.datagrid('cancelEdit', editIndex);
	editIndex = undefined;
};

/**
 * 取消行编辑回调方法
 * 
 * @param {}
 *            index
 * @param {}
 *            row
 */
function onCancelEdit(index, row) {
	if (!row.id) {
		teamGrid.datagrid('deleteRow', index);
	}
	hideOptButtons(index);
	if (gridType == 1) {
		teamGrid.datagrid('resizeColumn', [{
							field : 'description',
							width : '60%'
						}]);
	} else {
		teamGrid.datagrid('resizeColumn', [{
							field : 'menuUrl',
							width : '35%'
						}, {
							field : 'description',
							width : '40%'
						}]);
	}
	teamGrid.datagrid('hideColumn', 'opt');
}

/**
 * 结束编辑回调方法
 * 
 * @param {}
 *            index
 * @param {}
 *            row
 */
function onEndEdit(index, row) {
	hideOptButtons(index);
	if (gridType == 1) {
		teamGrid.datagrid('resizeColumn', [{
							field : 'description',
							width : '60%'
						}]);
	} else {
		teamGrid.datagrid('resizeColumn', [{
							field : 'menuUrl',
							width : '35%'
						}, {
							field : 'description',
							width : '40%'
						}]);
	}
	teamGrid.datagrid('hideColumn', 'opt');
}

/**
 * 绑定页面回车事件，以及初始化页面时的光标定位
 * 
 * @formId 表单ID
 * @elementName 光标定位在指点表单元素的name属性的值
 * @submitFun 表单提交需执行的任务
 */
$(function() {
			window.teamGrid = $('#grid');
			if (document.addEventListener) {
				document.addEventListener("keyup", getKey, false);
			} else if (document.attachEvent) {
				document.attachEvent("onkeyup", getKey);
			} else {
				document.onkeyup = getKey;
			}

		});