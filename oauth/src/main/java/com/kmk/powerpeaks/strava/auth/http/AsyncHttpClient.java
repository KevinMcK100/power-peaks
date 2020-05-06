package com.kmk.powerpeaks.strava.auth.http;

import com.kmk.powerpeaks.strava.auth.databind.mapper.AccessTokenResponseMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class AsyncHttpClient {

    private AccessTokenResponseMapper mapper = new AccessTokenResponseMapper();

    public CompletionStage<HttpResponse<String>> executeGetRequest(String url, List<String> headers) {

        HttpRequest request = buildBaseRequest(url, headers).build();

        return HttpClient.newHttpClient()
                         .sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletionStage<HttpResponse<String>> executePostRequest(Object requestObj, String url, List<String> headers) {

        String requestBody = mapper.prettyWriteValueAsString(requestObj);

        HttpRequest request = buildBaseRequest(url, headers)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return HttpClient.newHttpClient()
                         .sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpRequest.Builder buildBaseRequest(String url, List<String> headers) {

        return HttpRequest.newBuilder()
                          .uri(URI.create(url))
                          .timeout(Duration.ofSeconds(10))
                          .headers(headers.toArray(String[]::new));
    }
}
