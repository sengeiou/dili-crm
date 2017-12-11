<div id="contactsDiv" class="easyui-panel" style="width: 96%;"  header="#contactsHeader">
    <#controls_panelHeader panelId="contactsDiv" headerId="contactsHeader" title="联系人" insertFun="openInsertContacts"></#controls_panelHeader>
    <!-- =========================================================联系人表格========================================================= -->
    <table class="easyui-datagrid" id="contactsGrid" style="height:150px;padding:0px;width:100%;" noheader="true" title="联系人"
           pagination="false" rownumbers="true" remoteSort="false" data-options="loadMsg:0
          <%if(has(action) && action=="edit"){%>,onDblClickRow:openUpdateContacts, onBeforeSelect:onBeforeSelectContacts,tools:[{iconCls:'icon-add',handler:openInsertContacts}]<%}%>"
           loadMsg="数据加载中..." singleSelect="true" method="post" multiSort="false"
           align="center"  striped="false" idField="id" >
        <thead>
        <tr>
            <th width="12%" data-options="field:'name',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                姓名
            </th>
            <th width="12%" data-options="field:'position',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                职务/关系
            </th>
            <th width="8%" data-options="field:'sex',  _provider:'sexProvider', sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                性别
            </th>
            <th width="10%" data-options="field:'phone',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                电话
            </th>
            <th width="10%" data-options="field:'birthday',  _provider:'dateProvider', sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                出生日期
            </th>
            <th width="8%" data-options="field:'nation',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                民族
            </th>
            <th width="12%" data-options="field:'address',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                地址
            </th>
            <th width="<%if(has(action) && action=="edit"){%>21%<%}else{%>29%<%}%>" data-options="field:'notes',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                备注
            </th>
            <%if(has(action) && action=="edit"){%>
            <th width="8%" data-options="field:'contactsOpt', formatter:contactsOptFmt, sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                操作
            </th>
            <%}%>
        </tr>
        </thead>
    </table>
</div> <!-- end of contactsDiv -->