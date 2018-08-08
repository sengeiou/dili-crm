<p />
<div id="vehicleDiv" class="easyui-panel" style="width: 100%;" border="false" header="#vehicleHeader">
    <% var insertFunction = "";
    if(has(action) && (action=="edit" || action=="add")){
        insertFunction = "openInsertVehicle";
    }%>
    <#controls_panelHeader panelId="vehicleDiv" headerId="vehicleHeader" title="车辆" insertFun="${insertFunction}"></#controls_panelHeader>

    <!-- =========================================================车辆表格========================================================= -->
    <table class="easyui-datagrid" id="vehicleGrid" style="height:150px;padding:0px;width:100%;" title="车辆" noheader="true"
           pageNumber="1" rownumbers="false" remoteSort="false" 
           singleSelect="true" method="post" multiSort="false" sortName="name"
           align="center" striped="false" idField="id" data-options="loadMsg:0
          <%if(has(action) && (action=="edit" || action=="add")){%>,onDblClickRow:openUpdateVehicle, onBeforeSelect:onBeforeSelectVehicle,tools:[{iconCls:'icon-add',handler:openInsertVehicle}]<%}%>">
        <thead>
        <tr>
            <th width="<%if(has(action) && (action=="edit" || action=="add")){%>30%<%}else{%>40%<%}%>" data-options="field:'name',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                车辆名称
            </th>
            <th width="25%" data-options="field:'registrationNumber',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                车牌号
            </th>
            <th width="<%if(has(action) && (action=="edit" || action=="add")){%>25%<%}else{%>37%<%}%>" data-options="field:'type',   _provider:'dataDictionaryValueProvider', _queryParams:{'dd_code':'vehicle_type'}, sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                车型
            </th>
            <%if(has(action) && (action=="edit" || action=="add")){%>
            <th width="22%" data-options="field:'vehicleOpt', formatter:vehicleOptFmt, sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                操作
            </th>
            <%}%>
        </tr>
        </thead>
    </table>
</div> <!-- end of vehicleDiv -->