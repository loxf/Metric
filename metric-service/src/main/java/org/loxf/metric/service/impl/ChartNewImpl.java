//package org.loxf.metric.biz.impl;
//
//import org.loxf.metric.client.IChartService;
//import org.loxf.metric.common.dto.BaseResult;
//import org.loxf.metric.common.dto.ChartDto;
//import org.loxf.metric.common.dto.PageData;
//import org.loxf.metric.dal.dao.ChartMapper;
//import org.loxf.metric.dal.dao.interfaces.ChartDao;
//import org.loxf.metric.dal.po.Chart;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.util.CollectionUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by hutingting on 2017/7/6.
// */
//@Service("chartService")
//public class ChartNewImpl implements IChartService {
//    @Autowired
//    private ChartDao chartDao;
//    @Override
//    public PageData listChartPage(ChartDto chartDto) {
//        Chart chart=new Chart();
//        BeanUtils.copyProperties(chartDto,chart);
//        PageData pageUtilsUI=super.pageList(chart,ChartMapper.class,"Chart");
//        List<Chart> chartList=    pageUtilsUI.getRows();
//        List<ChartDto> chartDtoList=new ArrayList<>();
//        if(!CollectionUtils.isEmpty(chartList)){
//            for(Chart c:chartList){
//                ChartDto dto=new ChartDto();
//                BeanUtils.copyProperties(c, dto);
//                chartDtoList.add(dto);
//            }
//        }
//        pageUtilsUI.setRows(chartDtoList);
//        return  pageUtilsUI;
//    }
//
//    @Override
//    public BaseResult<String> createChart(ChartDto chartDto) {
//        return null;
//    }
//
//    @Override
//    public BaseResult<ChartDto> queryByChartId(String chartId) {
//        return null;
//    }
//
//    @Override
//    public BaseResult<String> updateChart(ChartDto boardDto) {
//        return null;
//    }
//
//    @Override
//    public BaseResult<String> delChart(String chartId) {
//        return null;
//    }
//}
