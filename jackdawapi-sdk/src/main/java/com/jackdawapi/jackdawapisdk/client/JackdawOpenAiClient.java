package com.jackdawapi.jackdawapisdk.client;

import cn.hutool.core.util.StrUtil;
import com.jackdawapi.jackdawapicommon.model.entity.OpenAI.ChatRequest;
import com.jackdawapi.jackdawapicommon.model.entity.OpenAI.ChatResponse;
import com.jackdawapi.jackdawapisdk.service.SseService;
import com.jackdawapi.jackdawapisdk.service.impl.SseServiceImpl;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import com.unfbx.chatgpt.function.KeyRandomStrategy;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Jackdaw
 */
public class JackdawOpenAiClient {

    private SseService sseService;

//    private List<String> apiKey = new ArrayList<>();

    public JackdawOpenAiClient(String userApiKey, String apiHost,String proxyHostName,Integer proxyPort) {
        List<String> apiKey = new ArrayList<>();
        apiKey.add(userApiKey);
        this.sseService = new SseServiceImpl(openAiStreamClient(apiKey, apiHost, proxyHostName, proxyPort));
    }


    public OpenAiStreamClient openAiStreamClient(List<String> apiKey, String apiHost,String proxyHostName,Integer proxyPort) {
        //本地开发需要配置代理地址
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHostName, proxyPort));
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        //!!!!!!测试或者发布到服务器千万不要配置Level == BODY!!!!
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .proxy(proxy)
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(600, TimeUnit.SECONDS)
                .readTimeout(600, TimeUnit.SECONDS)
                .build();
        return OpenAiStreamClient
                .builder()
                .apiHost(apiHost)
                .apiKey(apiKey)
                //自定义key使用策略 默认随机策略
                .keyStrategy(new KeyRandomStrategy())
                .okHttpClient(okHttpClient)
                .build();
    }

    //创建连接-多用户使用
    public SseEmitter createConnect(String uid) {
        return sseService.createSse(uid);
    }

    //创建连接-单用户使用
    public SseEmitter createConnect() {
        String uid = "1";
        return sseService.createSse(uid);
    }
    //对话-多用户使用
    public ChatResponse sseChat(String uid, ChatRequest chatRequest) {
        return sseService.sseChat(uid, chatRequest);
    }
    //对话-单用户使用
    public ChatResponse sseChat( ChatRequest chatRequest) {
        String uid = "1";
        return sseService.sseChat(uid, chatRequest);
    }

    public void closeConnect(@RequestHeader Map<String, String> headers) {
        String uid = getUid(headers);
        sseService.closeSse(uid);
    }

    private String getUid(Map<String, String> headers) {
        String uid = headers.get("uid");
        if (StrUtil.isBlank(uid)) {
            throw new BaseException(CommonError.SYS_ERROR);
        }
        return uid;
    }
}
