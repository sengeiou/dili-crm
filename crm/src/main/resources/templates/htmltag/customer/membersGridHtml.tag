<div id="membersDiv" style="display: inline;float: left;padding-left: 15px;width: 92%">
    <!-- =========================================================成员客户表格========================================================= -->
    <table class="easyui-datagrid" id="membersGrid" style="height:185px;padding:0px;width:100%;" title="成员客户" collapsible="true"
           pagination="false" rownumbers="true" remoteSort="false" data-options="
           loadMsg:0, onBeforeSelect:onBeforeSelectMembers,tools:[{iconCls:'icon-add',handler:_selectMembers}]"
           singleSelect="true" method="post" multiSort="false"
           align="center"  striped="false" idField="id" >
        <thead>
        <tr>
            <th width="25%" data-options="field:'name',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                姓名
            </th>
            <th width="25%" data-options="field:'code',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                客户编码
            </th>
            <th width="30%" data-options="field:'phone',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                电话
            </th>
            <th width="20%" data-options="field:'membersOpt', formatter:membersOptFmt, sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                操作
            </th>
        </tr>
        </thead>
    </table>
</div> <!-- end of membersDiv -->
<div id="membersSelectDialog" style="display: none;"></div>