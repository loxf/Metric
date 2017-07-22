package org.loxf.metric.dal.dao.impl;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.sun.tools.javac.util.Assert;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.base.ItemList.TargetItem;
import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.constants.ComPareConstants;
import org.loxf.metric.base.constants.StandardState;
import org.loxf.metric.base.constants.VisibleTypeEnum;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.ChartDao;
import org.loxf.metric.dal.po.Chart;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by hutingting on 2017/7/4.
 */
@Service("chartDao")
public class ChartDaoImpl extends MongoDaoBase<Chart> implements ChartDao {
    private final String collectionName = CollectionConstants.CHART.getCollectionName();
    private static String chart_prefix = "CHART_";

    @Override
    public String insert(Chart object) {
        String sid = IdGenerator.generate(chart_prefix);
        object.setChartCode(sid);
        object.setCreatedAt(new Date());
        object.setUpdatedAt(new Date());
        super.insert(object, collectionName);
        return sid;
    }

    @Override
    public long countByParams(Chart params) {
        Assert.error("不支持此方法");
        return 0;
    }

    @Override
    public long countByParams(Map<String, Object> qryParams) {
        qryParams.put("state", StandardState.AVAILABLE.getValue());
        return super.countByParams(qryParams, collectionName);
    }

    @Override
    public long countByParams(Chart object, List<String> excludeChartList, String handleUserName) {
        return super.countByParams(getCommonQuery(object, handleUserName, excludeChartList), collectionName);
    }

    @Override
    public void update(Chart params, Map<String, Object> setParams) {
        super.update(getCommonQuery(params, null, null), setParams, collectionName);
    }

    @Override
    public void updateOne(String itemCode, Map<String, Object> setParams) {
        Map<String, Object> queryParams = new HashedMap();
        queryParams.put("chartCode", itemCode);
        setParams.put("updatedAt", new Date());
        super.updateOne(queryParams, setParams, collectionName);
    }

    @Override
    public void remove(String itemCode) {
        Map<String, Object> params = new HashedMap();
        params.put("chartCode", itemCode);
        super.remove(params, collectionName);
    }

    private Query getCommonQuery(Chart chart, String handleUserName, List<String> excludeChartList) {
        BasicDBObject query = new BasicDBObject();
        if (StringUtils.isNotEmpty(chart.getChartCode())) {
            query.put("chartCode", chart.getChartCode());
        }
        if (StringUtils.isNotEmpty(chart.getChartName())) {//模糊匹配
            Pattern pattern = Pattern.compile("^.*" + chart.getChartName() + ".*$", Pattern.CASE_INSENSITIVE);
            query.put("chartName", pattern);
        }
        //有指定查看人范围
        if (VisibleTypeEnum.SPECIFICRANGE.name().equals(chart.getVisibleType())) {
            if (StringUtils.isNotEmpty(handleUserName)) {
                query.put("visibleType", VisibleTypeEnum.SPECIFICRANGE.name());
                query.put("visibleList.type", "user");
                query.put("visibleList.code", handleUserName);
            }
        }

        if (StringUtils.isNotEmpty(chart.getChartDimension())) {
            query.put("chartDim", chart.getChartDimension());
        }
        if (StringUtils.isNotEmpty(chart.getUniqueCode())) {
            query.put("uniqueCode", chart.getUniqueCode());
        }
        if (StringUtils.isNotEmpty(chart.getType())) {
            query.put("type", chart.getType());
        }
        if (StringUtils.isNotEmpty(chart.getCreateUserName())) {
            query.put("createUserName", chart.getCreateUserName());
        }

        if (chart.getStartDate() != null || chart.getEndDate() != null) {
            Map<String, Object> createT = new HashMap<>();
            if (chart.getStartDate() != null)
                createT.put("$gte", chart.getStartDate());
            if (chart.getEndDate() != null)
                createT.put("$lte", chart.getEndDate());
            query.put("createdAt", createT);
        }
        query.put("state", StandardState.AVAILABLE.getValue());
        if(CollectionUtils.isNotEmpty(excludeChartList)){
            BasicDBList excludeChartListParam = new BasicDBList();
            for(String chartCode : excludeChartList){
                excludeChartListParam.add(chartCode);
            }
            query.put("chartCode", new BasicDBObject("$nin", excludeChartListParam));
        }
        return new BasicQuery(query);
    }

    @Override
    public Chart findOne(Chart params) {
        Assert.error("不支持此方法");
        return null;
    }

    @Override
    public List<Chart> findAll(Chart params) {
        Assert.error("不支持此方法");
        return null;
    }

    @Override
    public List<Chart> findByPager(Chart params, int start, int pageSize) {
        Assert.error("不支持此方法");
        return null;
    }

    @Override
    public Chart findOne(Chart object, String handleUserName) {
        Chart chart = super.findOne(getCommonQuery(object, handleUserName, null), collectionName);
        return chart;
    }

    @Override
    public Chart findByCode(String chartCode, String handleUserName) {
        Map<String, Object> qryMap = new HashedMap();
        qryMap.put("chartCode", chartCode);
        qryMap.put("state", StandardState.AVAILABLE.getValue());
        Map<String, Map> elemMap = new HashedMap();
        Map<String, String> userMap = new HashedMap();
        userMap.put("code", handleUserName);
        elemMap.put(ComPareConstants.ELEMMATCH.getDisplayName(), userMap);
        qryMap.put("visibleList", elemMap);
        return super.findOne(qryMap, collectionName);
    }
    @Override
    public List<Chart> findAll(Chart object, String handleUserName) {
        List<Chart> chartList = super.findAll(getCommonQuery(object, handleUserName, null), collectionName);
        return chartList;
    }

    @Override
    public List<Chart> findByPager(Chart object, List<String> excludeChartList,  String handleUserName, int start, int pageSize) {
        List<Chart> chartList = super.findByPager(getCommonQuery(object, handleUserName, excludeChartList), start, pageSize, collectionName);
        return chartList;
    }

    @Override
    public List<Chart> findPagerByParams(Map<String, Object> qryParams, int start, int pageSize) {
        qryParams.put("state", StandardState.AVAILABLE.getValue());
        return super.findByPager(qryParams, start, pageSize, collectionName);
    }
}

