package org.loxf.metric.dal.dao.impl;

import com.mongodb.BasicDBObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.QuotaDimensionDao;
import org.loxf.metric.dal.po.QuotaDimension;
import org.loxf.metric.dal.po.QuotaDimension;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by hutingting on 2017/7/4.
 */
@Service("quotaDimension")
public class QuotaDimensionDaoImpl extends MongoDaoBase<QuotaDimension> implements QuotaDimensionDao {
    private final String collectionName = CollectionConstants.QUOTA_DIMENSION.getCollectionName();
    private static String quota_dim_prefix = "DIM_";

    @Override
    public String insert(QuotaDimension object) {
        String sid = IdGenerator.generate(quota_dim_prefix);
        object.setDimCode(sid);
        object.setCreatedAt(new Date());
        object.setUpdatedAt(new Date());
        super.insert(object, collectionName);
        return object.getDimCode();
    }

    @Override
    public QuotaDimension findOne(QuotaDimension object) {
        QuotaDimension quotaDimension = super.findOne(getCommonQuery(object), collectionName);
        return quotaDimension;
    }

    @Override
    public List<QuotaDimension> findAll(QuotaDimension object) {
        List<QuotaDimension> quotaDimensionList = super.findAll(getCommonQuery(object), collectionName);
        return quotaDimensionList;
    }

    @Override
    public List<QuotaDimension> findByPager(QuotaDimension object, int start, int pageSize) {
        List<QuotaDimension> quotaDimensionList = super.findByPager(getCommonQuery(object), start, pageSize, collectionName);
        return quotaDimensionList;
    }

    @Override
    public void update(QuotaDimension object, Map<String, Object> setParams) {
        setParams.put("updatedAt", new Date());
        super.update(getCommonQuery(object), setParams, collectionName);
    }

    @Override
    public void updateOne(String itemCode, Map<String, Object> setParams) {
        setParams.put("updatedAt", new Date());
        Map<String, Object> queryParams = new HashedMap();
        queryParams.put("dimCode", itemCode);
        super.updateOne(queryParams, setParams, collectionName);
    }

    @Override
    public void remove(String itemCode) {
        Map<String, Object> params = new HashedMap();
        params.put("dimCode", itemCode);
        super.remove(params, collectionName);
    }

    @Override
    public long countByParams(QuotaDimension object) {
        return super.countByParams(getCommonQuery(object), collectionName);
    }

    private Query getCommonQuery(QuotaDimension quotaDimension) {
        BasicDBObject query = new BasicDBObject();
        if (StringUtils.isNotEmpty(quotaDimension.getDimCode())) {
            query.put("dimCode", quotaDimension.getDimCode());
        }
        if (StringUtils.isNotEmpty(quotaDimension.getDimName())) {
            Pattern pattern = Pattern.compile("^.*" + quotaDimension.getDimName() + ".*$", Pattern.CASE_INSENSITIVE);
            query.put("dimName", pattern);
        }
        if (StringUtils.isNotEmpty(quotaDimension.getUniqueCode())) {
            query.put("uniqueCode", quotaDimension.getUniqueCode());
        }
        return new BasicQuery(query);
    }

    @Override
    public QuotaDimension findOne(Map object) {
        return super.findOne(object, collectionName);
    }

    @Override
    public void updateOne(Map params, String dimName) {
        Map setMap = new HashMap();
        setMap.put("dimName", dimName);
        setMap.put("updatedAt", new Date());
        super.updateOne(params, setMap, collectionName);
    }

    @Override
    public void remove(String itemCode, String uniqueCode) {
        Map<String, Object> params = new HashedMap();
        params.put("dimCode", itemCode);
        params.put("uniqueCode", uniqueCode);
        super.remove(params, collectionName);
    }
}
