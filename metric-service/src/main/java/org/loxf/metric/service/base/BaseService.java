package org.loxf.metric.service.base;

import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.loxf.metric.api.IBaseService;
import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.constants.UserTypeEnum;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.core.mongo.IBaseDao;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.UserDao;
import org.loxf.metric.dal.po.BasePO;
import org.loxf.metric.dal.po.Common;
import org.loxf.metric.dal.po.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by caiyang on 2017/3/27.
 */
public class BaseService {
    Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private UserDao userDao;

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

    /**
     * @param userName 经办人
     * @param checkRoot 是否校验ROOT
     * @return
     */
    public BaseResult<String> validHandlerUser(String userName, boolean checkRoot){
        BaseResult<String> result = new BaseResult<>();
        if (StringUtils.isEmpty(userName)) {
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg("经办人不能为空!");
            return result;
        } else {//判断该用户是否存在
            User user = new User();
            user.setUserName(userName);
            User existsUser = userDao.findOne(user);
            if (existsUser == null) {
                result.setCode(ResultCodeEnum.USER_NOT_EXIST.getCode());
                result.setMsg(ResultCodeEnum.USER_NOT_EXIST.getCodeMsg());
                return result;
            } else {
                if(checkRoot) {
                    //判断该用户是否为root用户
                    String type = existsUser.getUserType();
                    if (!(UserTypeEnum.ROOT.equals(type))) {
                        result.setCode(ResultCodeEnum.NO_PERMISSION.getCode());
                        result.setMsg(ResultCodeEnum.NO_PERMISSION.getCodeMsg());
                        return result;
                    }
                }
            }
        }
        return new BaseResult(userName);
    }
}
