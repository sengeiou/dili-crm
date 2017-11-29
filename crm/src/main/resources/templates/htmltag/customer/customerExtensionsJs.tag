<script type="text/javascript">
    // ============================   业务系统账号信息相关js st  =============================

    //修改客户窗口打开时查询业务系统账号信息
    function listCustomerExtensions(){
        <%if (has(customer)){%>
        var opts = $("#customerExtensionsGrid").datagrid("options");
        if (null == opts.url || "" == opts.url) {
            opts.url = "${contextPath}/customerExtensions/listPage";
        }
        var selected = ${customer};
        $("#customerExtensionsGrid").datagrid("load",  bindGridMeta2Data("customerExtensionsGrid", {"customerId":selected["id"]}));
        <%}%>
    }

    // ============================   业务系统账号信息相关js end  =============================


</script>