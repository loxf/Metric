package org.loxf.metric.service.utils;

import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.base.ItemList.QuotaDimItem;
import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.common.dto.Condition;
import org.loxf.metric.common.dto.ConditionVo;
import org.loxf.metric.dal.dao.interfaces.QuotaDao;
import org.loxf.metric.dal.po.Quota;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.dal.po.QuotaDimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/5/9.
 */
@Component
public class ValidConditionUtil {
    @Autowired
    private QuotaDao quotaDao;

    public Quota valid(String quotaCode, ConditionVo vo) throws MetricException {
        if (StringUtils.isEmpty(quotaCode)) {
            throw new MetricException("指标CODE为空");
        }
        Quota param = new Quota();
        param.setQuotaCode(quotaCode);
        Quota quota = quotaDao.findOne(param);
        if (quota == null) {
            throw new MetricException("指标CODE不正确");
        }
        if (vo.getCondition() != null) {
            List<QuotaDimItem> dimensions = quota.getQuotaDim();
            if (dimensions != null)
                for (Condition con : vo.getCondition()) {
                    boolean isFound = false;
                    for (QuotaDimItem quotaDimItem : dimensions) {
                        if (quotaDimItem.getDimCode().equals(con.getCode())) {
                            isFound = true;
                            break;
                        }
                    }
                    if (!isFound) {
                        throw new MetricException("指标未配置当前条件：" + con.getCode(), "CHECK_CONDITION_01");
                    }
                }
        }
        return quota;
    }
}
