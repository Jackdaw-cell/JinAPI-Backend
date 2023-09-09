package com.jackdawapi.jackdawapisdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import com.jackdawapi.jackdawapisdk.common.BaseResponse;
import com.jackdawapi.jackdawapisdk.common.ResultUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import static com.jackdawapi.jackdawapisdk.utils.SignUtils.genSign;

/**
 * 调用第三方接口的客户端
 *
 * @author jackdaw
 */
public class JackdawApiClient {

//    private static final String GATEWAY_HOST = "http://111.230.23.40:8122";
    private static final String GATEWAY_HOST = "http://127.0.0.1:8122";

    private String accessKey;

    private String secretKey;

    public JackdawApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("body", body);
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", genSign(getUTF8ToString(body), secretKey));
        hashMap.put("Accept","*/*");
        hashMap.put("Content-Type", "application/json");
        return hashMap;
    }

    private Map<String, String> getHeaderMap() {
        String body = "";
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("body", body);
        hashMap.put("accessKey", accessKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", genSign(getUTF8ToString(body), secretKey));
        hashMap.put("Accept","*/*");
        hashMap.put("Content-Type", "application/json");
        return hashMap;
    }

    private String getUTF8String(String str) {
        try{
            str = URLEncoder.encode(str, "UTF-8");
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return str;
    }

    private String getUTF8ToString(String str) {
        try{
            str = URLDecoder.decode(str,"UTF-8");
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return str;
    }

    /**
     * Post请求
     * @param bodyJson
     * @return
     */
    public BaseResponse<Object> postRequest(String bodyJson,String url) {
        // 请求头传递过程会出现乱码问题，而请求体传到控制器时会自动序列化为对象，不需要处理乱码
        // 因此，请求头的body要进行utf8编码，请求体不需要进行编码
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + url)
                .addHeaders(getHeaderMap(getUTF8String(bodyJson)))
                .charset(StandardCharsets.UTF_8)
                .body(bodyJson)
                .execute();
        String result = httpResponse.body();
        return ResultUtils.success(result);
    }

    /**
     * Get请求
     * @param bodyJson
     * @return
     */
    public BaseResponse<Object> getRequest(String bodyJson, String url) {
        // 将JSON字符串转换为JSONObject
        JSONObject jsonObject = new JSONObject(bodyJson);
        // 将JSONObject转换为HashMap
        HashMap<String, Object> hashMap = new HashMap<>();
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            hashMap.put(key, value);
        }
        String result = HttpRequest.get(GATEWAY_HOST + url)
                .addHeaders(getHeaderMap(getUTF8String(bodyJson)))
                .form((hashMap))
                .charset(StandardCharsets.UTF_8)
                .execute()
                .body();
        return ResultUtils.success(result);
         
    }


}
