<p />
<div id="portraitDiv" align="center" >
    <table style="padding:0px;width:96%;">
        <!--<tr>-->
            <!--<td colspan="2" style="height:25px;font-size: 15px;color: inherit;font-weight:bold;">客户画像</td>-->
        <!--</tr>-->
        <tr>
            <td width="50%" style="padding: 0px; padding-right: 6px; margin: 0px;" valign="top">
                <!--<div style="height:25px;padding:0px;font-size: 15px;color: inherit;font-weight:bold;">近一月销售</div>-->
                <div class="easyui-panel" id="salesReportDiv" border="false" data-options="collapsible:true,collapsed:true,onBeforeExpand:loadSalePortrait" header="#salesReportHeader">
                    <#controls_panelHeader panelId="salesReportDiv" headerId="salesReportHeader" title="近一月销售" collapsed="true"></#controls_panelHeader>
                    <iframe scrolling="no" frameborder="0" id="salePortrait" style="width:99%;height:210px;"></iframe>
                </div>
            </td>
            <td width="50%" style="padding: 0px; padding-left: 6px; margin: 0px;" valign="top">
                <!--<div style="height:25px;padding:0px;font-size: 15px;color: inherit;font-weight:bold;">近一月采购</div>-->
                <div class="easyui-panel" id="purchaseReportDiv" border="false" data-options="collapsible:true,collapsed:true,onBeforeExpand:loadPurchasePortrait" header="#purchaseReportHeader">
                    <#controls_panelHeader panelId="purchaseReportDiv" headerId="purchaseReportHeader" title="近一月采购" collapsed="true"></#controls_panelHeader>
                    <iframe scrolling="no" frameborder="0" id="purchasePortrait" style="width:99%;height:210px;"></iframe>
                </div>
            </td>
        </tr>
        <tr>
            <td style="padding: 0px; padding-right: 6px; margin: 0px;" valign="top">
                <!--<div style="height:25px;padding:0px;font-size: 15px;color: inherit;font-weight:bold;">近一月市场贡献度</div>-->
                <div class="easyui-panel" id="contributionReportDiv" border="false" data-options="collapsible:true,collapsed:true,onBeforeExpand:loadContributionPortrait" header="#contributionReportHeader">
                    <#controls_panelHeader panelId="contributionReportDiv" headerId="contributionReportHeader" title="近一月市场贡献度" collapsed="true"></#controls_panelHeader>
                    <iframe scrolling="no" frameborder="0" id="contributionPortrait" style="width:99%;height:210px;"></iframe>
                </div>
            </td>
            <td></td>
        </tr>
    </table>
</div>