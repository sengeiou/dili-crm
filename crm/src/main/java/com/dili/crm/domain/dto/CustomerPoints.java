package com.dili.crm.domain.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2018-03-20 11:29:30.
 */
public interface CustomerPoints extends IBaseDomain {
    Long getId();

    void setId(Long id);

    String getCertificateNumber();

    void setCertificateNumber(String certificateNumber);


    Integer getAvailable();

    void setAvailable(Integer available);

    Integer getFrozen();

    void setFrozen(Integer frozen);

    Integer getTotal();

    void setTotal(Integer total);

    Long getModifiedId();

    void setModifiedId(Long modifiedId);

    Date getCreated();

    void setCreated(Date created);

    Date getModified();

    void setModified(Date modified);
}