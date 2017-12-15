<script type="text/javascript">
    // ============================   客户画像js st  =============================
    function loadSalePortrait() {
        $("#salePortrait").attr("src", "http://ap.nong12.com/find/sZesc_order_details1Z0tftftfff0.html?dt=${startDate} - ${endDate}&customer_id=${customerId}");
    }
    
    function loadPurchasePortrait() {
        $("#purchasePortrait").attr("src", "http://ap.nong12.com/find/sZesc_order_details2Z0fftftfff0.html?dt=${startDate} - ${endDate}&customer_id=${customerId}");
    }
    
    function loadContributionPortrait() {
        $("#contributionPortrait").attr("src", "http://ap.nong12.com/find/sZuser_contributionZ0tftftfff0.html?dt=${startDate} - ${endDate}&created_id=${customerId}");
    }
    // ============================   客户画像js end  =============================
</script>