<div id="addressDiv" class="easyui-panel" style="width: 100%;" header="#addressHeader">
    <#controls_panelHeader panelId="addressDiv" headerId="addressHeader" title="地址" insertFun="openInsertAddress"></#controls_panelHeader>

    <!-- =========================================================地址表格========================================================= -->
    <table class="easyui-datagrid" id="addressGrid" style="height:150px;padding:0px;width:100%;" title="地址" noheader="true"
           pagination="false" rownumbers="true" remoteSort="false" data-options="loadMsg:0
          <%if(has(action) && action=="edit"){%>, onDblClickRow:openUpdateAddress, onBeforeSelect:onBeforeSelectAddress,tools:[{iconCls:'icon-add',  handler:openInsertAddress}]<%}%>"
           loadMsg="数据加载中..." singleSelect="true" method="post" multiSort="false"
           align="center"  striped="false" idField="id" >
        <thead>
        <tr>
            <th width="20%" data-options="field:'name',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                名称
            </th>
            <th width="<%if(has(action) && action=="edit"){%>31%<%}else{%>52%<%}%>" data-options="field:'address',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                地址
            </th>
            <th width="30%" data-options="field:'cityId', _provider:'cityProvider', sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                城市
            </th>
            <%if(has(action) && action=="edit"){%>
            <th width="21%" data-options="field:'addressOpt', formatter:addressOptFmt, sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                操作
            </th>
            <%}%>
        </tr>
        </thead>
    </table>
</div> <!-- end of addressDiv -->