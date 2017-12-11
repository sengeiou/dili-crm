<div id="membersDiv" class="easyui-panel" style="width: 96%;"  header="#membersHeader">
    <#controls_panelHeader panelId="membersDiv" headerId="membersHeader" title="成员客户" insertFun="_selectMembers"></#controls_panelHeader>
    <!-- =========================================================成员客户表格========================================================= -->
    <table class="easyui-datagrid" id="membersGrid" style="height:150px;padding:0px;width:100%;" title="成员客户" noheader="true"
           pagination="false" rownumbers="true" remoteSort="false" data-options="loadMsg:0
           <%if(has(action) && action=="edit"){%>, onBeforeSelect:onBeforeSelectMembers,tools:[{iconCls:'icon-add',handler:_selectMembers}]<%}%>"
           singleSelect="true" method="post" multiSort="false"
           align="center"  striped="false" idField="id" >
        <thead>
        <tr>
            <th width="<%if(has(action) && action=="edit"){%>25%<%}else{%>35%<%}%>" data-options="field:'name',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                姓名
            </th>
            <th width="<%if(has(action) && action=="edit"){%>25%<%}else{%>30%<%}%>" data-options="field:'code',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                客户编码
            </th>
            <th width="<%if(has(action) && action=="edit"){%>30%<%}else{%>36%<%}%>" data-options="field:'phone',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                电话
            </th>
            <%if(has(action) && action=="edit"){%>
            <th width="21%" data-options="field:'membersOpt', formatter:membersOptFmt, sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                操作
            </th>
            <%}%>
        </tr>
        </thead>
    </table>
</div> <!-- end of membersDiv -->
<div id="membersSelectDialog" style="display: none;"></div>