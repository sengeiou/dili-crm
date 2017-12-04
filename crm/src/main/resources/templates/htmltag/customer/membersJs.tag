<script type="text/javascript">
    // ============================   成员客户相关js st  =============================

    //修改客户窗口打开时查询成员客户
    function listMembers(){
    <%if (has(customer)){%>
            var opts = $("#membersGrid").datagrid("options");
            if (null == opts.url || "" == opts.url) {
                opts.url = "${contextPath}/customer/listPage";
            }
            var selected = ${customer};
            $("#membersGrid").datagrid("load",  bindGridMeta2Data("membersGrid", {"parentId":selected["id"]}));
        <%}%>
    }
    <%if(has(action) && action=="edit"){%>
    //选择成员客户之前切换反相色的图标
    function onBeforeSelectMembers(index, row) {
        //获取当前选中的行索引
        var rowIndex = $("#membersGrid").datagrid("getRowIndex", $("#membersGrid").datagrid("getSelected"));
        $("#membersRemove"+rowIndex).attr("src", "${contextPath}/resources/easyui/css/themes/icons/edit_remove.png");
        //index为即将选中的行索引
        $("#membersRemove"+index).attr("src", "${contextPath}/resources/easyui/css/themes/icons/edit_remove-on.png");
    }

    function membersOptFmt(value,row,index) {
        return "<img src='${contextPath}/resources/easyui/css/themes/icons/edit_remove.png' style='cursor: pointer' id='membersRemove"+index+"' onclick='delMembers(\""+row["id"]+"\")'/>";
    }

    //保存成员客户
    function saveOrUpdateMembers(id) {
        <%if (has(customer)){%>
        var _formData = {id:id};
        var _url = "${contextPath}/customer/update";
        //设置当前选择的客户id
        var selected = ${customer};
        _formData["parentId"] = selected["id"];
        $.ajax({
            type: "POST",
            url: _url,
            data: _formData,
            processData:true,
            dataType: "json",
            async : true,
            success: function (data) {
                if(data.code=="200"){
                    $("#membersGrid").datagrid("reload");
                }else{
                    $.messager.alert('错误',data.result);
                }
            },
            error: function(){
                $.messager.alert('错误','远程访问失败');
            }
        });
        <%}%>
    }

    //根据主键删除成员客户
    function delMembers(id) {
        var selectedId;
        if(id){
            selectedId = id;
        }else{
            var selected = $("#membersGrid").datagrid("getSelected");
            if (null == selected) {
                $.messager.alert('警告','请选中一条数据');
                return;
            }
            selectedId = selected.id;
        }
        $.messager.confirm('确认','您确认想要删除成员客户吗？',function(r){
            if (r){
                $.ajax({
                    type: "POST",
                    url: "${contextPath}/customer/deleteMembers",
                    data: {id:selectedId},
                    processData:true,
                    dataType: "json",
                    async : true,
                    success: function (data) {
                        if(data.code=="200"){
                            $("#membersGrid").datagrid("reload");
                            $('#membersDlg').dialog('close');
                        }else{
                            $.messager.alert('错误',data.result);
                        }
                    },
                    error: function(){
                        $.messager.alert('错误','远程访问失败');
                    }
                });
            }
        });
    }
    <%}%>
    // ============================   成员客户相关js end  =============================


</script>