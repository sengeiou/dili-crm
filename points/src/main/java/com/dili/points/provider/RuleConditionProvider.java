package com.dili.points.provider;

import com.alibaba.fastjson.JSON;
import com.dili.points.constant.Constants;
import com.dili.points.domain.PointsRule;
import com.dili.points.domain.RuleCondition;
import com.dili.points.service.RuleConditionService;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RuleConditionProvider implements ValueProvider {

    @Autowired
    private RuleConditionService ruleConditionService;

    @Override
    public List<ValuePair<?>> getLookupList(Object o, Map map, FieldMeta fieldMeta) {
        return null;
    }

    @Override
    public String getDisplayText(Object o, Map map, FieldMeta fieldMeta) {
        PointsRule pointsRule = (PointsRule) map.get("_rowData");
        RuleCondition example = DTOUtils.newDTO(RuleCondition.class);
        example.setPointRuleId(pointsRule.getId());
        List<RuleCondition> ruleConditions = ruleConditionService.listByExample(example);
        StringBuilder sb = new StringBuilder();
        String splicer = ";";
        for (RuleCondition ruleCondition : ruleConditions) {
            String nameByCode = Constants.WeightType.getNameByCode(ruleCondition.getWeightType());
            if (!sb.toString().contains(nameByCode + splicer)) {
                sb.append(nameByCode);
                sb.append(splicer);
            }
        }
        if (sb.length() == 0) {
            return "无";
        }
        return sb.toString();
    }
}
