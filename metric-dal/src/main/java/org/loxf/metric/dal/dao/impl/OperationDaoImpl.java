package org.loxf.metric.dal.dao.impl;

import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.base.utils.MapAndBeanTransUtils;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.OperationDao;
import org.loxf.metric.dal.po.Operation;
import org.loxf.metric.dal.po.Operation;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 这个方法 不实现 只需要query就行了
 * Created by hutingting on 2017/7/4.
 */
@Service("operationDao")
public class OperationDaoImpl extends MongoDaoBase<Operation> implements OperationDao{
    private final String collectionName = CollectionConstants.OPERATION.getCollectionName();

    @Override
    public String insert(Operation object) {
        return null;
    }

    @Override
    public Operation findOne(Operation obj) {
        Map<String, Object> params = MapAndBeanTransUtils.transBean2Map(obj);
        Operation operation= super.findOne(params, collectionName);
        return operation;
    }

    @Override
    public List<Operation> findAll(Operation obj) {
        Map<String, Object> params = MapAndBeanTransUtils.transBean2Map(obj);
        List<Operation> operationList=super.findAll(params, collectionName);
        return operationList;
    }

    @Override
    public List<Operation> findByPager(Operation params, int start, int pageSize) {
        return null;
    }

    @Override
    public void update(Operation params, Map<String, Object> setParams) {
        return;
    }

    @Override
    public void updateOne(String itemCode, Map<String, Object> setParams) {
        return;
    }

    @Override
    public void remove(String itemCode) {
        return;
    }


    @Override
    public long countByParams(Operation params) {
        return 0;
    }

}
