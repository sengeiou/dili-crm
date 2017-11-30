<script type="text/javascript">
    //-----------------------------  城市控件js st  -----------------------------
    //标识键盘选择的时候不进行后台联想
    var isSelect = false;
    //标识城市控件是否为手工输入状态(手工输入不合法)
    var manualInput = false;

    //根据newValue加载combobx(easyui的jquery对象)
    function reloadCombobox(jq, newValue) {
        if(newValue && newValue.length >= 1){
            var opts = jq.combobox("options");
            opts.queryParams.value = jq.combobox("getValue");
            jq.combobox("reload");
//                 alert("newValue:"+newValue+", value:"+jq.combobox("getValue"));
//                 alert(jq.combobox("getData"));
        }
    }
    //联想输入框演示
    function changeName(newValue, oldValue){
        //值改变时，判断如果是选择输入，不联想
        if(isSelect) {
            isSelect = false;
            return;
        }else{//不是选择输入，则标识当前为人工输入(不合法)
            manualInput = true;
        }
        reloadCombobox($(this), newValue);
    }
    //键盘下键选择的时候设置isSelect标识为true,在onchange事件时判断isSelect标识为true不联想
    function selectName(record){
        isSelect = true;
        //标识当前为选择输入(合法)
        manualInput = false;
    }
    //-----------------------------  城市控件js end  -----------------------------
</script>