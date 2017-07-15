package org.loxf.metric.api;

import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.ChartData;
import org.loxf.metric.common.dto.ConditionVo;
import org.loxf.metric.common.dto.QuotaDto;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/7/13.
 */
public interface IDataImportService {
    /**
     * 数据导入接口
     * @param handleUserName
     * @param quotaCode
     * @param data
     * @return
     */
    public BaseResult<QuotaDto> importData(String handleUserName, String quotaCode, List<Map> data);
    /**
     * 按账期删除指标数据接口
     * @param handleUserName
     * @param quotaCode
     * @param circleTime
     * @return
     */
    public BaseResult<QuotaDto> rmDataByCircleTime(String handleUserName, String quotaCode, Date circleTime);
    /**
     * 删除指标全部数据接口
     * @param handleUserName
     * @param quotaCode
     * @return
     */
    public BaseResult<QuotaDto> dropAllData(String handleUserName, String quotaCode);

}
