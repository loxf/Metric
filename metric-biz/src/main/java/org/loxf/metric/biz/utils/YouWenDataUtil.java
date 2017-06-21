package org.loxf.metric.biz.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 右文接口请求数据工具
 * Created by caiyang on 2017/5/12.
 */
public class YouWenDataUtil {
    private static Logger logger = LoggerFactory.getLogger(YouWenDataUtil.class);

    /**
     * 根据右文渠道ID获取右文渠道名称
     * @param chindId
     * @return
     */
    public static String getChindNameById(String chindId){
        String chindName="";
        String url=  PropertiesUtil.getValue("youwen.channelcount.url");
        StringBuilder pamarm=new StringBuilder(url+"?");
        pamarm.append("chnid="+chindId);
        try {
            String result=  HttpUtil.handleGet(pamarm.toString(), 50);
            JSONObject jsonObject= JSONObject.parseObject(result);
            JSONArray jsonArray= (JSONArray) jsonObject.get("data");
            if(jsonArray.size()>0){
                JSONObject data= (JSONObject) jsonArray.get(0);
                chindName=data.get("name")+"";
            }
        } catch (Exception e) {
            logger.error("右文根据渠道Id获取渠道名称失败。", e);
            return chindId;
        }
        return chindName;
    }

}
