package com.kmk.powerpeaks.strava.auth.http;

import com.google.inject.Inject;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class HttpHelper {

    private AsyncHttpClient httpClient;

    @Inject
    public HttpHelper(AsyncHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public boolean ping(String url, List<String> headers) {

        CompletionStage<HttpResponse<String>> responseStage = httpClient.executeGetRequest(url, headers);
        HttpResponse<String> response = responseStage.toCompletableFuture().join();

        return response.statusCode() < 300;
    }
}
