<div  class="easyui-panel" style="width: 96%;" title="回访信息" border="false" >
    <form id="_form" class="easyui-form" method="post" fit="true">
        <input name="_id" id="_id" type="hidden">
        <table align="center" width="94%">
            <tr>
                <td style="padding-top:10px;padding-left:10px;" width="33%">
                    <input class="easyui-textbox inputWidth" name="_subject" id="_subject"  data-options="labelAlign:'right',label:'&lowast;主题:', validType:'length[0,40]'" required="true" />
                </td>
                <td style="padding-top:10px;" width="33%">
                    <input class="easyui-combobox inputWidth" name="_firmCode" id="_firmCode" data-options="labelAlign:'right',required:true, editable:false,onLoadSuccess:onComboLoadSuccessSelectOne" panelWidth="auto" panelHeight="auto" label="*所属市场:" />
                    <#comboProvider _id="_firmCode" _provider='firmProvider' />
                </td>
                <td style="padding-top:10px;padding-left:10px;" width="33%">
                    <input class="easyui-textbox inputWidth" name="userId" id="userId" panelWidth="auto" panelHeight="auto" label="&lowast;回访人:"  required="true"
                           data-options="labelAlign:'right',editable:false,buttonText:'选择',onClickButton:_selectUserByFirmCode,onChange:_changeTextboxShowClear" />
                </td>
            </tr>
            <tr>
                <td style="padding-top:10px;padding-left:10px;" width="33%">
                    <input class="easyui-textbox inputWidth" name="customerId" id="customerId" panelWidth="auto" panelHeight="auto" label="&lowast;回访客户:"  required="true"
                           data-options="labelAlign:'right',editable:false,
                                        buttonText:'选择',
                                        onClickButton:_selectCustomer,onChange:_changeTextboxShowClear" />
                </td>
                <td style="padding-top:10px;">
                    <input class="easyui-textbox inputWidth" name="_createdName" id="_createdName" data-options="labelAlign:'right',label:'回访创建者:'"  editable="true"/>
                </td>
                <td style="padding-top:10px;padding-left:10px;">
                    <input name="_mode" class="easyui-combobox inputWidth" id="_mode"  panelWidth="auto" panelHeight="auto" data-options="labelAlign:'right'" label="&lowast;回访方式:" required="true" editable="false"/>
                    <#comboProvider _id="_mode" _provider='dataDictionaryValueProvider' _queryParams='{dd_code:"visit_mode"}'/>
                </td>
            </tr>
            <tr>
                <td style="padding-top:10px;padding-left:10px;">
                    <input class="easyui-combobox inputWidth" name="_priority" id="_priority"  data-options="labelAlign:'right',editable:false" panelWidth="auto" panelHeight="auto" label="优先级:" />
                    <#comboProvider _id="_priority" _provider='dataDictionaryValueProvider' _queryParams='{dd_code:"priority"}'/>
                </td>
                <td style="padding-top:10px;">
                    <input class="easyui-datetimebox inputWidth" name="_finishTime" id="_finishTime" data-options="labelAlign:'right',label:'完成时间:',editable:false"/>
                </td>
                <td></td>
            </tr>
            <tr>
                <td style="padding-top:10px;padding-left:10px;" colspan="3" width="99%">
                    <input class="easyui-textbox" name="_notes" id="_notes" style="width:90%" multiline="true" data-options="labelAlign:'right',label:'描述:', validType:'length[0,200]'" />
                </td>
            </tr>

        </table>
    </form>
</div>