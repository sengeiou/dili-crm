<script type="text/javascript">
    // ============================   客户画像js st  =============================
    function loadSalePortrait() {
        $("#salePortrait").attr("src", "${clientSalesTopChartUrl}?rows=10000&dt=${startDate} - ${endDate}&customer_id=${customerId}");
    }
    
    function loadPurchasePortrait() {
        $("#purchasePortrait").attr("src", "${clientPurchasingTopChartUrl}?rows=10000&dt=${startDate} - ${endDate}&customer_id=${customerId}");
    }
    
    function loadContributionPortrait() {
        $("#contributionPortrait").attr("src", "${clientUserContributionChartUrl}?rows=10000&dt=${startDate} - ${endDate}&created_id=${customerId}");
    }
    // ============================   客户画像js end  =============================
</script>