<a href="http://crm.diligrp.com:8085/crm/index.html">首页</a>&nbsp;&nbsp;当前位置:
<%
for(parentMenu in parentMenus){
var url = @parentMenu.getMenuUrl();
url = (url == null || url == "") ? "" : url;
var href = url == "" ? "" : "href=\""+url+"\"";
if(parentMenuLP.last){
%>
<a >${@parentMenu.getName()}</a>
<%} else {%>
<a ${href}>${@parentMenu.getName()}</a> >
<%}
}%>