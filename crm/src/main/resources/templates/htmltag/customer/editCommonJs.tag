<script type="text/javascript">
    //保存客户
    function saveOrUpdate(){
        saveAndExit("${contextPath}/customer/detail.html?id=");
    }

    //保存客户
    function saveAndExit(url){
        if(!$('#_form').form("validate")){
            return;
        }
        // 修改操作，则不验证证件信息
        <%if(has(action) && action != "edit") {%>
        if(!validateCertificateNumber($("#_certificateNumber").textbox("getValue"))){
            return;
        }
        <%}%>
        if ($("#_operatingArea").combobox("getValue")){
            if(manualInput){
                $.messager.alert('错误',"手动输入不合法，请选择经营区域");
                return;
            }
        }

        var msg = (!url || url == null || url === "") ? "您确认要保存并退出吗？" : "您确认想要保存吗？";
        $.messager.confirm('确认',msg,function(r) {
            if (r) {
                //禁用保存
                $("#saveBtn").linkbutton("disable");
                $("#saveAndExitBtn").linkbutton("disable");
                var selectOperatingArea = $('#_operatingArea').textbox("getValue");
                if(selectOperatingArea == ''){
                    $("#selectedAreaLat").val('');
                    $("#selectedAreaLng").val('');
                }
                var _formData = removeKeyStartWith($("#_form").serializeObject(true),"_");
                var _url = null;
                var add = true;
                <%if(has(action) && action == "add") {%>
                _url = "${contextPath}/customer/insert.action";
                <%}else{%>
                add = false;
                _url = "${contextPath}/customer/update.action";
                <%}%>
                if (add){
                    _formData.members = _members;
                }
                $.ajax({
                    type: "POST",
                    url: _url,
                    data: _formData,
                    processData:true,
                    dataType: "json",
                    traditional: true,
                    async : true,
                    success: function (data) {
                        if(data.code=="200"){
                            if(url){
                                url += data.data.id;
                            }else{
                                url = "${contextPath}/customer/index.html";
                            }
                            $.messager.alert({
                                title: '提示',
                                msg: data.result,
                                fn: function(){
                                    window.location.href = url;
                                }
                            });
                        }else{
                            $("#saveBtn").linkbutton("enable");
                            $("#saveAndExitBtn").linkbutton("enable");
                            $.messager.alert('错误',data.result);
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown){
                        $("#saveBtn").linkbutton("enable");
                        $("#saveAndExitBtn").linkbutton("enable");
                        $.messager.alert('错误','远程访问失败');
                    }
                });
            }
        });
    }

    //清空客户表单
    function clearForm() {
        $('#form').form('clear');
    }

    //表格表头右键菜单
    function headerContextMenu(e, field){
        e.preventDefault();
        if (!cmenu){
            createColumnMenu("grid");
        }
        cmenu.menu('show', {
            left:e.pageX,
            top:e.pageY
        });
    }

    //根据组织类型加载和显示相关字段(证件类型、性别、民族、证件日期控件、注册资金和企业员工数)
    function loadByOrganizationType(formData) {
        switchByOrganizationType(formData[orginal_key_prefix + "organizationType"]);
    }

    //根据组织类型切换控件
    function switchByOrganizationType(organizationType) {
        //加载时组织类型默认为个人
        if (null == organizationType) {
            organizationType = "individuals";
        }
        //根据组织类型加载证件类型下拉框
        loadCertificateTypeByOrganizationType(organizationType);
        if (organizationType == "individuals") {
            $(".individuals").show();
            $(".enterprise").hide();
            //组织类型为'个人'时，证件号必填
            $("#_certificateNumber").textbox("options").label = "&lowast;证件号:";
        } else if (organizationType == "enterprise") {
            $(".individuals").hide();
            $(".enterprise").show();
            //组织类型为'企业'时，证件号非必填
            $("#_certificateNumber").textbox("options").label = "证件号:";
        }
        $("#_certificateNumber").textbox();
    }

    //根据组织类型加载证件类型
    function loadCertificateTypeByOrganizationType(organizationType) {
        $.ajax({
            type: "POST",
            url: "${contextPath}/customer/getCertificateTypeComboboxData.action",
            data: {"organizationType":organizationType},
            processData:true,
            dataType: "json",
            async : false,
            success: function (result) {
                if(result.code=="200"){
                    //加载证件类型
                    $("#_certificateType").combobox({
                        data:eval("("+result.data+")")
                        ,valueField:"value"
                        ,textField:"text"
                    });
                }else{
                    $.messager.alert('错误',result.result);
                }
            },
            error: function(){
                $.messager.alert('错误','远程访问失败');
            }
        });

    }
    
     //修改组织类型
     function changeOrganizationType(newValue, oldValue) {
         switchByOrganizationType(newValue);
     }

    //验证证件号码
    function validateCertificateNumber(newValue, year, month, day){
        //初始化
        if(year == null || month == null || day == null){
            var birthStr = newValue.substr(6,8);
            month = birthStr.substr(4,2);
            day = birthStr.substr(6,2);
            year = birthStr.substr(0,4);
        }
        //获取组织类型
        var organizationType = $("#_organizationType").combobox("getValue");
        //组织类型不为企业时，证件号码为必填项
        if (organizationType != "enterprise"){
            if (!newValue){
                $.messager.alert('警告','组织类型不为企业时，证件号为必填项');
                return false;
            }
        }
        //组织类型为个人,并且证件类型为身份证时才校验
        if(organizationType != "individuals" || $("#_certificateType").combobox("getValue") != "id"){
            return true;
        }
        if(newValue.length < 18 || newValue.length > 19){
            $.messager.alert('警告','证件号不合法');
            return false;
        }

        if (isNaN(year) || isNaN(month) || isNaN(day)){
            $.messager.alert('警告','证件号不合法');
            return false;
        }
        if(parseInt(month)<1 || parseInt(month)>12){
            $.messager.alert('警告','证件号不合法');
            return false;
        }
        if(parseInt(day)<1 || parseInt(day)>31){
            $.messager.alert('警告','证件号不合法');
            return false;
        }
        if(parseInt(year)<1900 || parseInt(year)>2100){
            $.messager.alert('警告','证件号不合法');
            return false;
        }
        return true;
    }
    //修改证件号，如果证件类型为身份证，在输入18位-19位时，自动完成证件日期
    function changeCertificateNumber(newValue, oldValue) {

    }
    //经营区域地图对象
    var operatingAreaMap=null;
    //经营区域的地图搜索自动完成对象
    var areaAc =null;
    // var markes=[];
    function initOperatingAreaMap(city,point){
        var lat ="<#config name='map.lat' />";
        var lng ="<#config name='map.lng' />";
        if(operatingAreaMap==null){
            operatingAreaMap=new BMap.Map("operatingAreaMapDiv");
            operatingAreaMap.centerAndZoom(new BMap.Point(lng, lat), 11);
            operatingAreaMap.enableScrollWheelZoom();
            //单击获取点击的经纬度
            operatingAreaMap.addEventListener("click",mapClick);
        }
        if(null != city && point!=null) {
            // 将地址解析结果显示在地图上,并调整地图视野
            $('#selectOperatingArea').textbox("setValue",city);
            operatingAreaMap.centerAndZoom(point, 11);
            addOverlay(point);
        }
        return operatingAreaMap;
    }
    //点击地图时保存坐标信息到隐藏域
    function mapClick(e) {
        var pt = e.point;
        addOverlay(pt);
        geoco.getLocation(pt, function(rs){
            var addComp = rs.addressComponents;
            $('#selectOperatingArea').textbox("setValue",addComp.province+addComp.city+addComp.district);
            $('#selectedAreaLat').val(pt.lat);
            $('#selectedAreaLng').val(pt.lng);
        });
    }
    //添加图层
    function addOverlay(point) {
        operatingAreaMap.clearOverlays();    //清除地图上所有覆盖物
        var marker = new BMap.Marker(point);  // 创建标注
        operatingAreaMap.addOverlay(marker);               // 将标注添加到地图中
        marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
    }

    /**
     * 打开经营区域地图
     */
    function openMaps() {
        $('#operatingAreaDialog').dialog('open',true);
        //清空数据
        $('#selectOperatingArea').textbox("setValue","");
        if($('#_operatingArea').combobox('getText')==''){
            initOperatingAreaMap(null,null);
        }else{
            var point = new BMap.Point($('#selectedAreaLng').val(),$('#selectedAreaLat').val());
            var reg=/,/g;
            var city=$('#_operatingArea').combobox('getText').replace(reg,'');
            initOperatingAreaMap(city,point);
        };
        initAreaAc();
    }

    function initAreaAc() {
        $('#suggestArea').val('');
        if (areaAc == null){
            //建立一个自动完成的对象
            areaAc = new BMap.Autocomplete(
                {"input" : "suggestArea"
                    ,"location" : operatingAreaMap
                });

            /**
             * 点击确认下拉中的某项
             */
            areaAc.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
                var _value = e.item.value;
                var myArea = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
                // $("#areaSearchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myArea = " + myArea;
                setPlace(myArea);
            });
        }
    }
    /**
     * 地图上的点击事件
     */
    var geoco = new BMap.Geocoder();
    /**
     * 根据选择的数据进行位置搜索
     */
    function setPlace(city){
        function areaSearch(){
            var poi = local.getResults().getPoi(0);
            var pt = poi.point;    //获取第一个智能搜索的结果
            geoco.getLocation(pt, function(rs){
                var addComp = rs.addressComponents;
                $('#selectOperatingArea').textbox("setValue",addComp.province+addComp.city+addComp.district);
                $('#selectedAreaLat').val(pt.lat);
                $('#selectedAreaLng').val(pt.lng);
            });
            operatingAreaMap.centerAndZoom(pt, 11);
            addOverlay(pt);
        }
        var local = new BMap.LocalSearch(operatingAreaMap, { //智能搜索
            onSearchComplete: areaSearch
        });
        local.search(city);
    }

    /**
     * 查询图标的单击事件
     * 手动触发 Autocomplete 的search事假
     */
    function queryArea() {
        areaAc.search($('#suggestArea').val());
    }

    /**
     * 确认按钮的点击事件
     */
    function confirmSelectedArea(){
        disableEasyUiControl('confirmSelectedArea',true);
        var selectOperatingArea=$('#selectOperatingArea').textbox("getValue");
        var lat=$('#selectedAreaLat').val();
        var lng=$('#selectedAreaLng').val();
        if(selectOperatingArea==''){
            $.messager.alert('提示','请选择经营区域');
            disableEasyUiControl('confirmSelectedArea',false);
            return ;
        }

        //发送ajax请求查询adcode
        $.ajax({
            type: "POST",
            url:"${contextPath}/address/locationReverse.action",
            data: {"lat":lat,"lng":lng},
            processData:true,
            dataType: "json",
            async : true,
            success: function (data) {
                if(data.code=="200"){
                    //console.info(data.data)
                    var adcode=data.data.id;
                    $('#operatingAreaDialog').dialog('close',true);
                    $("#_operatingArea").combobox('initValue',adcode);
                    $("#_operatingArea").combobox('setText',selectOperatingArea);
                    //设置不是手工输入
                    selectName();
                    $('#selectOperatingArea').textbox("setValue","");
                }else{
                    $.messager.alert('错误',data.result);
                }
                disableEasyUiControl('confirmSelectedArea',false);
            },
            error: function(){
                $.messager.alert('错误','远程访问失败');
                disableEasyUiControl('confirmSelectedArea',false);
            }
        });
    }

    $.parser.onComplete = function(){
        $.messager.progress("close");
        $(".window-mask-opaque").toggleClass("window-mask");
    }

    /**
     * 绑定页面回车事件，以及初始化页面时的光标定位
     * @formId
     *          表单ID
     * @elementName
     *          光标定位在指点表单元素的name属性的值
     * @submitFun
     *          表单提交需执行的任务
     */
    $(function () {
        bindFormEvent("_form", "_name", saveOrUpdate);
        <%if(has(action) && action=="edit"){%>
        openUpdate();
        <%}else{%>
        openInsert();
        <%}%>
        listAddress();
        listVehicle();
        listContacts();
        listMembers();
    });
    // ============================   客户相关js st  ==============================
    //打开新增客户窗口
    function openInsert(){
//            $('#_form').form('clear');
        $("#_ownerName").textbox("setValue", "${user.realName}");
        var formData = {};
        //组织类型默认选中"个人"
        formData["organizationType"] = "individuals";
        formData["organizationType"] = "individuals";
        formData["sourceSystem"] = "CRM";
        <%if (has(customer)){%>
        var selected = ${customer};
        formData["id"] = selected.id;
        <%}%>
        //根据组织类型加载证件类型、性别、民族、证件日期控件、注册资金和企业员工数f
        switchByOrganizationType("individuals");
        formData = addKeyStartWith(formData,"_");
        $('#_form').form('load', formData);
        <%if(has(department) && department !=null){%>
        $("#departmentName").textbox("setText", "${department.name}");
        <%}%>
        formFocus("_form", "_name");
        $("#portraitDiv").hide();
    }

    //打开修改客户窗口
    function openUpdate(){
        <%if (has(customer)){%>
        var selected = ${customer};
        $('#_form').form('clear');
        var formData = $.extend({},selected);

        //设置客户所有者(这里表格中的提供者已经作了转义,只需要对应上字段即可)
        formData["ownerName"] = selected["ownerId"];
        //根据组织类型加载证件类型、性别、民族、证件日期控件、注册资金和企业员工数
        loadByOrganizationType(formData);
        formData = addKeyStartWith(getOriginalData(formData),"_");
        //设置经营地区的显示值
        formData["_operatingArea"] = selected["operatingArea"];
        $('#_form').form('load', formData);
        $("#oldName").val(formData["_name"]);
        $("#oldMarket").val(formData["_market"]);
        //设置父客户
        $("#_parentId").textbox("initValue", selected["$_parentId"]);
        $("#_parentId").textbox("setText", selected["parentId"]);
        $('#selectedAreaLat').val(selected.operatingLat);
        $('#selectedAreaLng').val(selected.operatingLng);
        $("#departmentName").textbox("setText", selected["department"]);
        //修改时，如果证件号，为空，则表示可以修改，否则，不能修改证件号
        if (selected.certificateNumber){
            $("#_certificateNumber").textbox("disable");
            $("#_certificateNumber").textbox("readonly");
        }
        formFocus("_form", "_name");
        listCustomerExtensions();
        <%}%>
    }
    
</script>