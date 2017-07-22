package org.loxf.metric.utils;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.log4j.Logger;
import org.loxf.metric.utils.AcsClientSingleton;

/**
 * Created by hutingting on 2017/7/20.
 */
public class SendMsgUtils {
   static Logger logger = Logger.getLogger("SendMsgUtils".getClass());

    static  final RateLimiter rateLimiter = RateLimiter.create(20);//从数据库读取或者页面读取
    public static void sendMsg(String phoneNumbers,String signName,String templateCode,String templateParam,String outId){
        rateLimiter.acquire();
        SendSmsRequest request=getSmsRequestBody("18902212310","LOXF指标网站","SMS_78615007","{\"code\":\"123\"}","yourOutId");
        try{
            SendSmsResponse sendSmsResponse = AcsClientSingleton.getInstance().getAcsClient().getAcsResponse(request);
            if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {//请求成功

            }
        }catch (Exception e){//请求失败这里会抛ClientException异常
            logger.error("短信发送异常:",e);
        }


    }

    private static SendSmsRequest getSmsRequestBody(String phoneNumbers,String signName,String templateCode,String templateParam,String outId){
        //组装请求对象

        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为20个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
        request.setPhoneNumbers(phoneNumbers);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        //尊敬的用户，欢迎注册骑驴指标。短信验证码：${code}。
        request.setTemplateParam(templateParam);

        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"123\"}");
        //可选-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId(outId);
        return request;
    }
}
