<script type="text/javascript">
    // ============================   成员客户相关js st  =============================
    //新增用户时，存储当前用户有哪些成员客户，即：哪些用户的父客户需要改成当前用户ID
    var _members = [];
    //修改客户窗口打开时查询成员客户
    function listMembers(){
    <%if (has(customer)){%>
            var opts = $("#membersGrid").datagrid("options");
            if (null == opts.url || "" == opts.url) {
                opts.url = "${contextPath}/customer/listPage.action";
            }
            var selected = ${customer};
            $("#membersGrid").datagrid("load",  bindGridMeta2Data("membersGrid", {"parentId":selected["id"]}));
        <%}%>
    }
    <%if(has(action) && (action=="edit" || action=="add")){%>
    //选择成员客户之前切换反相色的图标
    function onBeforeSelectMembers(index, row) {
        //获取当前选中的行索引
        var rowIndex = $("#membersGrid").datagrid("getRowIndex", $("#membersGrid").datagrid("getSelected"));
        $("#membersRemove"+rowIndex).attr("src", "${contextPath}/resources/easyui/css/themes/icons/edit_remove.png");
        //index为即将选中的行索引
        $("#membersRemove"+index).attr("src", "${contextPath}/resources/easyui/css/themes/icons/edit_remove-on.png");
    }

    function membersOptFmt(value,row,index) {
        return "<img src='${contextPath}/resources/easyui/css/themes/icons/edit_remove.png' style='cursor: pointer' id='membersRemove"+index+"' onclick='delMembers("+index+")'/>";
    }

    //保存成员客户
    function saveOrUpdateMembers(row) {
        <%if (has(action) && action=="edit"){%>
        saveToDB(row.id);
        <%}else {%>
        updateMembersGrid(row);
        <%}%>
    }

    /**
     * 用户数据新增时，刷新grid信息
     * @param gridId
     * @param _formData
     */
    function updateMembersGrid(_rowData) {
        var _gridId = '#membersGrid';
        var index = $(_gridId).datagrid('getRowIndex', _rowData.id);
        if (null == index || index < 0){
            $(_gridId).datagrid('appendRow', _rowData);
            _members.push(_rowData.id);
        }
    }

    /**
     * 修改操作时，操作数据库，更改用户的父客户信息
     * @param id
     */
    function saveToDB(id) {
        var _formData = {id:id};
        var _url = "${contextPath}/customer/update.action";
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
            success: function (ret) {
                if(ret.success){
                    $("#membersGrid").datagrid("reload");
                }else{
                    $.messager.alert('错误',ret.result);
                }
            },
            error: function(){
                $.messager.alert('错误','远程访问失败');
            }
        });
    }

    //根据主键删除成员客户
    function delMembers(index) {
        $("#membersGrid").datagrid("selectRow", index);
        var selected = $("#membersGrid").datagrid("getSelected");
        if (null == selected) {
            $.messager.alert('警告','请选中一条数据');
            return;
        }
        var selectedId = selected.id;
        //修改用户时，因为是直接保存数据库，所以，这里也需要删除数据
        <%if (has(action) && action=="edit"){%>
        $.messager.confirm('确认','您确认想要删除成员客户吗？',function(r){
            if (r){
                $.ajax({
                    type: "POST",
                    url: "${contextPath}/customer/deleteMembers",
                    data: {id:selectedId},
                    processData:true,
                    dataType: "json",
                    async : true,
                    success: function (ret) {
                        if(ret.success){
                            $("#membersGrid").datagrid("reload");
                            $('#membersDlg').dialog('close');
                        }else{
                            $.messager.alert('错误',ret.result);
                        }
                    },
                    error: function(){
                        $.messager.alert('错误','远程访问失败');
                    }
                });
            }
        });
        <%}else {%>
        $.messager.confirm('确认','您确认想要删除成员客户吗？',function(r){
            if (r) {
                //新增用户时，只是记录用户ID，所以，此处需要删除grid，并且移除元素
                $("#membersGrid").datagrid("deleteRow", index);
                _members = $.grep(_members, function (n, i) {
                    return n != selectedId;
                });
            }
        });
        <%}%>
    }
    <%}%>
    // ============================   成员客户相关js end  =============================


</script>