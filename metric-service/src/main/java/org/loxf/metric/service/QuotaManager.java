package org.loxf.metric.service;

import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.common.constants.QuotaType;
import org.loxf.metric.common.constants.State;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.common.dto.QuotaDimensionDto;
import org.loxf.metric.common.dto.QuotaDto;
import org.loxf.metric.dal.dao.QuotaDimensionMapper;
import org.loxf.metric.dal.dao.QuotaMapper;
import org.loxf.metric.dal.po.Quota;
import org.loxf.metric.dal.po.QuotaDimension;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/5/4.
 */
@Component
public class QuotaManager {
    private static String quota_prefix = "Q_";
    @Autowired
    private QuotaMapper quotaMapper;
    @Autowired
    private QuotaDimensionMapper quotaDimensionMapper;

    /**
     * @param quotaDto
     * @param quotas 复合指标对应的计算指标
     * @return
     */
    @Transactional
    public String createQuota(QuotaDto quotaDto, List<String> quotas){
        Quota quota = new Quota();
        BeanUtils.copyProperties(quotaDto,quota);
        String sid = IdGenerator.generate(quota_prefix);
        quota.setQuotaId(sid);
        int succ = quotaMapper.insert(quota);
        if(succ>0) {
            // 插入维度
            // 基础指标
            if (quotaDto.getType().equals(QuotaType.BASIC.getValue()) && quotaDto.getDimensionList() != null) {
                for (QuotaDimensionDto tmp : quotaDto.getDimensionList()) {
                    QuotaDimension po = new QuotaDimension();
                    BeanUtils.copyProperties(tmp, po);
                    po.setQuotaId(sid);
                    quotaDimensionMapper.insert(po);
                }
            } else if (quotaDto.getType().equals(QuotaType.COMPOSITE.getValue())){
                // 复合指标 来源于表达式内其他指标的维度的交集
                List<Map> dimList = queryDimenListByQuotaCode(quotas.toArray(new String [quotas.size()]));
                if(CollectionUtils.isNotEmpty(dimList)) {
                    for (Map dim:dimList) {
                        QuotaDimension po = new QuotaDimension();
                        po.setColumnCode((String) dim.get("columnCode"));
                        po.setColumnName((String) dim.get("columnName"));
                        po.setQuotaId(sid);
                        quotaDimensionMapper.insert(po);
                    }
                }
            }
        }
        return sid;
    }
    @Transactional
    public String updateQuota(QuotaDto quotaDto, List<String> quotas){
        Quota quota = new Quota();
        BeanUtils.copyProperties(quotaDto,quota);
        if(quotaDto.getType().equals(QuotaType.COMPOSITE.getValue()) && CollectionUtils.isNotEmpty(quotas)){
            // 更新维度 先删除 后增加
            quotaDimensionMapper.deleteByQuotaId(quotaDto.getQuotaId());

            List<Map> dimList = queryDimenListByQuotaCode(quotas.toArray(new String [quotas.size()]));
            if(CollectionUtils.isNotEmpty(dimList)) {
                for (Map dim:dimList) {
                    QuotaDimension po = new QuotaDimension();
                    po.setColumnCode((String) dim.get("columnCode"));
                    po.setColumnName((String) dim.get("columnName"));
                    po.setQuotaId(quotaDto.getQuotaId());
                    quotaDimensionMapper.insert(po);
                }
            }
        }
        int sid=  quotaMapper.update(quota);
        if(sid>0){
            return quotaDto.getQuotaId();
        }else{
            return  null;
        }

    }
    public Quota getQuotaByCode(String code){
        Quota quota = new Quota();
        quota.setQuotaCode(code);
        Quota result = quotaMapper.selectQuota(quota);
        if(result!=null){
            List<QuotaDimension> dimensions = quotaDimensionMapper.selectByQuotaId(result.getQuotaId());
            result.setQuotaDimensionList(dimensions);
        }
        return result;
    }
    public Quota getQuotaById(String id){
        Quota quota = new Quota();
        quota.setQuotaId(id);
        Quota result = quotaMapper.selectQuota(quota);
        if(result!=null){
            List<QuotaDimension> dimensions = quotaDimensionMapper.selectByQuotaId(result.getQuotaId());
            result.setQuotaDimensionList(dimensions);
        }
        return result;
    }

    public List<Quota> getBasicQuotaList(){
        Quota quota = new Quota();
        quota.setState(State.EFF.getValue());
        quota.setType(QuotaType.BASIC.getValue());
        return quotaMapper.selectList(quota);
    }

    public List<Map> queryBySql(String sql){
        return quotaMapper.queryBySql(sql);
    }

    public BaseResult<List<QuotaDto>> queryQuotaNameAndId(QuotaDto quotaDto) {
        Quota quota = new Quota();
        BeanUtils.copyProperties(quotaDto,quota);
        List<Quota>quotaList=   quotaMapper.queryQuotaNameAndId(quota);
        List<QuotaDto> resultList=new ArrayList<>();
        if(!CollectionUtils.isEmpty(quotaList)){
            for(Quota qd:quotaList){
                QuotaDto dto=new QuotaDto();
                BeanUtils.copyProperties(qd,dto);
                resultList.add(dto);
            }
        }
        return  new BaseResult<>(resultList);
    }

