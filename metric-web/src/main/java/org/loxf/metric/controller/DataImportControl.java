package org.loxf.metric.controller;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.ParseException;
import io.swagger.annotations.*;
import org.apache.commons.collections.CollectionUtils;
import org.loxf.metric.api.*;
import org.loxf.metric.base.ItemList.QuotaDimItem;
import org.loxf.metric.base.constants.DefaultDimCode;
import org.loxf.metric.base.utils.ExcelUtil;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.dto.*;
import org.loxf.metric.filter.LoginFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by luohj on 2017/7/19.
 */
@Controller
@RequestMapping("/dataImport")
@Api(value = "dataImport", description = "数据导入")
public class DataImportControl {
    private static Logger logger = LoggerFactory.getLogger(DataImportControl.class);
    @Autowired
    private IDataImportService dataImportService;
    @Autowired
    private IQuotaService quotaService;

    /**
     * EXCEL导入
     *
     * @return
     */
    @RequestMapping(value = "/excel", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "EXCEL导入", notes = "EXCEL导入", httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<QuotaDto> excel(@ApiParam(value = "指标CODE", required = true) String quotaCode,
                                      @ApiParam(value = "上传文件", required = true) MultipartFile file,
                                        DefaultMultipartHttpServletRequest request,
                                      HttpServletResponse response) {
        UserDto userDto = LoginFilter.getUser(request);
        BaseResult<QuotaDto> quotaResult = quotaService.queryItemByCode(quotaCode, userDto.getHandleUserName());
        if (quotaResult.getCode() == ResultCodeEnum.SUCCESS.getCode()) {
            try {
                // 校验表头 根据表头 指标和用户获取表头对应的dimCode和dimName的映射关系
                List<String> title = ExcelUtil.loadTitle(file.getInputStream(), file.getOriginalFilename());
                if (CollectionUtils.isNotEmpty(title)) {
                    QuotaDto quotaDto = quotaResult.getData();
                    List<QuotaDimItem> quotaDimItemList = quotaDto.getQuotaDim();
                    Map<String, String> nameMapCode = new HashMap<>();
                    for (String name : title) {
                        for (QuotaDimItem item : quotaDimItemList) {
                            if (name.equals(item.getDimName())) {
                                nameMapCode.put(name, item.getDimCode());
                                break;
                            } else if (name.equals("值")) {
                                nameMapCode.put(name, DefaultDimCode.VALUE);
                                break;
                            } else if (name.equals("账期")) {
                                nameMapCode.put(name, DefaultDimCode.CIRCLE_TIME);
                                break;
                            }
                        }
                    }
                    List<List<String>> data = ExcelUtil.loadData(file.getInputStream(), file.getOriginalFilename());
                    // 解析数据为标准数据格式
                    return dataImportService.importData(userDto.getUserName(), quotaCode,
                            parseStandardData(data, title, nameMapCode));
                }
            } catch (IOException e) {
                logger.error("EXCEL导入失败", e);
                return new BaseResult<>(ResultCodeEnum.ERROR.getCode(), "EXCEL导入失败：" + e.getMessage());
            }
        }
        return quotaResult;
    }

    private List<Map> parseStandardData(List<List<String>> data, List<String> title, Map<String, String> nameMapCode) {
        if (CollectionUtils.isNotEmpty(data)) {
            List<Map> standardData = new ArrayList<>();
            for (List<String> row : data) {
                int inx = 0;
                Map tmp = new HashMap();
                for (String col : row) {
                    String name = title.get(inx);
                    String code = nameMapCode.get(name);
                    tmp.put(code, col);
                }
                standardData.add(tmp);
            }
            return standardData;
        }
        return null;
    }

    /**
     * SDK导入
     *
     * @return
     */
    @RequestMapping(value = "/sdk", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "SDK导入", notes = "SDK导入", httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<QuotaDto> sdk(@RequestParam("quotaCode") @ApiParam(value = "指标CODE", required = true) String quotaCode,
                                    @RequestParam("data") @ApiParam(value = "导入数据", required = true) String data,
                                      HttpServletRequest request, HttpServletResponse response) throws ParseException {
        UserDto userDto = LoginFilter.getUser(request);
        List list = JSON.parse(data, List.class);
        return dataImportService.importData(userDto.getUniqueCode(), quotaCode, list);
    }
}
