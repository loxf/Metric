//package org.loxf.metric.service;
//
//import org.loxf.metric.common.dto.OperationDto;
//import org.loxf.metric.dal.dao.interfaces.operationDao;
//import org.loxf.metric.dal.dao.interfaces.OperationDao;
//import org.loxf.metric.dal.po.Operation;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.Map;
//
///**
// * Created by hutingting on 2017/7/7.
// */
//@Component
//public class OperationManager {
//    @Autowired
//    private OperationDao operationDao;
//
//    public void insert(OperationDto OperationDto) {
//        Operation Operation = new Operation();
//        BeanUtils.copyProperties(OperationDto, Operation);
//        Operation.setCreatedAt(new Date());
//        Operation.setUpdatedAt(new Date());
//        operationDao.insert(Operation);
//    }
//
//    public OperationDto getOperationByParams(Map<String, Object> params) {
//        OperationDto OperationDto=new OperationDto();
//        Operation Operation = operationDao.findOne(params);
//        BeanUtils.copyProperties(Operation, OperationDto);
//        return OperationDto;
//    }
//
//    public void updateOperation(Map<String, Object> qryParams,Map<String, Object> setParams) {
//        setParams.put("updatedAt",new Date());
//        operationDao.update(qryParams,setParams);
//    }
//
//    public void delOperationByParams(Map<String, Object> delParams) {
//        operationDao.remove(delParams);
//    }
//}
