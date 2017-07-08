//package org.loxf.metric.service;
//
//import org.apache.commons.collections.map.HashedMap;
//import java.lang.reflect.Method;
//import java.util.Date;
//import java.util.Map;
//
///**
// * Created by hutingting on 2017/7/7.
// */
//public class BaseManager<T1,T2> {
//    private Class<?>  daoClass;
//
//    public void insert(T1 poObject, T2 dtoObject) {
//        try {
//            Method insertMethod = daoClass.getDeclaredMethod("insert", poObject.getClass());
//            insertMethod.invoke(poObject);
//        }catch (Exception e){
//
//        }
//    }
//
////    public BoardDto getBoardByBoardId(String boardId) {
////        Map<String, Object> params=new HashedMap();
////        params.put("boardId",boardId);
////        BoardDto boardDto=new BoardDto();
////        Board board = boardDao.findOne(params);
////        BeanUtils.copyProperties(board, boardDto);
////        return boardDto;
////    }
//
//    public void updateByParams(Map<String, Object> qryParams,Map<String, Object> setParams) {
//        setParams.put("updatedAt",new Date());
//        try {
//            Method insertMethod = daoClass.getDeclaredMethod("update", Map.class,Map.class);
//            insertMethod.invoke(qryParams,setParams);
//        }catch (Exception e){
//
//        }
//    }
//
//    public void delById(Map<String, Object> delParams) {
//        try {
//            Method insertMethod = daoClass.getDeclaredMethod("remove", Map.class);
//            insertMethod.invoke(delParams);
//        }catch (Exception e){
//
//        }
//    }
//
//    public Class<?> getDaoClass() {
//        return daoClass;
//    }
//
//    public void setDaoClass(Class<?> daoClass) {
//        this.daoClass = daoClass;
//    }
//}
//
