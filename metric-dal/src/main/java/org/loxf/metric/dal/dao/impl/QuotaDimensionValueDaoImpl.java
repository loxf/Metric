package org.loxf.metric.dal.dao.impl;

import com.mongodb.BasicDBObject;
import com.sun.tools.javac.util.Assert;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.QuotaDimensionValueDao;
import org.loxf.metric.dal.po.QuotaDimensionValue;
import org.loxf.metric.dal.po.QuotaDimensionValue;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by hutingting on 2017/7/4.
 */
@Service("quotaDimensionValue")
public class QuotaDimensionValueDaoImpl extends MongoDaoBase<QuotaDimensionValue> implements QuotaDimensionValueDao{
    private final String collectionName = CollectionConstants.QUOTA_DIMENSION_VALUE.getCollectionName();
    private static String quota_dim_value_prefix = "DIM_V_";

    @Override
    public String insert(QuotaDimensionValue object) {
        Assert.error("不支持单个维度值的插入");
        return null;
    }

    @Override
    public QuotaDimensionValue findOne(QuotaDimensionValue object) {
        QuotaDimensionValue quotaDimensionValue= super.findOne(getCommonQuery(object), collectionName);
        return quotaDimensionValue;
    }

    @Override
    public List<QuotaDimensionValue> findAll(QuotaDimensionValue object) {
        List<QuotaDimensionValue> quotaDimensionValueList=super.findAll(getCommonQuery(object), collectionName);
        return quotaDimensionValueList;
    }

    @Override
    public List<QuotaDimensionValue> findByPager(QuotaDimensionValue object, int start, int pageSize) {
        List<QuotaDimensionValue> QuotaDimensionValueList=super.findByPager(getCommonQuery(object), start, pageSize, collectionName);
        return QuotaDimensionValueList;
    }

    @Override
    public void update(QuotaDimensionValue object, Map<String, Object> setParams) {
        Assert.error("不支持维度值更新");
    }

    @Override
    public void updateOne(String itemCode, Map<String, Object> setParams) {
        Assert.error("不支持维度值更新");
    }

    @Override
    public void remove(String itemCode) {
        Assert.error("不支持维度值根据CODE删除");
    }

    @Override
    public long countByParams(QuotaDimensionValue object) {
        return super.countByParams(getCommonQuery(object),collectionName);
    }

    private Query getCommonQuery(QuotaDimensionValue quotaDimensionValue){
        BasicDBObject query = new BasicDBObject();
        if (StringUtils.isNotEmpty(quotaDimensionValue.getDimCode())) {
            query.put("dimCode", quotaDimensionValue.getDimCode());
        }
        if (StringUtils.isNotEmpty(quotaDimensionValue.getDimName())) {
            Pattern pattern = Pattern.compile("^.*" + quotaDimensionValue.getDimName() + ".*$", Pattern.CASE_INSENSITIVE);
            query.put("dimName", pattern);
        }
        if (StringUtils.isNotEmpty(quotaDimensionValue.getUniqueCode())) {
            query.put("uniqueCode", quotaDimensionValue.getUniqueCode());
        }
        return new BasicQuery(query);
    }

    @Override
    public void saveDimensionValue(Date circleTime, String key, Set<String> values) {
        Map map = new HashMap();
        for(String val : values){
            map.put("dimValue", val);
        }
    }
}
