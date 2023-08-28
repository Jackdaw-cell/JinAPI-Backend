package com.jackdawapi.jackdawapisdk;

import com.jackdawapi.jackdawapisdk.client.JackdawApiClient;
import com.jackdawapi.jackdawapisdk.client.JackdawOpenAiClient;
import com.jackdawapi.jackdawapisdk.entity.longApi;
import com.jackdawapi.jackdawapisdk.entity.shortApi;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.function.KeyRandomStrategy;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties({shortApi.class, longApi.class})
@Data
@ComponentScan
public class JackdawApiConfig {

    @Autowired
    private longApi longApi;
    @Autowired
    private shortApi shortApi;
    @Autowired
    private shortApi aliyunMessageApi;

    @Bean
    public JackdawApiClient jackdawApiClient() {
        return new JackdawApiClient(shortApi.getAccessKey(), shortApi.getSecretKey());
    }

    @Bean
    public JackdawOpenAiClient jackdawOpenAiClient() {
        return new JackdawOpenAiClient(longApi.getApiKey(),longApi.getApiHost(),longApi.getProxyHostName(),longApi.getProxyPort());
    }

//    @Bean
//    public OpenAiStreamClient openAiStreamClient() {
//        List<String> userApiKey = new ArrayList<>();
//        userApiKey.add(longApi.getApiHost());
//        //本地开发需要配置代理地址
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(longApi.getProxyHostName(),longApi.getProxyPort()));
//        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
//        //!!!!!!测试或者发布到服务器千万不要配置Level == BODY!!!!
//        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
//        OkHttpClient okHttpClient = new OkHttpClient
//                .Builder()
//                .proxy(proxy)
//                .addInterceptor(httpLoggingInterceptor)
//                .connectTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(600, TimeUnit.SECONDS)
//                .readTimeout(600, TimeUnit.SECONDS)
//                .build();
//        return OpenAiStreamClient
//                .builder()
//                .apiHost(longApi.getApiHost())
//                .apiKey(userApiKey)
//                //自定义key使用策略 默认随机策略
//                .keyStrategy(new KeyRandomStrategy())
//                .okHttpClient(okHttpClient)
//                .build();
//    }

}
