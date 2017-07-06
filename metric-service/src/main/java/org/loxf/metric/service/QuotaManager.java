package org.loxf.metric.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.common.constants.QuotaType;
import org.loxf.metric.common.constants.State;
import org.loxf.metric.common.dto.*;
import org.loxf.metric.dal.dao.interfaces.QuotaDao;
import org.loxf.metric.dal.dao.interfaces.QuotaDimensionDao;
import org.loxf.metric.dal.po.Quota;
import org.loxf.metric.dal.po.QuotaDimension;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by luohj on 2017/5/4.
 */
@Component
public class QuotaManager {
    private static String quota_prefix = "QUOTA";
    @Autowired
    private QuotaDao quotaDao;
    @Autowired
    private QuotaDimensionDao quotaDimensionDao;

    /**
     * @param quotaDto
     * @return
     */
    @Transactional
    public String createQuota(QuotaDto quotaDto){
        Quota quota = new Quota();
        BeanUtils.copyProperties(quotaDto,quota);
        String sid = IdGenerator.generate(quota_prefix);
        quota.setQuotaId(sid);
        quotaDao.insert(quota);
        return sid;
    }
    @Transactional
    public String updateQuota(QuotaDto quotaDto, String quotaId){
        Quota quota = new Quota();
        BeanUtils.copyProperties(quotaDto,quota);
        Map queryMap = new HashMap();
        queryMap.put("quotaId", quotaId);
        quotaDao.update(queryMap, null);
        return quotaId;
    }
    public Quota getQuotaByCode(String code){
        Quota quota = new Quota();
        quota.setQuotaCode(code);
        Quota result = quotaDao.selectQuota(quota);
        return result;
    }
    public Quota getQuotaById(String id){
        Quota quota = new Quota();
        quota.setQuotaId(id);
        Quota result = quotaDao.selectQuota(quota);
        return result;
    }

    public List<Quota> getBasicQuotaList(){
        Quota quota = new Quota();
        quota.setState(State.EFF.getValue());
        quota.setType(QuotaType.BASIC.getValue());
        return quotaDao.selectList(quota);
    }

    public QuotaDataList queryBySql(String sql){
        /*List<Map> list = quotaDao.queryBySql(sql);
        QuotaDataList quotaDataList = new QuotaDataList();
        if(CollectionUtils.isNotEmpty(list)){
            for (Map map : list){
                QuotaData quotaData = new QuotaData();
                Iterator ite = map.keySet().iterator();
                while (ite.hasNext()){
                    String key = (String) ite.next();
                    if(key.equals("value")){
                        quotaData.setValue(new BigDecimal(map.get(key)==null?"0":map.get(key).toString()));
                    } else {
                        quotaData.putDim(key, map.get(key)==null?"":map.get(key).toString());
                    }
                }
                quotaDataList.add(quotaData);
            }
        }
        return quotaDataList;*/
        return null;
    }

    public BaseResult<List<QuotaDto>> queryQuotaNameAndId(QuotaDto quotaDto) {
        /*Quota quota = new Quota();
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
        return  new BaseResult<>(resultList);*/
        return null;
    }

    public List<QuotaDimensionDto> queryDimenListByChartId(String chartId){
        /*List<QuotaDimension> list = quotaMapper.queryDimenListByChartId(chartId);
        List<QuotaDimensionDto> result = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for(QuotaDimension qd:list){
                QuotaDimensionDto dto=new QuotaDimensionDto();
                BeanUtils.copyProperties(qd,dto);
                result.add(dto);
            }
        }
        return result;*/
        return null;
    }

    public List<QuotaDimensionDto> queryDimenListByBoardId(String boardId){
        /*List<QuotaDimension> list = quotaMapper.queryDimenListByBoardId(boardId);
        List<QuotaDimensionDto> result = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for(QuotaDimension qd:list){
                QuotaDimensionDto dto=new QuotaDimensionDto();
                BeanUtils.copyProperties(qd,dto);
                result.add(dto);
            }
        }
        return result;*/
        return null;
    }
    public List<QuotaDimensionDto> queryDimenListByQuotaId(String quotaId){
        /*List<QuotaDimension> list = quotaMapper.queryDimenListByQuotaId(quotaId);
        List<QuotaDimensionDto> result = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for(QuotaDimension qd:list){
                QuotaDimensionDto dto=new QuotaDimensionDto();
                BeanUtils.copyProperties(qd,dto);
                result.add(dto);
            }
        }
        return result;*/
        return null;
    }

    public List<Map> queryDimenListByQuotaCode(String[] quotaCodes){
        /*if(quotaCodes!=null && quotaCodes.length>0) {
            // 去重
            Set set = new  HashSet();
            for(String quotaStr : quotaCodes){
                set.add(quotaStr);
            }
            String [] quotaCodesResult = (String[])set.toArray(new String[set.size()]);
            int count = quotaCodesResult.length;
            List<Map> list = quotaMapper.queryDimenListByQuotaCodes(quotaCodesResult);
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
        }*/
        return null;
    }

    public String selectDimByDimCode(String quotaId, String dimCode){
        /*return quotaDimensionMapper.selectByDimCode(quotaId, dimCode);*/
        return null;
    }

    public PageData<QuotaDto> listQuotaPage(QuotaDto quotaDto) {
        /*Quota newQuota = new Quota();
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
        return new PageData<QuotaDto>(totalPage, count, ret);*/
        return null;

    }
    @Transactional
    public BaseResult<String> delQuota(QuotaDto quotaDto) {
        /*// 查询是否当前指标被依赖，如果是，不能被删除
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
        }*/
        return null;
    }
}
