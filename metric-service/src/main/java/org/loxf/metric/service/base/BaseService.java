package org.loxf.metric.service.base;

import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.core.mongo.IBaseDao;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.po.BasePO;
import org.loxf.metric.dal.po.Common;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by caiyang on 2017/3/27.
 */
public class BaseService {
    Logger logger = Logger.getLogger(this.getClass());
    public PageData getPageResult(Class<? extends Object> daoClazz, Map<String, Object> params,int start,int pageSize) {
        IBaseDao dao = (IBaseDao)SpringApplicationContextUtil.getBean(daoClazz);
        return getPageResult(dao, params, start, pageSize);
    }

    public PageData getPageResult(IBaseDao dao, Map<String, Object> params, int start, int pageSize) {
        try {
            long totalCount = (long) dao.countByParams(params);
            if (totalCount <= 0) {
                return null;
            }
            List pageResult = dao.findByPager(params, start, pageSize);
            PageData pageData=new PageData();
            pageData.setRows(pageResult);
            pageData.setTotalRecords(totalCount);
            pageData.setCurrentPage(start/pageSize+1);
            pageData.setRownum(pageSize);
            return pageData;
        }catch (Exception e){
            logger.error("查询分页异常！",e);
            return null;
        }
    }

    public boolean validHandlerUser(String userName){
        if(StringUtils.isEmpty(userName)){
            return false;
        }
        // TODO 校验用户是否存在
        return true;
    }
}
