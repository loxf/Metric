package org.loxf.metric.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hutingting on 2017/7/20.
 */
public class AcsClientSingleton {
    private static Logger logger = LoggerFactory.getLogger(AcsClientSingleton.class);
    private static AcsClientSingleton singleton;
    private IAcsClient acsClient;

    private AcsClientSingleton() {
        init();
    }

    public static AcsClientSingleton getInstance() {
        if (singleton == null) {
            synchronized (AcsClientSingleton.class) {
                if (singleton == null) {
                    singleton = new AcsClientSingleton();
                }
            }
        }
        return singleton;
    }

    private void init(){
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient需要的几个参数
        final String product = "Dysmsapi";//短信API产品名称
        final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名
        //替换成你的AK
        final String accessKeyId = "KxjoVkg0QwtxT6Zt";//你的accessKeyId,参考本文档步骤2
        final String accessKeySecret = "85LPVHLXixPJ8ZaoveC0bCUZxxblb5";//你的accessKeySecret，参考本文档步骤2
        //初始化ascClient,暂时不支持多region
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e) {
            logger.error("", e);
        }
        acsClient = new DefaultAcsClient(profile);
    }

    protected IAcsClient getAcsClient() {
        return acsClient;
    }
}
