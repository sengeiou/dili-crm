<script type="text/javascript">
    // ============================   联系人相关js st  =============================
    //修改客户窗口打开时查询联系人
    function listContacts(){
    <%if (has(customer)){%>
            var opts = $("#contactsGrid").datagrid("options");
            if (null == opts.url || "" == opts.url) {
                opts.url = "${contextPath}/contacts/listPage.action";
            }
            var selected = ${customer};
            $("#contactsGrid").datagrid("load",  bindGridMeta2Data("contactsGrid", {"customerId":selected["id"]}));
        <%}%>
    }
    <%if(has(action) && (action=="edit" || action=="add")){%>
    //选择联系人之前切换反相色的图标
    function onBeforeSelectContacts(index, row) {
        //获取当前选中的行索引
        var rowIndex = $("#contactsGrid").datagrid("getRowIndex", $("#contactsGrid").datagrid("getSelected"));
        $("#contactsUpdate"+rowIndex).attr("src", "${contextPath}/resources/easyui/css/themes/icons/pencil.png");
        $("#contactsRemove"+rowIndex).attr("src", "${contextPath}/resources/easyui/css/themes/icons/edit_remove.png");
        //index为即将选中的行索引
        $("#contactsUpdate"+index).attr("src", "${contextPath}/resources/easyui/css/themes/icons/pencil-on.png");
        $("#contactsRemove"+index).attr("src", "${contextPath}/resources/easyui/css/themes/icons/edit_remove-on.png");
    }

    function contactsOptFmt(value,row,index) {
        return "<img src='${contextPath}/resources/easyui/css/themes/icons/pencil.png' style='cursor: pointer' id='contactsUpdate"+index+"' onclick='openUpdateContacts("+index+")'/>&nbsp;&nbsp;&nbsp;"
            + "    <img src='${contextPath}/resources/easyui/css/themes/icons/edit_remove.png' style='cursor: pointer' id='contactsRemove"+index+"' onclick='delContacts(\""+row["id"]+"\")'/>";
    }
    //新增客户时,打开插入联系人框
    function openInsertContacts() {
        $('#contactsDlg').dialog('open');
        $('#contactsDlg').dialog('center');
        $('#_contactsForm').form('clear');
        formFocus("_contactsForm", "_contacts_name");
    }

    //修改客户时,打开修改联系人窗口
    function openUpdateContacts(index){
        $("#contactsGrid").datagrid("selectRow", index);
        var selected = $("#contactsGrid").datagrid("getSelected");
        if (null == selected) {
            $.messager.alert('警告','请选中一条数据');
            return;
        }
        $('#contactsDlg').dialog('open');
        $('#contactsDlg').dialog('center');
        $('#_contactsForm').form('clear');
        formFocus("_contactsForm", "_contacts_name");
        var formData = $.extend({},selected);
        formData = addKeyStartWith(getOriginalData(formData),"_contacts_");
        $('#_contactsForm').form('load', formData);
    }

    //保存联系人
    function saveOrUpdateContacts() {
        <%if (has(customer)){%>
        if(!$('#_contactsForm').form("validate")){
            return;
        }
        var _formData = removeKeyStartWith($("#_contactsForm").serializeObject(),"_contacts_");
        var _url = null;
        //没有id就新增
        if(_formData.id == null || _formData.id==""){
            _url = "${contextPath}/contacts/insert.action";
        }else{//有id就修改
            _url = "${contextPath}/contacts/update.action";
        }
        //设置当前选择的客户id
        var selected = ${customer};
        _formData["customerId"] = selected["id"];
        $('#contactsDlg').dialog('close');
        $.ajax({
            type: "POST",
            url: _url,
            data: _formData,
            processData:true,
            dataType: "json",
            async : true,
            success: function (ret) {
                if(ret.success){
                    $("#contactsGrid").datagrid("reload");
                }else{
                    $.messager.alert('错误',ret.result);
                }
            },
            error: function(){
                $.messager.alert('错误','远程访问失败');
            }
        });
        <%}%>
    }

    //根据主键删除联系人
    function delContacts(id) {
        debugger;
        var selectedId;
        if(id){
            selectedId = id;
        }else{
            var selected = $("#contactsGrid").datagrid("getSelected");
            if (null == selected) {
                $.messager.alert('警告','请选中一条数据');
                return;
            }
            selectedId = selected.id;
        }
        $.messager.confirm('确认','您确认想要删除记录吗？',function(r){
            if (r){
                $.ajax({
                    type: "POST",
                    url: "${contextPath}/contacts/delete.action",
                    data: {id:selectedId},
                    processData:true,
                    dataType: "json",
                    async : true,
                    success: function (data) {
                        if(data.code=="200"){
                            $("#contactsGrid").datagrid("reload");
                            $('#contactsDlg').dialog('close');
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
    // ============================   联系人相关js end  =============================
</script>