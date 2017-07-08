package org.loxf.metric.biz.base;

import com.github.pagehelper.PageHelper;
import org.apache.log4j.Logger;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.dal.po.BasePO;
import org.loxf.metric.dal.po.Common;
import org.loxf.metric.utils.ApplicationContextUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by caiyang on 2017/3/27.
 */
public class BaseService {
    Logger logger = Logger.getLogger(this.getClass());

    public PageData getPageResult(Class<? extends Object> daoClzz, Map<String, Object> params,Integer totalCount,int start,int pageSize) {
        try {
            Object dao = ApplicationContextUtil.getBean(daoClzz);
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

    public PageData pageList(Common entity, Class<? extends Object> daoClzz, String suffix) {
        /*if (null == entity) {
            return new PageData(0, 0, new ArrayList<Common>());
        }
        try {
            Object dao = SpringApplicationContextUtil.getBean(daoClzz);
            Class<?> clazz = dao.getClass();
            Pager pager = entity.getPager();
            if (null == pager) {//不分页，查询所有数据
                Method listMethod = clazz.getDeclaredMethod("list" + suffix, entity.getClass());
                List<Common> list = (List<Common>) listMethod.invoke(dao, entity);
                return new PageData(1, list.size(), list);
            } else if (pager.isCount()) {//获取总总记录数
                Method countMethod = clazz.getDeclaredMethod("count" + suffix, entity.getClass());
                int totalCount = (Integer) countMethod.invoke(dao, entity);
                if (totalCount <= 0) {
                    return new PageData(0, new ArrayList<Common>());
                }
                pager.setTotalNum(totalCount);//设置总记录数，获取总页数
            }
            PageHelper.startPage(pager.getPageIndex(), pager.getPageSize(), false);
            Method listMethod = clazz.getDeclaredMethod("list" + suffix, entity.getClass());
            List<Common> list = (List<Common>) listMethod.invoke(dao, entity);
            return new PageData(pager.getTotalPage(), pager.getTotalNum(), list);
        } catch (Exception ex) {
            logger.error("mybatis分页查询出错:" , ex);
        }
        return new PageData(0, 0, new ArrayList<Common>());*/
        return null;
    }

}
