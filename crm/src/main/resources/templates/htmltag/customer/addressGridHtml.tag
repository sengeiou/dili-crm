<div id="addressDiv" style="display: inline;float: left;padding-left: 15px;width: 46%">
    <B>地址信息</B><a class="easyui-linkbutton" iconCls="icon-add" onclick="openInsertAddress()"></a>

    <!-- =========================================================地址表格========================================================= -->
    <table class="easyui-datagrid" id="addressGrid" style="height:150px;padding:5px;"
           pagination="false" rownumbers="true" remoteSort="false" data-options="onDblClickRow:openUpdateAddress,loadMsg:0, onBeforeSelect:onBeforeSelectAddress"
           loadMsg="数据加载中..." singleSelect="true" method="post" multiSort="false"
           align="center"  striped="false" idField="id" >
        <thead>
        <tr>
            <th width="30%" data-options="field:'name',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                名称
            </th>
            <th width="50%" data-options="field:'address',   sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                地址
            </th>
            <th width="50%" data-options="field:'cityId', hidden:true, _provider:'cityProvider', sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                城市
            </th>
            <th width="20%" data-options="field:'addressOpt', formatter:addressOptFmt, sortable:'true', order:'asc', align:'center', resizable:'true', fixed:'false'">
                操作
            </th>
        </tr>
        </thead>
    </table>
</div> <!-- end of addressDiv -->