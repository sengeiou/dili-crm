<!-- 隐藏编辑框 -->
<div id="contactsDlg" class="easyui-dialog" resizable="false" constrain="true" shadow="true" draggable="false" title="联系人信息" style="padding:20px" modal="true" border="thin" closed="true"
     data-options="
				height: 420,
				buttons: [{
					text:'确认',
					iconCls:'icon-ok',
					handler:saveOrUpdateContacts
				}]
			">
    <form id="_contactsForm" class="easyui-form" method="post" fit="true">
        <input name="_contacts_id" id="_contacts_id" type="hidden">
        <table width="360px">
            <tr>
                <td style="padding:5px;">
                    <input class="easyui-textbox" name="_contacts_name" id="_contacts_name" style="width:100%" required="true" data-options="labelAlign:'right',label:'&lowast;姓名:', validType:'length[1,20]'" />
                </td>
            </tr>
            <tr>
                <td style="padding:5px;">
                    <input name="_contacts_sex" id="_contacts_sex" style="width:100%;" panelWidth="auto" data-options="labelAlign:'right',editable:false" required="true" panelHeight="auto" label="性别:" />
                    <#comboProvider _id="_contacts_sex" _provider='sexProvider'/>
                </td>
            </tr>
            <tr>
                <td style="padding:5px;">
                    <input class="easyui-textbox" name="_contacts_phone" id="_contacts_phone" style="width:100%" required="true" data-options="labelAlign:'right',label:'&lowast;电话:', validType:'length[0,20]'" />
                </td>
            </tr>
            <tr>
                <td style="padding:5px;">
                    <input class="easyui-textbox" name="_contacts_address" id="_contacts_address" style="width:100%" data-options="labelAlign:'right',label:'地址:', validType:'length[0,250]'" />
                </td>
            </tr>
            <tr>
                <td style="padding:5px;">
                    <input class="easyui-textbox" name="_contacts_position" id="_contacts_position" style="width:100%" data-options="labelAlign:'right',label:'职务/关系:', validType:'length[0,100]'" />
                </td>
            </tr>
            <tr>
                <td style="padding:5px;">
                    <input class="easyui-datebox" name="_contacts_birthday" id="_contacts_birthday" style="width:100%" data-options="labelAlign:'right',label:'出生日期:',editable:false" />
                </td>
            </tr>
            <tr>
                <td style="padding:5px;">
                    <input class="easyui-textbox" name="_contacts_nation" id="_contacts_nation" style="width:100%" data-options="labelAlign:'right',label:'民族:', validType:'length[0,20]'" />
                </td>
            </tr>
            <tr>
                <td style="padding:5px;">
                    <input class="easyui-textbox" name="_contacts_notes" id="_contacts_notes" style="width:100%" data-options="labelAlign:'right',label:'备注:', validType:'length[0,250]'" />
                </td>
            </tr>
        </table>
    </form>

</div>