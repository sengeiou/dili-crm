<!-- 面板自定义标题标签，参数: panelId(面板元素id),headerId(header元素id), title(面板标题), insertFun(新增的函数名，选填)，collapsed(是否折叠，选填,true/false)  -->
<script type="text/javascript">
    //切换展示面板
    function toggle${panelId}() {
        if(isCollapse("${panelId}")){
            $("#${panelId}").panel("expand", true);
            $("#${headerId}Toggle").linkbutton({text:'收起', iconCls:'panel-tool-collapse'});
        }else{
            $("#${panelId}").panel("collapse", true);
            $("#${headerId}Toggle").linkbutton({text:'展开', iconCls:'panel-tool-expand'});
        }
    }
</script>
<div id="${headerId}" class="panel-title" style="height:24px;border: 1px solid #d4d4d4; line-height:24px; margin: 0; padding-left:10px;">
    <span id="${headerId}Title">${title}</span>
    <div style="float:right; padding-right:15px;">
    <%if(has(insertFun)){%><a  href="#" class="easyui-linkbutton" id="${headerId}Insert" iconCls="icon-add" onclick="${insertFun}()">新增</a>&nbsp;&nbsp;<%}%>
    <a  href="#" class="easyui-linkbutton" id="${headerId}Toggle"
        <%if(has(collapsed) && collapsed=="true"){%>
        iconCls="panel-tool-expand" onclick="toggle${panelId}()" >展开
        <%}else{%>
        iconCls="panel-tool-collapse" onclick="toggle${panelId}()" >收起
        <%}%>
        </a>
    </div>
</div>