package cn.cwj.community.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Properties;

@Component
public class BaseSample {
    @Autowired
    private Environment env;
    protected static String accessKeyId ;
    protected static String accessKeySecret ;

    protected static String regionId = "cn-beijing";

    @PostConstruct
    public void initParam () {
        accessKeyId = env.getProperty("oss.accessKeyId");
        accessKeySecret = env.getProperty("oss.accessKeySecret");
    }
/*    static {
        Properties properties = new Properties();

        try {
            properties.load(BaseSample.class.getResourceAsStream("config.properties"));
            accessKeyId = properties.getProperty("accessKeyId");
            accessKeySecret = properties.getProperty("accessKeySecret");
            regionId = properties.getProperty("regionId");
        } catch(IOException e) {
            e.printStackTrace();
        }

    }*/


    protected static String getDomain(){
         if("cn-shanghai".equals(regionId)){
             return "green.cn-shanghai.aliyuncs.com";
         }

         if ("cn-beijing".equals(regionId)) {
            return "green.cn-beijing.aliyuncs.com";
         }

         if ("ap-southeast-1".equals(regionId)) {
            return "green.ap-southeast-1.aliyuncs.com";
         }

         if ("us-west-1".equals(regionId)) {
            return "green.us-west-1.aliyuncs.com";
         }

         return "green.cn-shanghai.aliyuncs.com";
    }

    protected static String getEndPointName(){
        return regionId;
    }

}
