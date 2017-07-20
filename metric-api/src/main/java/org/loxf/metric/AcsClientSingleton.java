package org.loxf.metric;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * Created by hutingting on 2017/7/20.
 */
public class AcsClientSingleton {
    private volatile static IAcsClient acsClient;
    private AcsClientSingleton (){}
    public static IAcsClient getSingleton() {
        if (acsClient == null) {
            synchronized (IAcsClient.class) {
                if (acsClient == null) {
                    acsClient = getAcsClient();
                }
            }
        }
        return acsClient;
    }


    private static  IAcsClient getAcsClient(){
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
        try{
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        }catch (ClientException e){
            System.out.println(e);
        }
        acsClient = new DefaultAcsClient(profile);
        return  acsClient;
    }
}
