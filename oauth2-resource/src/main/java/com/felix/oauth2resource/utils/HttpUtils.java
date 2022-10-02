package com.felix.oauth2resource.utils;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.collections.MapUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Map;

public class HttpUtils {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");


    @Deprecated
    // TODO remove me
    public static String get(@NotBlank String url) {
        OkHttpClient client = SpringContextUtils.getBean(OkHttpClient.class);
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(@NotNull OkHttpClient client, @NotBlank String url, Map<String, String> headers) {
        Request.Builder reqBuilder = new Request.Builder().url(url);
        if (MapUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> headerKV : headers.entrySet()) {
                reqBuilder.header(headerKV.getKey(), headerKV.getValue());
            }
        }
        try (Response response = client.newCall(reqBuilder.build()).execute()) {
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    // TODO remove me
    public static String get(@NotBlank String url, Map<String, String> headers) {
        OkHttpClient client = SpringContextUtils.getBean(OkHttpClient.class);
        return get(client, url, headers);
    }

    @Deprecated
    // TODO remove me
    public static String post(@NotBlank String url, @NotBlank String json) {
        OkHttpClient client = SpringContextUtils.getBean(OkHttpClient.class);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    // TODO remove me
    public static String post(@NotBlank String url) {
        OkHttpClient client = SpringContextUtils.getBean(OkHttpClient.class);
        RequestBody body = RequestBody.create(JSON, "{}");
        Request request = new Request.Builder().url(url).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    // TODO remove me
    public static String post(@NotBlank String url, @NotBlank String json, Map<String, String> headers) {
        OkHttpClient client = SpringContextUtils.getBean(OkHttpClient.class);
        RequestBody body = RequestBody.create(JSON, json);
        Request.Builder requestBuilder = new Request.Builder().url(url).post(body);
        if (MapUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> headerKV : headers.entrySet()) {
                requestBuilder.header(headerKV.getKey(), headerKV.getValue());
            }
        }
        Request request = requestBuilder.build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
