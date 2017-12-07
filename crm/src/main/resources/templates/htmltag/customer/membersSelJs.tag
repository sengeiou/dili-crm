<script type="text/javascript">
    //============================   选择成员客户,必填参数:dlgId, 后台必须传入当前customer对象  =============================
    // 打开选择成员客户弹出框
    function _selectMembers() {
        <%if (has(customer)){%>
            var selected = ${customer};
            showMembersDlg(selected.id);
        <%}%>
    }

    // 确认选择事件
    function confirmMembersBtn(id) {
        saveOrUpdateMembers(id);
        closeMembersSelectDlg();
    }

    //关闭成员客户选择窗口
    function closeMembersSelectDlg(){
        $('#${dlgId}').dialog('close');
    }

    // 根据id打开成员客户选择
    function showMembersDlg(id) {
        $('#${dlgId}').dialog({
            title : '客户选择',
            width : 800,
            height : 480,
            href : '${contextPath!}/customer/members.html',
            queryParams : {
                id : id
            },
            modal : true,
            buttons : [{
                text : '确定',
                iconCls:"icon-ok",
                handler : function() {
                    confirmMembersBtn($("#selectMembersGrid").datagrid("getSelected")["id"]);
                }
            }, {
                text : '取消',
                iconCls:"icon-cancel",
                handler : function() {
                    closeMembersSelectDlg();
                }
            }]
        });
    }
</script>