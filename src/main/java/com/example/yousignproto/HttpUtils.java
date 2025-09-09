package com.example.yousignproto;

import com.example.yousignproto.model.SignatureRequest;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class HttpUtils {

    @Value("${yousign.apiKey}")
    private String apiKey;
    @Value("${yousign.baseUrl}")
    private String baseUrl;

    private final OkHttpClient client;

    public HttpUtils() {
        client = new OkHttpClient();
    }

    public Response jsonPostRequest(String url, String jsonBody) throws IOException {
        System.out.println(jsonBody);
        MediaType jsonMediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(jsonBody, jsonMediaType);
        Request createSignatureRequest = new Request.Builder()
                .url(baseUrl + url)
                .post(requestBody)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        return client.newCall(createSignatureRequest).execute();
    }

    public Response multipartPostRequest(String url, File fileToSign) throws IOException {
        RequestBody fileBody = RequestBody.create(fileToSign, MediaType.parse("application/pdf"));
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileToSign.getName(), fileBody)
                .addFormDataPart("nature", "signable_document")
                .build();

        Request addDocumentRequest = new Request.Builder()
                .url(baseUrl + url)
                .post(multipartBody)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        return client.newCall(addDocumentRequest).execute();
    }
}
