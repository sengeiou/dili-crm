package com.dili.crm.provider;

import com.dili.crm.rpc.UserRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.sdk.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 所有者提供者
 */
@Component
public class OwnerProvider implements ValueProvider {

    @Autowired
    private UserRpc userRpc;

    //不提供下拉数据
    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        if(obj == null || "".equals(obj)){
            return null;
        }
        BaseOutput<User> userBaseOutput= userRpc.get(Long.parseLong(obj.toString()));
        User user = userBaseOutput.getData();
        if(user == null) return null;
        return user.getRealName();
    }
}