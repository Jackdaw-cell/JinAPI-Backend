package com.jackdawapi.jackdawapisdk.Interceptor;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class NetInterceptor implements Interceptor {
    private static final String TAG = "LoggingInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
    
        Request request = chain.request();
        Response response = chain.proceed(request);
        //演示设置响应头
        CacheControl.Builder builder = new CacheControl.Builder()
        .maxAge(10, TimeUnit.MINUTES);
        return response.newBuilder()
                .header("Cache-Control",builder.build().toString())
                 .build();
    }
}
