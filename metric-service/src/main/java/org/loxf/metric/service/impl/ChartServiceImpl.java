package org.loxf.metric.service.impl;


import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.api.IChartService;
import org.loxf.metric.common.dto.Pager;
import org.loxf.metric.common.utils.MapAndBeanTransUtils;
import org.loxf.metric.dal.dao.interfaces.ChartDao;
import org.loxf.metric.service.base.BaseService;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.ChartDto;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.dal.po.Chart;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 图配置业务类
 * Created by caiyang on 2017/5/4.
 */
@Service("chartService")
public class ChartServiceImpl extends BaseService implements IChartService {
    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private ChartDao chartDao;
    @Override
    public BaseResult<String> insertItem(ChartDto obj) {
        BaseResult<String> result=new BaseResult<>();
        if(StringUtils.isEmpty(obj.getChartName())||
                StringUtils.isEmpty(obj.getType())||
                StringUtils.isEmpty(obj.getCreateUserName())||
                StringUtils.isEmpty(obj.getChartDim())||
                obj.getQuotaList().size()==0||obj.getQuotaList()==null){
            //result.setCode();//是否有constant
            result.setMsg("图名称、类型、创建人、维度、指标都不能为空！");
            return result;
        }
        Chart chart=new Chart();
        BeanUtils.copyProperties(obj,chart);
        chart.setCreatedAt(new Date());
        chart.setUpdatedAt(new Date());
        //result.setCode();
        result.setData(chartDao.insert(chart));
        return result;
    }

    @Override
    public PageData getPageList(ChartDto obj) {
        Pager pager=obj.getPager();
        if(pager==null){
            logger.info("分页信息为空，无法查询!");
            return null;
        }
        Map<String, Object> params=MapAndBeanTransUtils.transBean2Map(obj);

        List<Chart>  chartList=chartDao.findByPager(params, pager.getStart(), pager.getRownum());
        PageData pageData=new PageData();
        pageData.setTotalRecords(chartList.size());
        pageData.setRownum(pager.getRownum());
        pageData.setCurrentPage(pager.getCurrentPage());
        pageData.setRows(chartList);
        return pageData;
    }

    @Override
    public BaseResult<ChartDto> queryItemByCode(String itemCode) {
        BaseResult<ChartDto> result=new BaseResult<>();
        if(StringUtils.isEmpty(itemCode)){
            //result.setCode();
            result.setMsg("chartCode不能为空!");
            return result;
        }
        Map<String, Object> qryParams=new HashedMap();
        qryParams.put("chartCode",itemCode);
        Chart chart=chartDao.findOne(qryParams);
        ChartDto chartDto=new ChartDto();
        BeanUtils.copyProperties(chart,chartDto);//前者赋值给后者
        //result.setCode();
        result.setData(chartDto);
        return result;
    }

    @Override
    public BaseResult<String> updateItem(ChartDto obj) {
        BaseResult<String> result=new BaseResult<>();
        String itemCode=obj.getChartCode();
        if(StringUtils.isEmpty(itemCode)){
            //result.setCode();
            result.setMsg("chartCode不能为空！");
            return result;
        }
        Map chartMap= MapAndBeanTransUtils.transBean2Map(obj);
        chartDao.updateOne(itemCode,chartMap);
        //result.setCode()
        return result;
    }

    @Override
    public BaseResult<String> delItemByCode(String itemCode) {
        chartDao.remove(itemCode);
        return new BaseResult<>();
    }

}
