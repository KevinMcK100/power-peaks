package com.kmk.powerpeaks.strava.auth.http;

import com.kmk.powerpeaks.strava.auth.model.AccessTokenResponse;
import com.kmk.powerpeaks.strava.auth.model.OAuthRequest;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface AuthHttpClient {

    CompletionStage<Optional<AccessTokenResponse>> getAccessToken(OAuthRequest request);
}
