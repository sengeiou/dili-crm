<div id="contactsDiv" style="display: inline;float: left;padding-left: 15px;width: 92%">
    <!-- =========================================================联系人表格========================================================= -->
    <table class="easyui-datagrid" id="contactsGrid" style="height:185px;padding:0px;width:100%;" title="联系人" collapsible="true"
           pagination="false" rownumbers="true" remoteSort="false" data-options="onDblClickRow:openUpdateContacts,
           loadMsg:0, onBeforeSelect:onBeforeSelectContacts,tools:[{iconCls:'icon-add',handler:openInsertContacts}]"
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
            <th width="20%" data-options="field:'notes',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                备注
            </th>
            <th width="9%" data-options="field:'contactsOpt', formatter:contactsOptFmt, sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                操作
            </th>
        </tr>
        </thead>
    </table>
</div> <!-- end of contactsDiv -->