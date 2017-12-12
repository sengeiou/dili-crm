  <p />
    <div id="customerExtensionsDiv" class="easyui-panel" style="width: 96%;" border="false" header="#customerExtensionsHeader">
        <#controls_panelHeader panelId="customerExtensionsDiv" headerId="customerExtensionsHeader" title="业务系统账号信息" ></#controls_panelHeader>
    <!-- =========================================================业务系统账号信息表格========================================================= -->
    <table class="easyui-datagrid" id="customerExtensionsGrid" style="height:150px;padding:0px;width:100%;" title="业务系统账号信息" noheader="true"
           pagination="false" rownumbers="false" remoteSort="false" data-options="loadMsg:0"
           singleSelect="true" method="post" multiSort="false" 
           align="center"  striped="false" idField="id" >
        <thead>
        <tr>
            <th width="25%" data-options="field:'acctId',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                账户id
            </th>
            <th width="20%" data-options="field:'acctType', _provider:'dataDictionaryValueProvider', _queryParams:{'dd_id':'7'}, sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                类型
            </th>
            <th width="20%" data-options="field:'system', _provider:'dataDictionaryValueProvider', _queryParams:{'dd_id':'8'},  sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                所属系统
            </th>
            <th width="36%" data-options="field:'notes',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                备注
            </th>
        </tr>
        </thead>
    </table>
</div> <!-- end of customerExtensionsDiv -->
<div id="customerExtensionsSelectDialog" style="display: none;"></div>