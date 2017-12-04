<div id="vehicleDiv" style="width: 100%">

    <!-- =========================================================车辆表格========================================================= -->
    <table class="easyui-datagrid" id="vehicleGrid" style="height:185px;padding:0px;width:100%;"
           pageNumber="1" rownumbers="true" remoteSort="false" title="车辆" collapsible="true"
           singleSelect="true" method="post" multiSort="false" sortName="name"
           align="center" striped="false" idField="id" data-options="loadMsg:0
          <%if(has(action) && action=="edit"){%>,onDblClickRow:openUpdateVehicle, onBeforeSelect:onBeforeSelectVehicle,tools:[{iconCls:'icon-add',handler:openInsertVehicle}]<%}%>">
        <thead>
        <tr>
            <th width="<%if(has(action) && action=="edit"){%>30%<%}else{%>40%<%}%>" data-options="field:'name',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                车辆名称
            </th>
            <th width="25%" data-options="field:'registrationNumber',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                车牌号
            </th>
            <th width="<%if(has(action) && action=="edit"){%>25%<%}else{%>37%<%}%>" data-options="field:'type',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                车型
            </th>
            <%if(has(action) && action=="edit"){%>
            <th width="22%" data-options="field:'vehicleOpt', formatter:vehicleOptFmt, sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                操作
            </th>
            <%}%>
        </tr>
        </thead>
    </table>
</div> <!-- end of vehicleDiv -->