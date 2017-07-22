package org.loxf.metric.dal.dao.impl;

import com.mongodb.BasicDBObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.base.ItemList.ChartItem;
import org.loxf.metric.base.ItemList.VisibleItem;
import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.constants.StandardState;
import org.loxf.metric.base.constants.VisibleTypeEnum;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.ChartDao;
import org.loxf.metric.dal.dao.interfaces.IndexSettingDao;
import org.loxf.metric.dal.po.Chart;
import org.loxf.metric.dal.po.IndexSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by luohj on 2017/7/19.
 */
@Component
public class IndexSettingDaoImpl extends MongoDaoBase implements IndexSettingDao<IndexSetting> {
    private static String collectionName = CollectionConstants.INDEX_SETTING.getCollectionName();
    @Autowired
    private ChartDao chartDao;
    @Override
    public void updateSetting(List<ChartItem> data, String handlerUserName, String uniqueCode) {
        Query query = getCommonQuery(handlerUserName, uniqueCode);
        long count = super.countByParams(query, collectionName);
        if(count>0){
            Map<String, Object> setParams = new HashedMap();
            setParams.put("chartList", data);
            super.update(query, setParams, collectionName);
        } else {
            IndexSetting setting = new IndexSetting();
            setting.setChartList(data);
            setting.setUniqueCode(uniqueCode);
            setting.setCreateUserName(handlerUserName);
            setting.setUpdateUserName(handlerUserName);
            Date now = new Date();
            setting.setCreatedAt(now);
            setting.setUpdatedAt(now);
            super.insert(setting, collectionName);
        }
    }

    @Override
    public List<ChartItem> getIndexSetting(String handlerUserName, String visibleType, String uniqueCode) {
        Query query = getCommonQuery(handlerUserName, uniqueCode);
        IndexSetting settings = (IndexSetting)super.findOne(query, collectionName);
        if(settings!=null){
            List<ChartItem> chartItemList = settings.getChartList();
            if(visibleType.equals(VisibleTypeEnum.ALL.name())){
                return chartItemList;
            } else {
                // TODO 连表查询改造
                Chart chartParam = new Chart();
                chartParam.setVisibleType(visibleType);
                if (CollectionUtils.isNotEmpty(chartItemList)) {
                    List<ChartItem> result = new ArrayList<>();
                    for(ChartItem chartItem : chartItemList) {
                        chartParam.setChartCode(chartItem.getChartCode());
                        long count = chartDao.countByParams(chartParam, null, handlerUserName);
                        if (count > 0) {
                            result.add(chartItem);
                        }
                    }
                    return result;
                }
                return chartItemList;
            }
        }
        return null;
    }


    private Query getCommonQuery(String handlerUserName, String uniqueCode) {
        BasicDBObject query = new BasicDBObject();
        query.put("uniqueCode", uniqueCode);
        query.put("createdUserName", handlerUserName);
        return new BasicQuery(query);
    }

}
