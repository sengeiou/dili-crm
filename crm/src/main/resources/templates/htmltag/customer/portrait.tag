<div id="portraitDiv" align="center" class="easyui-panel" style="width: 96%" title="客户画像" collapsible="true">

    <table style="padding:0px;width:100%;">
        <!--<tr>-->
            <!--<td colspan="2" style="height:25px;font-size: 15px;color: inherit;font-weight:bold;">客户画像</td>-->
        <!--</tr>-->
        <tr>
            <td width="50%" style="padding: 0px; margin: 0px;" valign="top">
                <!--<div style="height:25px;padding:0px;font-size: 15px;color: inherit;font-weight:bold;">近一月销售</div>-->
                <div class="easyui-panel" title="近一月销售" data-options="collapsible:true,collapsed:true,onBeforeExpand:loadSalePortrait">
                    <iframe scrolling="no" frameborder="0" id="salePortrait" style="width:99%;height:210px;"></iframe>
                </div>
            </td>
            <td width="50%" style="padding: 0px; margin: 0px;" valign="top">
                <!--<div style="height:25px;padding:0px;font-size: 15px;color: inherit;font-weight:bold;">近一月采购</div>-->
                <div class="easyui-panel" title="近一月采购" data-options="collapsible:true,collapsed:true,onBeforeExpand:loadPurchasePortrait">
                <iframe scrolling="no" frameborder="0" id="purchasePortrait" style="width:99%;height:210px;"></iframe>
                </div>
            </td>
        </tr>
        <tr>
            <td style="padding: 0px; margin: 0px;" valign="top">
                <!--<div style="height:25px;padding:0px;font-size: 15px;color: inherit;font-weight:bold;">近一月市场贡献度</div>-->
                <div class="easyui-panel" title="近一月市场贡献度" data-options="collapsible:true,collapsed:true,onBeforeExpand:loadContributionPortrait">
                <iframe scrolling="no" frameborder="0" id="contributionPortrait" style="width:99%;height:210px;"></iframe>
                </div>
            </td>
            <td></td>
        </tr>
    </table>
</div>