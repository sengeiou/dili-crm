<p />
<div id="pointsDiv" class="easyui-panel" style="width: 100%;" border="false" header="#pointsHeader">

    <#controls_panelHeader panelId="pointsDiv" headerId="pointsHeader" title="客户积分"></#controls_panelHeader>
    <!-- =========================================================成员客户表格========================================================= -->
    <table class="easyui-datagrid" id="pointsGrid" style="height:150px;padding:0px;width:100%;" title="客户积分" noheader="true"
           pagination="false" rownumbers="false" remoteSort="false" data-options="loadMsg:0"
           singleSelect="true" method="post" multiSort="false"
           align="center"  striped="false" idField="id" >
        <thead>
        <tr>
            <th data-options="field:'id',hidden:true"></th>
            <th width="50%" data-options="field:'tradingFirmCode', sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                所属市场
            </th>
            <th width="51%" data-options="field:'available',  sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                可用积分
            </th>
        </tr>
        </thead>
    </table>
</div> <!-- end of pointsDiv -->
