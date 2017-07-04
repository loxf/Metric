package org.loxf.metric.biz.base;

import com.github.pagehelper.PageHelper;
import org.apache.log4j.Logger;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.dal.po.Common;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by caiyang on 2017/3/27.
 */
public class BaseService {
    Logger logger = Logger.getLogger(this.getClass());

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
