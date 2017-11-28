<div id="addressDlg" class="easyui-dialog" resizable="false" constrain="true" shadow="true" draggable="false" title="地址信息" style="padding:20px" modal="true" border="thin" closed="true"
     data-options="
				iconCls: 'icon-save',
				height: 230,
				buttons: [{
					text:'确定',
					iconCls:'icon-ok',
					handler:saveOrUpdateAddress
				},{
					text:'取消',
					handler:function(){
						$('#addressDlg').dialog('close');
					}
				}]
			">
    <form id="_addressForm" class="easyui-form" method="post" fit="true">
        <input name="_address_id" id="_address_id" type="hidden">
        <table width="360px">
            <tr>
                <td style="padding-top:10px;padding-left:15px;">
                    <input class="easyui-textbox" name="_address_name" id="_address_name" required="true" style="width:100%" data-options="label:'名称:', validType:'length[1,40]'" />
                </td>
            </tr>
            <tr>
                <td style="padding-top:10px;padding-left:15px;">
                    <input class="easyui-textbox" name="_address_address" id="_address_address" required="true" style="width:100%" data-options="label:'地址:', validType:'length[1,250]'" />
                </td>
            </tr>
            <tr>
                <td style="padding-top:10px;padding-left:15px;">
                    <input name="_address_cityId" id="_address_cityId" style="width:100%;" panelWidth="auto" required="true" panelHeight="auto" label="所在城市:"  data-options="validType:'length[1,20]', editable:true, hasDownArrow:false, onChange:changeName, onSelect:selectName, onLoadSuccess:onCityComboLoadSuccess"/>
                    <#comboProvider _id="_address_cityId" _provider="cityProvider" _valueField="value" _textField="label" />
                </td>
            </tr>
        </table>
    </form>

</div>