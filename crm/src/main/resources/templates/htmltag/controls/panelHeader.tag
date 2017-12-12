<!-- 面板自定义标题标签，参数: panelId(面板元素id),headerId(header元素id), title(面板标题), insertFun(新增的函数名，选填)，collapsed(是否折叠，选填,true/false)  -->
<script type="text/javascript">
    //切换展示面板
    function toggle${panelId}() {
        if(isCollapse("${panelId}")){
            $("#${panelId}").panel("expand", true);
            $("#${headerId}Toggle").linkbutton({iconCls:'panel-tool-collapse'});
        }else{
            $("#${panelId}").panel("collapse", true);
            $("#${headerId}Toggle").linkbutton({iconCls:'panel-tool-expand'});
        }
    }
</script>
<div id="${headerId}" class="panel-title" style="height:24px; line-height:24px; margin: 0; padding-left:10px;">
    <span id="${headerId}Title">${title}
     <%if(has(insertFun) && insertFun != ""){%>&nbsp;&nbsp;<a  href="#" class="easyui-linkbutton" id="${headerId}Insert" iconCls="icon-add" onclick="${insertFun}()"></a>&nbsp;&nbsp;<%}%>
    </span>
    <div style="float:right; padding-right:10px;">
    <a  href="#" class="easyui-linkbutton" id="${headerId}Toggle"
        <%if(has(collapsed) && collapsed=="true"){%>
        iconCls="panel-tool-expand" onclick="toggle${panelId}()" >
        <%}else{%>
        iconCls="panel-tool-collapse" onclick="toggle${panelId}()" >
        <%}%>
        </a>
    </div>
</div>