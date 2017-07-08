package org.loxf.metric.biz.base;

import org.apache.log4j.Logger;
import org.loxf.metric.common.dto.PageData;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by hutingting on 2017/7/6.
 */
public class BasePageHandleUtils {
    Logger logger = Logger.getLogger(this.getClass());

    public PageData getPageResult(Class<? extends Object> daoClzz, Map<String, Object> params, Integer totalCount, int start, int pageSize) {
        try {
            Object dao = SpringApplicationContextUtil.getBean(daoClzz);
            Class<?> clazz = dao.getClass();
            if(totalCount!=null){
                Method countMethod = clazz.getDeclaredMethod("countByParams", Map.class);
                totalCount = (Integer) countMethod.invoke(params);
                if (totalCount <= 0) {
                    return null;
                }
            }
            PageData pageData=new PageData();
            pageData.setTotalRecords(totalCount);
            Method countMethod = clazz.getDeclaredMethod("findByPager", Map.class,int.class,int.class);
            List pageResult=(List) countMethod.invoke(params,start,pageSize);
            pageData.setRows(pageResult);
            pageData.setTotalPage(totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1);
            pageData.setCurrentPage(start%pageSize==0?start/pageSize:start/pageSize+1);
            pageData.setRownum(pageResult.size());
            return pageData;
        }catch (Exception e){
            logger.error("查询分页异常！",e);
            return null;
        }
    }
}
