package org.loxf.metric.dal.dao.interfaces;

import org.loxf.metric.base.ItemList.ChartItem;
import org.loxf.metric.core.mongo.IBaseDao;
import org.loxf.metric.dal.po.Chart;

import java.util.List;

/**
 * Created by luohj on 2017/7/19.
 */
public interface IndexSettingDao<T> {
    public void updateSetting(List<ChartItem> data, String handlerUserName, String uniqueCode);
    public List<ChartItem> getIndexSetting(String handlerUserName, String visibleType, String uniqueCode);
}
