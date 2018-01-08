
<div id="addressDlg" class="easyui-dialog" resizable="false" constrain="true" shadow="true" draggable="false" title="地址信息" style="padding:20px;width: 80%;height: 100%;" modal="true" border="thin" closed="true"
     data-options="
				buttons: [{
					text:'确定',
					iconCls:'icon-ok',
					handler:saveOrUpdateAddress
				}]
			">
    <form id="_addressForm" class="easyui-form" method="post" fit="true">
        <input name="_address_id" id="_address_id" type="hidden">
        <input name="_address_lng" id="_address_lng" type="hidden">
        <input name="_address_lat" id="_address_lat" type="hidden">
        <table width="99%">
            <tr>
                <td style="padding-top:5px;" width="28%" height="20%">
                    <input class="easyui-textbox" name="_address_name" id="_address_name" required="true" style="width: 100%;" data-options="labelAlign:'right',label:'&lowast;名称:', validType:'length[1,40]'" />
                </td>
                <td style="padding-top:5px;padding-left:10px;" width="15%">
                    <input class="easyui-textbox" name="_address_isDefault" id="_address_isDefault" panelWidth="auto" style="width: 100%;" required="true" panelHeight="auto" label="&lowast;是否默认:"  data-options="labelAlign:'right',validType:'length[1,20]', editable:false"/>
                    <#comboProvider _id="_address_isDefault" _provider='isDefaultProvider' />
                </td>
                <td style="padding-top:5px;padding-left:10px;" width="23%">
                    <input class="easyui-textbox" name="_address_city" id="_address_city" panelWidth="auto" style="width: 100%;" required="true" panelHeight="auto" label="&lowast;所在城市:"  data-options="labelAlign:'right',validType:'length[1,20]', editable:false"/>
                </td>
                <td style="padding-top:5px;padding-left:10px;" width="33%">
                    <input class="easyui-textbox" name="_address_address" id="_address_address" style="width: 100%;" required="true"  data-options="labelAlign:'right',label:'&lowast;地址:', validType:'length[1,250]'" />
                </td>
            </tr>
            <tr>
                <td style="padding-top:5px;" colspan="4" width="99%" height="75%">
                    <div id="mapContainer" style="width: 95%;height: 75%; position: absolute;"></div>
                    <div style="padding-top:5px;position: fixed;width: 30%;height: 20px;">
                        <div id="r-result">&nbsp;&nbsp;&nbsp;<input type="text" id="suggestId" placeholder="地址搜索" value="" style="width:50%; height: 20px; line-height: 20px;border: 1px solid #d4d4d4; "/></div>
                        <div id="searchResultPanel" style="border:1px solid #C0C0C0;width:150px;height:auto; display:none;"></div>
                    </div>
                </td>
            </tr>
        </table>
    </form>
</div>
<script>
    var lng = 104.06792346;
    var lat = 30.67994285;
    // 百度地图API功能
    var map = new BMap.Map("mapContainer");
    var point = new BMap.Point(lng,lat);
    map.centerAndZoom(point,15);
    map.enableScrollWheelZoom();     //开启鼠标滚轮缩放
    // 百度地图API功能
    function G(id) {
        return $('#'+id);
    }
    var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
        {"input" : "suggestId"
            ,"location" : map
        });
    ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
        var str = "";
        var _value = e.fromitem.value;
        var value = "";
        if (e.fromitem.index > -1) {
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;
        value = "";
        if (e.toitem.index > -1) {
            _value = e.toitem.value;
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
        G("searchResultPanel").innerHTML = str;
    });

    var myValue;
    ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
        var _value = e.item.value;
        myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;
        setPlace();
    });

    function setPlace(){
        map.clearOverlays();    //清除地图上所有覆盖物
        function addressSearch(){
            var poi = local.getResults().getPoi(0);
            $('#_address_city').textbox("setValue", poi.province +','+  poi.city);
            $('#_address_address').textbox("setValue", poi.address);
            var pp = poi.point;    //获取第一个智能搜索的结果
            $('#_address_lng').val(pp.lng);
            $('#_address_lat').val(pp.lat);
            map.centerAndZoom(pp, 18);
            map.addOverlay(new BMap.Marker(pp));    //添加标注
        }
        var local = new BMap.LocalSearch(map, { //智能搜索
            onSearchComplete: addressSearch
        });
        local.search(myValue);
    }

    var geoc = new BMap.Geocoder();
    map.addEventListener("click", function(e){
        var pt = e.point;
        $('#_address_lng').val(pt.lng);
        $('#_address_lat').val(pt.lat);
        geoc.getLocation(pt, function(rs){
            var addComp = rs.addressComponents;
            $('#_address_city').textbox("setValue",addComp.province+','+addComp.city);
            $('#_address_address').textbox("setValue", addComp.district  + addComp.street +  addComp.streetNumber);
        });
    });
</script>
<style>
    .tangram-suggestion-main {z-index: 9999}
    .BMap_pop,.BMap_shadow{
        display:none;
    }
    /*. */
        /*display: none;*/
    /*}*/
</style>
