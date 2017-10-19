var gridType;
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
	if (menuGrid.datagrid('validateRow', editIndex)) {
		menuGrid.datagrid('endEdit', editIndex);
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
	editIndex = menuGrid.datagrid('getRows').length;
	menuGrid.datagrid('appendRow', {
				type : 0
			});
	menuGrid.datagrid('selectRow', editIndex);
	menuGrid.datagrid('beginEdit', editIndex);
}

// 开启选中行的编辑模式
function openUpdate() {
	if (!dataAuth.editMenu) {
		return;
	}
	var selected = menuGrid.datagrid("getSelected");
	if (!selected) {
		$.messager.alert('警告', '请选中一条数据');
		return;
	}
	var index = menuGrid.datagrid('getRowIndex', selected);
	if (endEditing()) {
		menuGrid.datagrid('selectRow', index).datagrid('beginEdit', index);
		editIndex = index;
	}
}

// 根据主键删除
function del() {
	if (!dataAuth.deleteMenu) {
		return;
	}
	var selected = menuGrid.datagrid("getSelected");
	if (null == selected) {
		$.messager.alert('警告', '请选中一条数据');
		return;
	}
	$.messager.confirm('确认', '您确认想要删除记录吗？', function(r) {
				if (r) {
					$.ajax({
								type : "POST",
								url : (gridType == 1 ? '${contextPath!}/resource/' : '${contextPath!}/menu/') + '/delete',
								data : {
									id : selected.id
								},
								processData : true,
								dataType : "json",
								async : true,
								success : function(data) {
									if (data.code == "200") {
										menuGrid.datagrid('deleteRow', menuGrid.datagrid('getRowIndex', selected));
										$('#dlg').dialog('close');
										if (gridType == 0) {
											var removeNode = menuTree.tree('find', selected.id);
											menuTree.tree('remove', removeNode.target);
										}
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
function queryGrid(node) {
	cancelEdit();
	gridType = node.attributes.type;
	if (node.attributes.type == 1) {
		menuGrid.datagrid({
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
	} else if (node.attributes.type == 0) {
		menuGrid.datagrid({
					url : '${contextPath!}/menu/list',
					queryParams : {
						parentId : node.id
					},
					columns : [[{
								field : 'id',
								title : 'id',
								hidden : true
							}, {
								field : 'name',
								title : '菜单名称',
								width : '10%',
								editor : {
									type : 'textbox',
									options : {
										required : true,
										validType : 'length[1, 50]',
										missingMessage : '菜单名称不能为空',
										invalidMessage : '菜单名称必须是1-50个字符'
									}
								}
							}, {
								field : 'type',
								title : '类型',
								width : '10%',
								formatter : function(value, row, index) {
									if (value == 0) {
										return '目录';
									} else if (value == 1) {
										return '链接';
									}
								},
								editor : {
									type : 'combobox',
									options : {
										valueField : 'value',
										textField : 'name',
										editable : false,
										data : [{
													name : '目录',
													value : 0
												}, {
													name : '链接',
													value : 1
												}]
									}
								}

							}, {
								field : 'menuUrl',
								title : '菜单链接地址',
								width : '35%',
								editor : {
									type : 'textbox'
								}
							}, {
								field : 'description',
								title : '描述',
								width : '40%',
								editor : 'text'
							}, {
								field : 'orderNumber',
								title : '排序',
								width : '5%',
								editor : {
									type : 'numberbox'									
								}
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
}

// 全局按键事件
function getKey(e) {
	e = e || window.event;
	var keycode = e.which ? e.which : e.keyCode;
	switch (keycode) {
		case 46 :
			if (isEditing())
				return;
			var selected = menuGrid.datagrid("getSelected");
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
			var selected = menuGrid.datagrid("getSelected");
			if (!selected) {
				return;
			}
			var selectedIndex = menuGrid.datagrid('getRowIndex', selected);
			if (selectedIndex <= 0) {
				return;
			}
			endEditing();
			menuGrid.datagrid('selectRow', --selectedIndex);
			break;
		case 40 :
			if (!endEditing()) {
				return;
			}
			if (menuGrid.datagrid('getRows').length <= 0) {
				openInsert();
				return;
			}
			var selected = menuGrid.datagrid("getSelected");
			if (!selected) {
				menuGrid.datagrid('selectRow', 0);
				return;
			}
			var selectedIndex = menuGrid.datagrid('getRowIndex', selected);
			if (selectedIndex == menuGrid.datagrid('getRows').length - 1) {
				openInsert();
			} else {
				menuGrid.datagrid('selectRow', ++selectedIndex);
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
		menuGrid.datagrid('resizeColumn', {
					field : 'description',
					width : '40%'
				});
	} else {
		menuGrid.datagrid('resizeColumn', [{
							field : 'menuUrl',
							width : '30%'
						}, {
							field : 'description',
							width : '35%'
						}]);
	}

	menuGrid.datagrid('showColumn', 'opt');
	var editors = menuGrid.datagrid('getEditors', index);
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
	var isValid = menuGrid.datagrid('validateRow', index);
	if (!isValid) {
		return false;
	}
	hideOptButtons(index);
	var selectedTreeNode = menuTree.tree('getSelected');
	if (selectedTreeNode.attributes.type == 0) {
		insertOrUpdateMenu(selectedTreeNode, index, row, changes);
	} else if (selectedTreeNode.attributes.type == 1) {
		insertOrUpdateResource(selectedTreeNode, index, row, changes);
	}
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
						menuGrid.datagrid('updateRow', {
									index : index,
									row : oldRecord
								});
					} else {
						menuGrid.datagrid('deleteRow', index);
					}
					$.messager.alert('提示', data.result);
					return;
				}
				if (!row.id) {
					row.id = data.data.id;
					menuTree.tree('append', {
								parent : node.target,
								data : [{
											id : row.id,
											parentId : row.parentId,
											name : row.name,
											attributes : {
												type : row.type
											}
										}]
							});
				} else {
					menuTree.tree('update', {
								target : node.target,
								text : row.name
							});
				}
				menuGrid.datagrid('updateRow', {
							index : index,
							row : row
						});
				if (changes.orderNumber) {
					menuGrid.datagrid('orderNumber', {
								sortName : 'orderNumber',
								sortOrder : 'asc'
							});
				}
				menuGrid.datagrid('refreshRow', index);
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
						menuGrid.datagrid('updateRow', {
									index : index,
									row : oldRecord
								});
					} else {
						menuGrid.datagrid('deleteRow', index);
					}
					$.messager.alert('提示', data.result);
					return;
				}
				if (!row.id) {
					row.id = data.data.id;
				}
				menuGrid.datagrid('updateRow', {
							index : index,
							row : row
						});
				menuGrid.datagrid('refreshRow', index);
			}, 'json');
}

var selectedTreeId = undefined;
/**
 * 页面加载完毕后默认选中菜单树的根节点节点
 * 
 * @param {}
 *            node
 * @param {}
 *            data
 */
function onLoadSuccess(node, data) {
	if (!selectedTreeId) {
		selectedTreeId = -1;
	}
	var selectedNode = menuTree.tree("find", selectedTreeId);
	menuTree.tree("select", selectedNode.target);
	queryGrid(selectedNode);
};

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
	menuGrid.datagrid('cancelEdit', editIndex);
	if (!row.id) {
		menuGrid.datagrid('deleteRow', editIndex);
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
	menuGrid.datagrid('cancelEdit', editIndex);
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
		menuGrid.datagrid('deleteRow', index);
	}
	hideOptButtons(index);
	if (gridType == 1) {
		menuGrid.datagrid('resizeColumn', [{
							field : 'description',
							width : '60%'
						}]);
	} else {
		menuGrid.datagrid('resizeColumn', [{
							field : 'menuUrl',
							width : '35%'
						}, {
							field : 'description',
							width : '40%'
						}]);
	}
	menuGrid.datagrid('hideColumn', 'opt');
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
		menuGrid.datagrid('resizeColumn', [{
							field : 'description',
							width : '60%'
						}]);
	} else {
		menuGrid.datagrid('resizeColumn', [{
							field : 'menuUrl',
							width : '35%'
						}, {
							field : 'description',
							width : '40%'
						}]);
	}
	menuGrid.datagrid('hideColumn', 'opt');
}

/**
 * 绑定页面回车事件，以及初始化页面时的光标定位
 * 
 * @formId 表单ID
 * @elementName 光标定位在指点表单元素的name属性的值
 * @submitFun 表单提交需执行的任务
 */
$(function() {

			window.menuGrid = $('#grid');
			window.menuTree = $('#menuTree');
			if (document.addEventListener) {
				document.addEventListener("keyup", getKey, false);
			} else if (document.attachEvent) {
				document.attachEvent("onkeyup", getKey);
			} else {
				document.onkeyup = getKey;
			}

		});