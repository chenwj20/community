package cn.cwj.community.provider;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.util.UUID;

/**
 * @Date 2020/2/3
 * @Version V1.0
 **/
@Component
public class OssUploadImgProvider {
    @Value("${oss.endpoint}")
    private  String endpoint;

    @Value("${oss.accessKeyId}")
    private  String accessKeyId;

    @Value("${oss.accessKeySecret}")
    private  String accessKeySecret;

    @Value("${oss.bucketName}")
    private  String bucketName;



    public  String UploadFile(InputStream inputStream, String fileType, String fileName,String key) throws IOException {
        /*
         * Constructs a client instance with your account for accessing OSS
         */
        OSS client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            /*
             * Upload an object to your bucket from an input stream
             */

            key+= UUID.randomUUID().toString()+fileName;

            client.putObject(bucketName, key, inputStream);
            // 设置URL过期时间为3年 3600l* 1000*24*365*10

            Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 3);
            // 生成URL
            URL url = client.generatePresignedUrl(bucketName, key, expiration);
            if (url != null) {
                return url.toString();
            }
            return  url.toString();
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorCode());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
            return null;
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce.getMessage());
            return null;
        } finally {
            /*
             * Do not forget to shut down the client finally to release all allocated resources.
             */
            client.shutdown();
        }
    }

}
