package org.loxf.metric.service;

import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.QuotaScanDto;
import org.loxf.metric.dal.dao.QuotaScanMapper;
import org.loxf.metric.dal.po.QuotaScan;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by caiyang on 2017/5/4.
 */
@Component
public class QuotaScanManager {
    private static String quotascan_prefix = "QS_";

    @Autowired
    private QuotaScanMapper quotaScanMapper;

    /**
     * 创建展示指标
     * @param quotaScanDto
     * @return
     */
    @Transactional
    public BaseResult<String> createQuotaScan(QuotaScanDto quotaScanDto){
        QuotaScan qs=new QuotaScan();
        String sid = IdGenerator.generate(quotascan_prefix);
        BeanUtils.copyProperties(quotaScanDto,qs);
        qs.setScanId(sid);
        qs.setCreatedAt(new Date());
        qs.setUpdatedAt(new Date());
        int i= quotaScanMapper.insert(qs);
        if(i>0){
            return  new BaseResult<>(sid);
        }else{
            return  new BaseResult<>("创建指标关联失败！");
        }
    }

    /**
     * 修改展示指标
     * @param quotaScanDto
     * @return
     */
    @Transactional
    public int updateQuotaScan(QuotaScanDto quotaScanDto){
        QuotaScan qs=new QuotaScan();
        BeanUtils.copyProperties(quotaScanDto,qs);
        return quotaScanMapper.updateByQuotaScanId(qs);
    }

    /**
     * 删除展示指标
     * @param quotaScanId
     * @return
     */
    @Transactional
    public  int delQuotaScanById(String quotaScanId){
        return  quotaScanMapper.deleteByQuotaScanId(quotaScanId);
    }

    public List<QuotaScanDto> listUserQuotaScanRel(String userId, String busiDomain){
        List<QuotaScan> quotaScanList = quotaScanMapper.listUserQuotaScanRel(userId, busiDomain);
        List<QuotaScanDto> result = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(quotaScanList)){
            for(QuotaScan quotaScan : quotaScanList){
                QuotaScanDto dto = new QuotaScanDto();
                BeanUtils.copyProperties(quotaScan, dto);
                result.add(dto);
            }
        }
        return result;
    }
}
