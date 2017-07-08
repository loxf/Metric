package org.loxf.metric.dal.dao.impl;

import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.OperationDao;
import org.loxf.metric.dal.po.Operation;
import org.loxf.metric.dal.po.Operation;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by hutingting on 2017/7/4.
 */
@Service("operationDao")
public class OperationDaoImpl extends MongoDaoBase<Operation> implements OperationDao{
    private final String collectionName = CollectionConstants.OPERATION.name();
    private static String oper_prefix = "OPERRATION_";

    @Override
    public String insert(Operation object) {
        String sid = IdGenerator.generate(oper_prefix);
        object.setOperationCode(sid);
        object.handleDateToMongo();
        super.insert(object, collectionName);
        return  sid;
    }

    @Override
    public Operation findOne(Map<String, Object> params) {
        Operation operation= super.findOne(params, collectionName);
        operation.handleMongoDateToJava();
        return operation;
    }

    private void handleDateForList(List<Operation> list){
        for(Operation operation:list){
            operation.handleMongoDateToJava();
        }
    }
    @Override
    public List<Operation> findAll(Map<String, Object> params) {
        List<Operation> operationList=super.findAll(params, collectionName);
        handleDateForList(operationList);
        return operationList;
    }

    @Override
    public List<Operation> findByPager(Map<String, Object> params, int start, int pageSize) {
        List<Operation> operationList=super.findByPager(params, start, pageSize, collectionName);
        handleDateForList(operationList);
        return operationList;
    }

    @Override
    public void update(Map<String, Object> queryParams, Map<String, Object> setParams) {
        super.update(queryParams, setParams, collectionName);
    }

    @Override
    public void updateOne(Map<String, Object> queryParams, Map<String, Object> setParams) {
        super.updateOne(queryParams, setParams, collectionName);
    }

    @Override
    public void remove(Map<String, Object> params) {
        super.remove(params, collectionName);
    }

    @Override
    public long countByParams(Map<String, Object> params) {
        return super.countByParams(params,collectionName);
    }

}
