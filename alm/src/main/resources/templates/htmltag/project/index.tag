// 编辑行索引
var editId = undefined;

Date.prototype.Format = function(fmt) { // author: meizz
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"H+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
		// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
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
				if (!el.parentId) {
					var obj = new Object();
					$.extend(true, obj, el);
					obj.children = getChildren(obj);
					target.push(obj);
				}
			});
	return target;
}

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
function openInsert(isRoot) {
	if (!endEditing()) {
		$.messager.alert('警告', '有数据正在编辑');
		return;
	}
	var node = undefined;
	if (!isRoot) {
		node = projectGrid.treegrid('getSelected');
		if (!node) {
			$.messager.alert('警告', '请选择一条记录');
			return;
		}
	}
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
	var selected = projectGrid.treegrid("getSelected");
	if (null == selected) {
		$.messager.alert('警告', '请选中一条数据');
		return;
	}
	$.messager.confirm('确认', '您确认想要删除记录吗？', function(r) {
				if (r) {
					$.ajax({
								type : "POST",
								url : '${contextPath!}/project/delete',
								data : {
									id : selected.id
								},
								processData : true,
								dataType : "json",
								async : true,
								success : function(data) {
									if (data.code == "200") {
										projectGrid.treegrid('remove', selected.id);
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
	debugger;
	var opts = projectGrid.datagrid("options");
	if (null == opts.url || "" == opts.url) {
		opts.url = "${contextPath!}/project/list";
	}
	if (!$('#form').form("validate")) {
		return;
	}
	var param = bindMetadata("grid", true);
	var formData = $("#form").serializeObject();
	$.extend(param, formData);
	projectGrid.treegrid("load", param);
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
	switch (keycode) {
		case 46 :
			if (isEditing())
				return;
			var selected = projectGrid.treegrid("getSelected");
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

function resizeColumn(original) {
	if (original) {
		projectGrid.datagrid('resizeColumn', [{
							field : 'name',
							width : '15%'
						}, {
							field : 'projectManager',
							width : '10%'
						}, {
							field : 'testManager',
							width : '10%'
						}, {
							field : 'productManager',
							width : '10%'
						}]);
	} else {
		projectGrid.datagrid('resizeColumn', [{
							field : 'name',
							width : '20%'
						}, {
							field : 'projectManager',
							width : '15%'
						}, {
							field : 'testManager',
							width : '15%'
						}, {
							field : 'productManager',
							width : '15%'
						}]);
	}
}

function onBeginEdit(row) {
	var fn = function(txtbox) {
		$(members).each(function(index, item) {
					if (item.id == txtbox.textbox('getValue')) {
						txtbox.textbox('setText', item.realName);
						return false;
					}
				});
	};
	var editor = projectGrid.datagrid('getEditor', {
				id : row.id,
				field : 'projectManager'
			});
	fn.call(this, editor.target)
	editor = projectGrid.datagrid('getEditor', {
				id : row.id,
				field : 'testManager'
			});
	fn.call(this, editor.target);
	editor = projectGrid.datagrid('getEditor', {
				id : row.id,
				field : 'productManager'
			});
	fn.call(this, editor.target);
}

function selectMember(field) {
	window.smDialog = $('#smDialog');
	smDialog.dialog({
				title : '用户选择',
				width : 400,
				height : 200,
				href : '${contextPath!}/project/members',
				modal : true,
				buttons : [{
							text : '确定',
							handler : function() {
								var selected = memberList.datalist('getSelected');
								var editor = projectGrid.datagrid('getEditor', {
											index : editId,
											field : field
										});
								editor.target.textbox('setValue', selected.value);
								editor.target.textbox('setText', selected.text);
								smDialog.dialog('close');
							}
						}, {
							text : '取消',
							handler : function() {
								smDialog.dialog('close');
							}
						}],
				onLoad : function() {
					window.memberList = $('#smGridList');
				}
			});
}

function selectFormMember(id) {
	window.smDialog = $('#smDialog');
	smDialog.dialog({
				title : '用户选择',
				width : 400,
				height : 200,
				href : '${contextPath!}/project/members',
				modal : true,
				buttons : [{
							text : '确定',
							handler : function() {
								var selected = memberList.datalist('getSelected');
								$('#' + id).textbox('setValue', selected.value);
								$('#' + id).textbox('setText', selected.text);
								smDialog.dialog('close');
							}
						}, {
							text : '取消',
							handler : function() {
								smDialog.dialog('close');
							}
						}],
				onLoad : function() {
					window.memberList = $('#smGridList');
				}
			});
}

function selectProjectManager() {
	selectMember('projectManager');
}

function selectTestManager() {
	selectMember('testManager');
}

function selectProductManager() {
	selectMember('productManager');
}

function selectFormProjectManager() {
	selectFormMember('projectManager');
}

function selectFormTestManager() {
	selectFormMember('testManager');
}

function selectFormProductManager() {
	selectFormMember('productManager');
}

var columnFormatter = function(value, row) {
	var content = '<input type="button" id="btnSave' + row.id + '" style="margin:0px 4px;display:none;" value="保存" onclick="javascript:endEditing();">';
	content += '<input type="button" id="btnCancel' + row.id + '" style="margin:0px 4px;display:none;" value="取消" onclick="javascript:cancelEdit();">';
	return content;
};

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
	resizeColumn();
	hideColumn();
	showOptButtons(row.id);
	projectGrid.treegrid('showColumn', 'opt');
}

function hideColumn() {
	projectGrid.treegrid('hideColumn', 'created');
	projectGrid.treegrid('hideColumn', 'modified');
}

function showColumn() {
	projectGrid.treegrid('showColumn', 'created');
	projectGrid.treegrid('showColumn', 'modified');
}

/**
 * 取消行编辑
 */
function cancelEdit() {
	oldRecord = undefined;
	if (editId == undefined) {
		return;
	}
	projectGrid.treegrid('cancelEdit', editId);
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
		projectGrid.treegrid('remove', row.id);
		return;
	}
	resizeColumn(true);
	hideOptButtons(row.id);
	projectGrid.treegrid('hideColumn', 'opt');
	showColumn();
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
	var isValid = projectGrid.treegrid('validateRow', row.id);
	if (!isValid) {
		return false;
	}
	hideOptButtons(row.id);
	showOptButtons(row.id);
	insertOrUpdateProject(row, changes);
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
function insertOrUpdateProject(row, changes) {
	var oldRecord = window.oldRecord;
	var postData = new Object();
	$.extend(true, postData, row);
	var url = '${contextPath!}/project/';
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
						projectGrid.treegrid('update', {
									id : row.id,
									row : oldRecord
								});
					} else {
						projectGrid.treegrid('remove', row.id);
					}
					$.messager.alert('提示', data.result);
					return;
				}
				var date = new Date().Format("yyyy-MM-dd HH:mm:ss");
				if (postData.id == undefined) {
					data.data.created = date;
					data.data.modified = date;
					projectGrid.treegrid('remove', 'temp');
					projectGrid.treegrid('append', {
								parent : data.data.parentId,
								data : [data.data]
							});
				} else {
					data.data.created = row.created;
					data.data.modified = date;
					projectGrid.treegrid('update', {
								id : postData.id,
								row : data.data
							});
				}
			}, 'json');
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
	resizeColumn(true);
	hideOptButtons(row.id);
	projectGrid.treegrid('hideColumn', 'opt');
	showColumn();
}

var projectTypes = undefined;
var projectTypesIsNull = undefined;

function projectColumnFormatter(value, row) {
	if (!projectTypes) {
		$.ajax({
					async : false,
					url : '${contextPath!}/project/type.json',
					success : function(res) {
						projectTypes = res;
						if (!projectTypes) {
							projectTypesIsNull = true;
						}
					}
				});
	}
	if (projectTypesIsNull) {
		return '';
	}
	var target = '';
	$(projectTypes).each(function(index, item) {
				if (item.id == value) {
					target = item.code;
					return false;
				}
			});
	return target;
}

var members = undefined;
var membersIsNull = undefined;

function memberColumnFormatter(value, row) {
	if (!members) {
		$.ajax({
					async : false,
					url : '${contextPath!}/project/members.json',
					success : function(res) {
						members = res;
						if (!members) {
							membersIsNull = true;
						}
					}
				});
	}
	if (membersIsNull) {
		return '';
	}
	var target = '';
	$(members).each(function(index, item) {
				if (item.id == value) {
					target = item.realName;
					return false;
				}
			});
	return target;
}

/**
 * 绑定页面回车事件，以及初始化页面时的光标定位
 * 
 * @formId 表单ID
 * @elementName 光标定位在指点表单元素的name属性的值
 * @submitFun 表单提交需执行的任务
 */
$(function() {
			bindFormEvent("form", "name", queryGrid);
			if (document.addEventListener) {
				document.addEventListener("keyup", getKey, false);
			} else if (document.attachEvent) {
				document.attachEvent("onkeyup", getKey);
			} else {
				document.onkeyup = getKey;
			}
			window.projectGrid = $('#grid');
		});