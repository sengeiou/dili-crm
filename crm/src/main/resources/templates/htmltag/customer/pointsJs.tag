<script type="text/javascript">
    // ============================   客户积分相关js st  =============================
    function listPoints(){
        var opts = $("#pointsGrid").datagrid("options");
        if (null == opts.url || "" == opts.url) {
            opts.url = "${contextPath}/customer/listCustomerPoints.action";
        }
        $("#pointsGrid").datagrid("load",  {"customerId": "${customerId}"});
    }
    // ============================   客户积分相关js end  =============================
</script>
