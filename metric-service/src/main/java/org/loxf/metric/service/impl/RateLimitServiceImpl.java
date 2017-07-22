package org.loxf.metric.service.impl;

import com.google.common.util.concurrent.RateLimiter;
import org.loxf.metric.api.IRateLimitService;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.RateLimitDto;
import org.loxf.metric.dal.dao.interfaces.RateLimitDao;
import org.loxf.metric.dal.po.RateLimit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hutingting on 2017/7/22.
 */
@Service("rateLimitService")
public class RateLimitServiceImpl implements IRateLimitService {

    @Autowired
    private RateLimitDao rateLimitDao;

    @Override
    public List<RateLimitDto> getRateLimitList() {
        List<RateLimit> rateLimitList=rateLimitDao.getRateLimitList();
        if(rateLimitList!=null&&rateLimitDao.getRateLimitList().size()>0){
            List<RateLimitDto> rateLimitDtoList=new ArrayList<>();
            for(RateLimit rateLimit:rateLimitList){
                RateLimitDto rateLimitDto=new RateLimitDto();
                BeanUtils.copyProperties(rateLimit, rateLimitDto);
                rateLimitDtoList.add(rateLimitDto);
            }
            return  rateLimitDtoList;
        }
        return null;
    }
}
