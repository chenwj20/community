package cn.cwj.community.provider;

import cn.cwj.community.dto.QQAccessTokenDTO;
import cn.cwj.community.dto.QQUserDTO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Date 2020/2/13
 * @Version V1.0
 **/
@Component
public class QQProvider {
    public String getAccessToken(QQAccessTokenDTO qqAccessTokenDTO) {
        MediaType mediaType = MediaType.get("application/x-www-form-urlencoded; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String s = "grant_type="+qqAccessTokenDTO.getGrant_type()+"&code="+qqAccessTokenDTO.getCode()+"&client_id="+qqAccessTokenDTO.getClient_id()+"&client_secret="+qqAccessTokenDTO.getClient_secret()+"&redirect_uri="+qqAccessTokenDTO.getRedirect_uri();
        RequestBody body = RequestBody.create(mediaType, s);
        Request request = new Request.Builder()
                .url("https://graph.qq.com/oauth2.0/token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            System.out.println(string);
            return string;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getOpenID(String accessToken) {


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://graph.qq.com/oauth2.0/me?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            String jsonString = string.split(" ")[1].split(" ")[0];;
            System.out.println(jsonString);
            JSONObject obj = JSONObject.parseObject(jsonString);
            String openid = obj.getString("openid");
            System.out.println(openid);
            return openid;
        } catch (IOException e) {
        }
        return null;

    }

    public QQUserDTO getUser(String accessToken, String oauth_consumer_key, String openid) {


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://graph.qq.com/user/get_user_info?access_token=" + accessToken+"&oauth_consumer_key="+oauth_consumer_key +"&openid="+openid)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            System.out.println(string);
            QQUserDTO qqUserDTO = JSON.parseObject(string, QQUserDTO.class);
            qqUserDTO.setOpenId(openid);
            return qqUserDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
