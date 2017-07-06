package org.loxf.metric.biz.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import org.loxf.metric.biz.base.BaseService;
import org.loxf.metric.client.QuotaScanService;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.common.dto.QuotaScanDto;
import org.loxf.metric.dal.po.QuotaScan;
import org.loxf.metric.service.QuotaScanManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caiyang on 2017/5/4.
 */
@Service("quotaScanService")
public class QuotaScanServiceImpl extends BaseService implements QuotaScanService {
    private static Logger logger = LoggerFactory.getLogger(QuotaScanServiceImpl.class);

    @Autowired
    private QuotaScanManager quotaScanManager;

    @Override
    public BaseResult<String> createQuotaScan(QuotaScanDto quotaScanDto) {
        return quotaScanManager.createQuotaScan(quotaScanDto);
    }

    @Override
    public PageData listQuotaScanPage(QuotaScanDto quotaScanDto) {
        QuotaScan quotaScan=new QuotaScan();
        BeanUtils.copyProperties(quotaScanDto,quotaScan);
        PageData pageUtilsUI=super.pageList(quotaScan,QuotaScanMapper.class,"QuotaScan");
        List<QuotaScan> quotaScanList = pageUtilsUI.getRows();
        List<QuotaScanDto> quotaScanDtoList=new ArrayList<>();
        if(!CollectionUtils.isEmpty(quotaScanList)){
            for(QuotaScan qc:quotaScanList){
                QuotaScanDto dto=new QuotaScanDto();
                BeanUtils.copyProperties(qc, dto);
                quotaScanDtoList.add(dto);
            }
        }
        pageUtilsUI.setRows(quotaScanDtoList);

        return  pageUtilsUI;
    }

    @Override
    public BaseResult<String>  updateQuotaScan(QuotaScanDto quotaScanDto) {
        int i= quotaScanManager.updateQuotaScan(quotaScanDto);
        return new BaseResult<>(i+"");
    }

    @Override
    public BaseResult<String>  delQuotaScanById(String quotaScanId) {
        int i= quotaScanManager.delQuotaScanById(quotaScanId);
        return new BaseResult<>(i+"");
    }

    /**
     * 根据用户ID查询关联列表
     * @param userId
     * @return
     */
    @Override
    public List<QuotaScanDto> listUserQuotaScanRel(String userId, String busiDomain){
        return quotaScanManager.listUserQuotaScanRel(userId, busiDomain);
    }
}
