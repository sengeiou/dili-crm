<div id="vehicleDiv" style="display: inline;float: left;padding-left: 0px;width: 46%">

    <!-- =========================================================车辆表格========================================================= -->
    <table class="easyui-datagrid" id="vehicleGrid" style="height:185px;width:100%"
           pageNumber="1" rownumbers="true" remoteSort="false" title="车辆信息"
           singleSelect="true" method="post" multiSort="false" sortName="name"
           align="center" striped="false" idField="id" data-options="onDblClickRow:openUpdateVehicle,
           loadMsg:0, onBeforeSelect:onBeforeSelectVehicle,tools:[{iconCls:'icon-add',handler:openInsertVehicle}]">
        <thead>
        <tr>
            <th width="30%" data-options="field:'name',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                车辆名称
            </th>
            <th width="25%" data-options="field:'registrationNumber',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                车牌号
            </th>
            <th width="25%" data-options="field:'type',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                车型
            </th>
            <th width="20%" data-options="field:'vehicleOpt', formatter:vehicleOptFmt, sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                操作
            </th>
        </tr>
        </thead>
    </table>
</div> <!-- end of vehicleDiv -->