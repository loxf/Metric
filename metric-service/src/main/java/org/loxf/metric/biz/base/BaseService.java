package org.loxf.metric.biz.base;

import com.github.pagehelper.PageHelper;
import org.apache.log4j.Logger;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.dal.po.BasePO;
import org.loxf.metric.dal.po.Common;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by caiyang on 2017/3/27.
 */
public class BaseService {
    Logger logger = Logger.getLogger(this.getClass());

    public PageData getPageResult(Class<? extends Object> daoClazz, Map<String, Object> params,int start,int pageSize) {
        try {
            Object dao = SpringApplicationContextUtil.getBean(daoClazz);
            Class<?> clazz = dao.getClass();
            Method countMethod = clazz.getDeclaredMethod("countByParams", Map.class);
            int totalCount = (Integer) countMethod.invoke(params);
            if (totalCount <= 0) {
                return null;
            }
            PageData pageData=new PageData();
            pageData.setRecords(totalCount);
            Method listMethod = clazz.getDeclaredMethod("findByPager", Map.class, int.class, int.class);
            List pageResult= (List) listMethod.invoke(params, start, pageSize);
            pageData.setRows(pageResult);
            pageData.setCurrentPage(start%pageSize==0?start/pageSize:start/pageSize+1);
            pageData.setRownum(pageResult.size());
            return pageData;
        }catch (Exception e){
            logger.error("查询分页异常！",e);
            return null;
        }
    }

}
