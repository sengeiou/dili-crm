<script type="text/javascript">
    //============================   选择成员客户,必填参数:dlgId  =============================
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
    function showMembersDlg() {
        $('#${dlgId}').dialog({
            title : '客户选择',
            width : 800,
            height : 400,
            href : '${contextPath!}/customer/members.html',
            modal : true,
            buttons : [{
                text : '确定',
                handler : function() {
                    confirmMembersBtn($("#selectCustomerGrid").datagrid("getSelected")["id"]);
                }
            }, {
                text : '取消',
                handler : function() {
                    closeMembersSelectDlg();
                }
            }]
        });
    }
</script>