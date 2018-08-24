package com.dili.points.domain.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.uap.sdk.domain.User;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.List;

@Table(name = "`user`")
public interface UserDto extends User {

    @Operator(Operator.IN)
    @Column(name = "`firm_code`")
    List<String> getFirmCodes();

    void setFirmCodes(List<String> firmCodes);
}
