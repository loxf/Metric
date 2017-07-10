package org.loxf.metric.service.base;

import org.apache.log4j.Logger;
import org.loxf.metric.common.dto.PageData;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by hutingting on 2017/7/6.
 */
public class BasePageHandleUtil {
    Logger logger = Logger.getLogger(this.getClass());

    public PageData getPageResult(Class<? extends Object> daoClzz, Map<String, Object> params, int start, int pageSize) {
        try {
            Object dao = SpringApplicationContextUtil.getBean(daoClzz);
            Class<?> clazz = dao.getClass();
            Method countMethod = clazz.getDeclaredMethod("countByParams", Map.class);
            int totalCount = (Integer) countMethod.invoke(params);
            if (totalCount <= 0) {
                return null;
            }
            Method listMethod = clazz.getDeclaredMethod("findByPager", Map.class,int.class,int.class);
            List pageResult=(List) listMethod.invoke(params,start,pageSize);
            PageData pageData=new PageData();
            pageData.setRecords(totalCount);
            pageData.setRownum(pageSize);
            pageData.setRows(pageResult);
            return pageData;
        }catch (Exception e){
            logger.error("查询分页异常！", e);
            return null;
        }
    }
}
