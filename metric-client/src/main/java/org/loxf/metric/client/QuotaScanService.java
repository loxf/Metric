package org.loxf.metric.client;


import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.common.dto.QuotaScanDto;

import java.util.List;

public interface QuotaScanService {
    /**
     * 创建展示指标
     * @param quotaScanDto
     * @return
     */
    public BaseResult<String> createQuotaScan(QuotaScanDto quotaScanDto);

    /**
     * 展示指标列表支持分页
     * @param quotaScanDto
     * @return
     */
    public PageData listQuotaScanPage(QuotaScanDto quotaScanDto);

    /**
     * 修改展示指标
     * @param quotaScanDto
     * @return
     */
    public BaseResult<String>  updateQuotaScan(QuotaScanDto quotaScanDto);

    /**
     * 删除展示指标
      * @param quotaScanId
     * @return
     */
    public  BaseResult<String>  delQuotaScanById(String quotaScanId);

    /**
     * 根据用户ID查询关联列表
     * @param userId
     * @return
     */
    public List<QuotaScanDto> listUserQuotaScanRel(String userId, String busiDomain);
}