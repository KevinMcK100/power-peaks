package com.kmk.powerpeaks.strava.auth.http;


import com.google.inject.Inject;
import com.kmk.powerpeaks.strava.auth.databind.mapper.AccessTokenResponseMapper;
import com.kmk.powerpeaks.strava.auth.model.AccessTokenResponse;
import com.kmk.powerpeaks.strava.auth.model.OAuthRequest;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

public class StravaAuthHttpClient implements AuthHttpClient {

    AsyncHttpClient httpClient;

    private static final Logger LOGGER = Logger.getLogger(StravaAuthHttpClient.class.getName());

    private static final String STRAVA_OAUTH_REQUEST_URL = "https://www.strava.com/api/v3/oauth/token";

    @Inject
    public StravaAuthHttpClient(AsyncHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public CompletionStage<Optional<AccessTokenResponse>> getAccessToken(OAuthRequest request) {

        AccessTokenResponseMapper mapper = new AccessTokenResponseMapper();
        return httpClient.executePostRequest(request, STRAVA_OAUTH_REQUEST_URL, List.of())
                         .thenApply(HttpResponse::body)
                         .thenApply(mapper::readAccessTokenResponse)
                         .thenApply(Optional::of);
    }
}
