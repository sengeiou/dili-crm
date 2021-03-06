<!-- 隐藏编辑框 -->
<div id="vehicleDlg" class="easyui-dialog" resizable="false" constrain="true" shadow="true" draggable="false" title="车辆信息" style="padding:20px" modal="true" border="thin" closed="true"
     data-options="
				height: 230,
				buttons: [{
					text:'确认',
					iconCls:'icon-ok',
					handler:saveOrUpdateVehicle
				}]
			">
    <form id="_vehicleForm" class="easyui-form" method="post" fit="true">
        <input name="_vehicle_id" id="_vehicle_id" type="hidden">
        <table width="360px">
            <tr>
                <td style="padding:5px;">
                    <input class="easyui-textbox" name="_vehicle_name" id="_vehicle_name" style="width:100%" required="true" data-options="labelAlign:'right',label:'&lowast;名称:', validType:'length[1,40]'" />
                </td>
            </tr>
            <tr>
                <td style="padding:5px;">
                    <input class="easyui-textbox" name="_vehicle_registrationNumber" id="_vehicle_registrationNumber" style="width:100%" data-options="labelAlign:'right',label:'车牌号:', validType:'length[5,20]'" />
                </td>
            </tr>
            <tr>
                <td style="padding:5px;">
                    <input  name="_vehicle_type" id="_vehicle_type" style="width:100%" data-options="labelAlign:'right',label:'车型:', editable:false, validType:'length[0,20]'" />
                    <#comboProvider _id="_vehicle_type" _provider='dataDictionaryValueProvider' _queryParams='{dd_code:"vehicle_type"}'/>
                </td>
            </tr>
        </table>
    </form>

</div>