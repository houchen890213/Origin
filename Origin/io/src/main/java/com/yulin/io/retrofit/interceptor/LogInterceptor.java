package com.yulin.io.retrofit.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by liu_lei on 2017/6/20.
 * 打印请求和返回
 */

public class LogInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Log.d(this.getClass().getSimpleName(), String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));
        if (request.body() != null && request.body().contentLength() > 0) {
            Log.d(this.getClass().getSimpleName(), String.format("Sending body %s", request.body().toString()));
        }

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        Log.d(this.getClass().getSimpleName(), String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        String content = response.body().string();
        Log.d(this.getClass().getSimpleName(), content);

        MediaType mediaType = response.body().contentType();
        return response.newBuilder()
                .body(ResponseBody.create(mediaType, content))
                .build();
    }

}
