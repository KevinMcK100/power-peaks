package com.kmk.powerpeaks.strava.api.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.kmk.powerpeaks.strava.auth.guice.AuthModule;
import com.kmk.powerpeaks.strava.auth.service.StravaAuthService;
import io.swagger.client.ApiClient;
import io.swagger.client.Configuration;
import io.swagger.client.api.ActivitiesApi;
import io.swagger.client.api.StreamsApi;
import io.swagger.client.auth.OAuth;

public class StravaApiModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new AuthModule());
    }

    @Provides
    @Singleton
    ApiClient provideApiClient(StravaAuthService stravaAuthService) {
        //TODO: Get athlete ID from config
        String accessToken = stravaAuthService.getValidAuthToken(2402700).get();
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        OAuth stravaOAuth = (OAuth) defaultClient.getAuthentication("strava_oauth");
        stravaOAuth.setAccessToken(accessToken);
        return defaultClient;
    }

    @Provides
    ActivitiesApi provideActivitiesApi(ApiClient apiClient) {
        return new ActivitiesApi(apiClient);
    }

    @Provides
    StreamsApi provideStreamsApi(ApiClient apiClient) {
        return new StreamsApi(apiClient);
    }
}
