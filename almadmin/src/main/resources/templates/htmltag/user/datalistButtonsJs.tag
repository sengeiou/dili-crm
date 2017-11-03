<script type="text/javascript">
	function moveRowByIndex(fromId, toId, index) {
		var data = $("#" + fromId).datalist("getData");
		if (1 > data.total) {
			$.messager.alert('警告', '沒有可选择的数据');
			return;
		}

		if (0 <= index) {
			$("#" + fromId).datalist("selectRow", index);
			var selected = $("#" + fromId).datalist("getSelected");
			moveRow(fromId, toId, selected);
			return;
		}

		$("#" + fromId).datalist('loadData', {
			total : 0,
			rows : []
		});
		$(data.rows).each(function(index, item) {
			$("#" + toId).datalist("appendRow", item);
		});
	}

	function moveRow(fromId, toId, row) {
		var index = $("#" + fromId).datalist("getRowIndex", row);
		$("#" + fromId).datalist("deleteRow", index);
		$("#" + toId).datalist("appendRow", row);
	}

	function moveSelectedRow(fromId, toId, isAlert) {
		var selected = $("#" + fromId).datalist("getSelected");
		if (null != selected) {
			// 有选中的记录，则直接操作
			moveRow(fromId, toId, selected);
			return;
		}

		// 没有选中的记录，再判断是告警还是默认选中第一行
		if (isAlert) {
			$.messager.alert('警告', '请选中一条数据');
		} else {
			moveRowByIndex(fromId, toId, 0);
		}
	}

	function addSelectedRole() {
		moveSelectedRow('withoutRoleDatalist', 'withRoleDatalist', true);
	}

	function delSelectedRole() {
		moveSelectedRow('withRoleDatalist', 'withoutRoleDatalist', true);
	}
</script>