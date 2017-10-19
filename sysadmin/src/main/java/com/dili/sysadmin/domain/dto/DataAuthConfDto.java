package com.dili.sysadmin.domain.dto;

/**
 * Created by Administrator on 2015/10/6.
 */
public class DataAuthConfDto {
    private String type;
    private String label;
    private Boolean change;
    private Boolean show;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getChange() {
        return change;
    }

    public void setChange(Boolean change) {
        this.change = change;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }
}
