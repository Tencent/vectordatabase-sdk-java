package com.tencentcloudapi.client;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * HttpClient
 * User: wlleiiwang
 * Date: 2023/7/24
 */
public class HttpClient {
    private OkHttpClient client;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public HttpClient() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))
                .build();
    }

    public Response get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response;
        }
    }

    public Response post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response;
        }
    }
}
