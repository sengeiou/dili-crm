首页:
<%
for(parentMenu in parentMenus){
var url = @parentMenu.getMenuUrl();
url = (url == null || url == "") ? "" : url;
var href = url == "" ? "" : "href=\""+url+"\"";
if(parentMenuLP.last){
%>
<a >${@parentMenu.getName()}</a>
<%} else {%>
<a ${href}>${@parentMenu.getName()}</a> ->
<%}
}%>