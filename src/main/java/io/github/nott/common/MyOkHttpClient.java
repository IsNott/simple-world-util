package io.github.nott.common;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.github.nott.factory.MyFactory;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.util.concurrent.TimeUnit;

/**
 * @author Nott
 * @date 2024-8-1
 */

@Slf4j
public class MyOkHttpClient {

    private OkHttpClient okHttpClient;

    public OkHttpClient getOkHttpClient() {
        if(this.okHttpClient == null){
            throw new RuntimeException("OkHttp Not instanced");
        }
        return okHttpClient;
    }

    public static MyOkHttpClient build(){
        MyOkHttpClient client = new MyOkHttpClient();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .callTimeout(3, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build();
        client.okHttpClient = okHttpClient;
        MyFactory.okHttpClient = okHttpClient;
        return client;
    }

    public static <T> JSONObject sendJsonRequest(String url,T req) throws Exception{
        OkHttpClient okHttpClient = MyFactory.okHttpClient;
        String jsonStr = JSONUtil.toJsonStr(req);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);
        log.info("HttpClient post url======>{}",url);
        log.info("HttpClient post json======>{}",jsonStr);
        Request request = new Request.Builder().post(body)
                .url(url).build();
        Response response = okHttpClient.newCall(request).execute();
        String respStr = response.body().string();
        log.info("HttpClient post response======>{}",respStr);
        return JSONUtil.parseObj(respStr);
    }
}
