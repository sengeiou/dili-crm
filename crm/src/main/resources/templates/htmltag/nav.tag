<a href="http://crm.diligrp.com:8085/crm/index.html" style="font-size:14px; display: inline;">首页</a>&nbsp;&nbsp;<div style="font-size:14px; display: inline;">当前位置:</div>
<%
for(parentMenu in parentMenus){
var url = @parentMenu.getMenuUrl();
url = (url == null || url == "") ? "" : url;
var href = url == "" ? "" : "href=\""+url+"\"";
if(parentMenuLP.last){
%>
<a style="font-size:14px; display: inline;">${@parentMenu.getName()}</a>
<%} else {%>
<a ${href} style="font-size:14px; display: inline;">${@parentMenu.getName()}</a> <div style="font-size:14px; display: inline;"> > </div>
<%}
}%>