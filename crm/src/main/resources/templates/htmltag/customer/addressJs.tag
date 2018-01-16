<script type="text/javascript">
    // ============================   地址相关js st  =============================

    //修改客户窗口打开时查询地址
    function listAddress(){
    <%if (has(customer)){%>
            var opts = $("#addressGrid").datagrid("options");
            if (null == opts.url || "" == opts.url) {
                opts.url = "${contextPath}/address/listPage";
            }
            var selected = ${customer};
            $("#addressGrid").datagrid("load", bindGridMeta2Data("addressGrid", {"customerId":selected["id"]}));
        <%}%>
    }
    //修改客户时,打开修改地址窗口
    function openUpdateAddress(index){
        $("#addressGrid").datagrid("selectRow", index);
        var selected = $("#addressGrid").datagrid("getSelected");
        if (null == selected) {
            $.messager.alert('警告','请选中一条数据');
            return;
        }
        $('#addressDlg').dialog('open');
        $('#addressDlg').dialog('center');
        $('#_addressForm').form('clear');
        formFocus("_addressForm", "_address_name");
        var formData = $.extend({},selected);
        formData = addKeyStartWith(getOriginalData(formData),"_address_");
        formData["_address_city"] = selected["cityId"];
        $('#_addressForm').form('load', formData);
        var c_lng = selected["lng"];
        var c_lat = selected["lat"];
        map.clearOverlays();    //清除地图上所有覆盖物
        //如果坐标不为空，则，根据坐标进行定位
        if (null != c_lng && ''!=c_lng && null != c_lat && ''!=c_lat){
            window.setTimeout(function(){
                var p =  new BMap.Point(c_lng, c_lat);
                map.panTo(p);
                var marker = new BMap.Marker(p);  // 创建标注
                map.addOverlay(marker);               // 将标注添加到地图中
                marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
            }, 50);
        }else{
            var city = selected["cityId"];
            if (null != city){
                window.setTimeout(function(){
                    map.setCenter(city);
                }, 50);
            }else{
                window.setTimeout(function(){
                    map.panTo(point);
                }, 50);
            }
        }

    }
    <%if(has(action) && action=="edit"){%>
        //选择地址之前切换反相色的图标
        function onBeforeSelectAddress(index, row) {
            //获取当前选中的行索引
            var rowIndex = $("#addressGrid").datagrid("getRowIndex", $("#addressGrid").datagrid("getSelected"));
            $("#addressUpdate"+rowIndex).attr("src", "${contextPath}/resources/easyui/css/themes/icons/pencil.png");
            $("#addressRemove"+rowIndex).attr("src", "${contextPath}/resources/easyui/css/themes/icons/edit_remove.png");
            //index为即将选中的行索引
            $("#addressUpdate"+index).attr("src", "${contextPath}/resources/easyui/css/themes/icons/pencil-on.png");
            $("#addressRemove"+index).attr("src", "${contextPath}/resources/easyui/css/themes/icons/edit_remove-on.png");
        }

        function addressOptFmt(value,row,index) {
            return "<img src='${contextPath}/resources/easyui/css/themes/icons/pencil.png' style='cursor: pointer' id='addressUpdate"+index+"' onclick='openUpdateAddress("+index+")'/>&nbsp;&nbsp;&nbsp;"
                + "    <img src='${contextPath}/resources/easyui/css/themes/icons/edit_remove.png' style='cursor: pointer' id='addressRemove"+index+"' onclick='delAddress(\""+row["id"]+"\")'/>";
        }
        //新增客户时,打开插入地址框
        function openInsertAddress() {
            $('#addressDlg').dialog('open');
            $('#addressDlg').dialog('center');
            $('#_addressForm').form('clear');
            formFocus("_addressForm", "_address_name");
            //默认为 非默认地址
            $('#_address_isDefault').combobox("setValue",0);
            //重新定位到初始地址
            window.setTimeout(function(){
                // map.panTo(point);
                map.clearOverlays();
                map.centerAndZoom(point,12);
            }, 50);

        }

        //保存地址
        function saveOrUpdateAddress() {
            <%if (has(customer)){%>
            if(!$('#_addressForm').form("validate")){
                return;
            }
            // if(manualInput){
            //     $.messager.alert('错误',"手动输入不合法，请选择城市");
            //     return;
            // }
            var city  = $('#_address_city').textbox("getValue");
            if (null == city || ''==city){
                $.messager.alert('错误',"输入不合法，请先在地图上选择城市");
                return;
            }
            var _formData = removeKeyStartWith($("#_addressForm").serializeObject(),"_address_");
            var _url = null;
            //没有id就新增
            if(_formData.id == null || _formData.id==""){
                _url = "${contextPath}/address/insert";
            }else{//有id就修改
                _url = "${contextPath}/address/update";
            }
            //设置当前选择的客户id
            var selected = ${customer};
            _formData["customerId"] = selected["id"];
            $('#addressDlg').dialog('close');
            $.ajax({
                type: "POST",
                url: _url,
                data: _formData,
                processData:true,
                dataType: "json",
                async : true,
                success: function (data) {
                    if(data.code=="200"){
                        $("#addressGrid").datagrid("reload");
                    }else{
                        $.messager.alert('错误',data.result);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown){
                    $.messager.alert('错误','远程访问失败');
                }
            });
            <%}%>
        }

        //根据主键删除地址
        function delAddress(id) {
            var selectedId;
            if(id){
                selectedId = id;
            }else{
                var selected = $("#addressGrid").datagrid("getSelected");
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
                        url: "${contextPath}/address/delete",
                        data: {id:selectedId},
                        processData:true,
                        dataType: "json",
                        async : true,
                        success: function (data) {
                            if(data.code=="200"){
                                $("#addressGrid").datagrid("reload");
                                $('#addressDlg').dialog('close');
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
    // ============================   地址相关js end  =============================
</script>