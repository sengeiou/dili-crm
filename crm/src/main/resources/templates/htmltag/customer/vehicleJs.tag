<script type="text/javascript">
    // ============================   车辆相关js st  =============================

    //修改客户窗口打开时查询车辆
    function listVehicle(){
    <%if (has(customer)){%>
            var opts = $("#vehicleGrid").datagrid("options");
            if (null == opts.url || "" == opts.url) {
                opts.url = "${contextPath}/vehicle/listPage.action";
            }
            var selected = ${customer};
            $("#vehicleGrid").datagrid("load",  bindGridMeta2Data('vehicleGrid',{"customerId":selected["id"]}));
        <%}%>
    }

    <%if(has(action) && (action=="edit" || action=="add")){%>
    //选择车辆之前切换反相色的图标
    function onBeforeSelectVehicle(index, row) {
        //获取当前选中的行索引
        var rowIndex = $("#vehicleGrid").datagrid("getRowIndex", $("#vehicleGrid").datagrid("getSelected"));
        $("#vehicleUpdate"+rowIndex).attr("src", "${contextPath}/resources/easyui/css/themes/icons/pencil.png");
        $("#vehicleRemove"+rowIndex).attr("src", "${contextPath}/resources/easyui/css/themes/icons/edit_remove.png");
        //index为即将选中的行索引
        $("#vehicleUpdate"+index).attr("src", "${contextPath}/resources/easyui/css/themes/icons/pencil-on.png");
        $("#vehicleRemove"+index).attr("src", "${contextPath}/resources/easyui/css/themes/icons/edit_remove-on.png");
    }

    function vehicleOptFmt(value,row,index) {
        return "<img src='${contextPath}/resources/easyui/css/themes/icons/pencil.png' style='cursor: pointer' id='vehicleUpdate"+index+"' onclick='openUpdateVehicle("+index+")'/>&nbsp;&nbsp;&nbsp;"
            + "    <img src='${contextPath}/resources/easyui/css/themes/icons/edit_remove.png' style='cursor: pointer' id='vehicleRemove"+index+"' onclick='delVehicle(\""+row["id"]+"\")'/>";
    }
    //新增客户时,打开插入车辆框
    function openInsertVehicle() {
        $('#vehicleDlg').dialog('open');
        $('#vehicleDlg').dialog('center');
        $('#_vehicleForm').form('clear');
        formFocus("_vehicleForm", "_vehicle_name");
    }

    //修改客户时,打开修改车辆窗口
    function openUpdateVehicle(index){
        $("#vehicleGrid").datagrid("selectRow", index);
        var selected = $("#vehicleGrid").datagrid("getSelected");
        if (null == selected) {
            $.messager.alert('警告','请选中一条数据');
            return;
        }
        $('#vehicleDlg').dialog('open');
        $('#vehicleDlg').dialog('center');
        formFocus("_vehicleForm", "_vehicle_name");
        var formData = $.extend({},selected);
        formData = addKeyStartWith(getOriginalData(formData),"_vehicle_");
        $('#_vehicleForm').form('load', formData);
    }

    //保存车辆
    function saveOrUpdateVehicle() {
        <%if (has(customer)){%>
        if(!$('#_vehicleForm').form("validate")){
            return;
        }
        var _formData = removeKeyStartWith($("#_vehicleForm").serializeObject(),"_vehicle_");
        var _url = null;
        //没有id就新增
        if(_formData.id == null || _formData.id==""){
            _url = "${contextPath}/vehicle/insert.action";
        }else{//有id就修改
            _url = "${contextPath}/vehicle/update.action";
        }
        //设置当前选择的客户id
        var selected = ${customer};
        _formData["customerId"] = selected["id"];
        $('#vehicleDlg').dialog('close');
        $.ajax({
            type: "POST",
            url: _url,
            data: _formData,
            processData:true,
            dataType: "json",
            async : true,
            success: function (data) {
                if(data.code=="200"){
                    $("#vehicleGrid").datagrid("reload");
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

    //根据主键删除车辆
    function delVehicle(id) {
        var selectedId;
        if(id){
            selectedId = id;
        }else{
            var selected = $("#vehicleGrid").datagrid("getSelected");
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
                    url: "${contextPath}/vehicle/delete.action",
                    data: {id:selectedId},
                    processData:true,
                    dataType: "json",
                    async : true,
                    success: function (data) {
                        if(data.code=="200"){
                            $("#vehicleGrid").datagrid("reload");
                            $('#vehicleDlg').dialog('close');
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
    // ============================   车辆相关js end  =============================
</script>