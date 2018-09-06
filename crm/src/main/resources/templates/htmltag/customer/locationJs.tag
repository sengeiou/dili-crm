<script type="text/javascript">
    // ============================   地图位置相关js st  =============================
    //默认icon
    var myIcon = new BMap.Icon("${contextPath}/resources/images/icon/pos-icon.png", new BMap.Size(38, 38), {
        // 指定定位位置。
        // 当标注显示在地图上时，其所指向的地理位置距离图标左上
        // 角各偏移7像素和20像素。
        anchor: new BMap.Size(7, 20)
    });
    //销售者icon
    var saleIcon = new BMap.Icon("${contextPath}/resources/images/icon/sale.png", new BMap.Size(38, 38), {
        anchor: new BMap.Size(7, 20)
    });
    //采购者icon
    var purchaseIcon = new BMap.Icon("${contextPath}/resources/images/icon/purchase.png", new BMap.Size(38, 38), {
        anchor: new BMap.Size(7, 20)
    });

    /**
     * 根据客户类型，返回不同的icon
     * @param c_type
     * @returns {BMap.Icon|string}
     */
    function getLocationIcon(c_type) {
        var t_icon = "";
        switch (c_type){
            case "purchase":
                t_icon = purchaseIcon;
                break;
            case "sale":
                t_icon = saleIcon;
                break;
            default:
                t_icon = myIcon;
                break;
        }
        return t_icon;
    }
    // ============================   地图位置相关js end  =============================
</script>