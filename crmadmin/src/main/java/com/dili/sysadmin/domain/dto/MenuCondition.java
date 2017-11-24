package com.dili.sysadmin.domain.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.sysadmin.domain.Menu;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2017-09-06 14:50:38.
 */
@Table(name = "`menu`")
public class MenuCondition extends Menu {

    /**
     * 父级菜单id
     */
    @Column(name = "`id`")
    @Operator(Operator.IN)
    private List<Long> ids;

    /**
     * 获取id
     *
     * @return ids - 父级菜单id列表
     */
    @FieldDef(label="id列表")
    @EditMode(editor = FieldEditor.Number, required = false)
    public List<Long> getIds() {
        return ids;
    }

    /**
     * 设置父级菜单id
     *
     * @param ids 父级菜单id
     */
    public void setIds(List<Long> ids) {
        this.ids = ids;
    }


}