    public List<QuotaDimensionDto> queryDimenListByChartId(String chartId){
        List<QuotaDimension> list = quotaMapper.queryDimenListByChartId(chartId);
        List<QuotaDimensionDto> result = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for(QuotaDimension qd:list){
                QuotaDimensionDto dto=new QuotaDimensionDto();
                BeanUtils.copyProperties(qd,dto);
                result.add(dto);
            }
        }
        return result;
    }

    public List<QuotaDimensionDto> queryDimenListByBoardId(String boardId){
        List<QuotaDimension> list = quotaMapper.queryDimenListByBoardId(boardId);
        List<QuotaDimensionDto> result = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for(QuotaDimension qd:list){
                QuotaDimensionDto dto=new QuotaDimensionDto();
                BeanUtils.copyProperties(qd,dto);
                result.add(dto);
            }
        }
        return result;
    }
    public List<QuotaDimensionDto> queryDimenListByQuotaId(String quotaId){
        List<QuotaDimension> list = quotaMapper.queryDimenListByQuotaId(quotaId);
        List<QuotaDimensionDto> result = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for(QuotaDimension qd:list){
                QuotaDimensionDto dto=new QuotaDimensionDto();
                BeanUtils.copyProperties(qd,dto);
                result.add(dto);
            }
        }
        return result;
    }

    public List<Map> queryDimenListByQuotaCode(String[] quotaCodes){
        if(quotaCodes!=null && quotaCodes.length>0) {
            int count = quotaCodes.length;
            List<Map> list = quotaMapper.queryDimenListByQuotaCodes(quotaCodes);
            List<Map> result = new ArrayList<>();
            List<String> existsColumn = new ArrayList<>();
            Map<String, Integer> countMap = new HashMap();
            if(CollectionUtils.isNotEmpty(list)) {
                for (Map tmp : list) {
                    String columnCode = (String)tmp.get("columnCode");
                    if(countMap.containsKey(columnCode)){
                        Integer i = countMap.get(columnCode);
                        countMap.put(columnCode, ++i);
                    } else {
                        countMap.put(columnCode, 1);
                    }
                }
                for (Map tmp : list) {
                    String columnCode = (String)tmp.get("columnCode");
                    if(!existsColumn.contains(columnCode) && countMap.get(columnCode).intValue()==count){
                        existsColumn.add(columnCode);
                        result.add(tmp);
                    }
                }
            }
            return result;
        } else {
            throw new MetricException("查询条件不能为空");
        }
    }

    public String selectDimByDimCode(String quotaId, String dimCode){
        return quotaDimensionMapper.selectByDimCode(quotaId, dimCode);
    }

    public PageData<QuotaDto> listQuotaPage(QuotaDto quotaDto) {
        Quota newQuota = new Quota();
        BeanUtils.copyProperties(quotaDto, newQuota);
        int count = quotaMapper.count(newQuota);
        int totalPage = count/newQuota.getRow() + (count%newQuota.getRow()==0?0:1);
        List<Quota> list = quotaMapper.selectList(newQuota);
        List<QuotaDto> ret = new ArrayList<>();
        for(Quota tmp : list){
            QuotaDto dto = new QuotaDto();
            BeanUtils.copyProperties(tmp, dto);
            ret.add(dto);
        }
        return new PageData<QuotaDto>(totalPage, count, ret);
    }
    @Transactional
    public BaseResult<String> delQuota(QuotaDto quotaDto) {
        // 查询是否当前指标被依赖，如果是，不能被删除
        Quota quota = new Quota();
        quota.setQuotaId(quotaDto.getQuotaId());
        Quota quotaAga = quotaMapper.selectQuota(quota);
        List<Quota> dependencyQuotas = quotaMapper.queryQuotaDependency("${" + quotaAga.getQuotaCode() + "}");
        boolean flag = false;// 是否依赖， 默认未依赖
        String dependencyQuotaName = "";
        if(CollectionUtils.isNotEmpty(dependencyQuotas)){
            for(Quota tmp : dependencyQuotas){
                if(!tmp.getQuotaId().equals(quotaAga.getQuotaId())){
                    if(StringUtils.isNotEmpty(dependencyQuotaName)){
                        dependencyQuotaName = dependencyQuotaName + "," +  quotaAga.getQuotaDisplayName();
                    } else {
                        dependencyQuotaName = quotaAga.getQuotaDisplayName();
                    }
                    flag = true;
                }
            }
        }
        if(!flag) {
            // 删除维度
            quotaDimensionMapper.deleteByQuotaId(quotaDto.getQuotaId());
            // 删除指标
            int i = quotaMapper.deleteByQuotaId(quotaDto.getQuotaId());
            if (i > 0) {
                return new BaseResult<>(1, "删除成功");
            } else {
                return new BaseResult<>(0, "删除失败");
            }
        } else {
            return new BaseResult<>(0, "删除失败，当前指标被以下指标依赖：" + dependencyQuotaName);
        }
    }
}
