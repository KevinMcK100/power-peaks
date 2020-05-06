package com.kmk.powerpeaks.strava.auth.service;

import com.google.inject.Inject;
import com.kmk.powerpeaks.strava.auth.http.HttpHelper;
import com.kmk.powerpeaks.strava.auth.config.AuthConfig;
import com.kmk.powerpeaks.strava.auth.dao.AuthDataStoreDao;
import com.kmk.powerpeaks.strava.auth.http.AuthHttpClient;

import java.util.Optional;

public class TestGuice implements AuthService {

    private AuthHttpClient authHttpClient;
    private AuthDataStoreDao authDataStoreDao;
    private AuthConfig authConfig;
    private HttpHelper httpHelper;

    @Inject
    public TestGuice(AuthHttpClient authHttpClient,
                             AuthDataStoreDao authDataStoreDao,
                             AuthConfig authConfig,
                             HttpHelper httpHelper) {
        this.authHttpClient = authHttpClient;
        this.authDataStoreDao = authDataStoreDao;
        this.authConfig = authConfig;
        this.httpHelper = httpHelper;
    }

    @Override
    public Optional<String> getValidAuthToken(long userId) {
        System.out.println(authConfig.toString());
        System.out.println("authHttpClient: " + authHttpClient);
        System.out.println("authDataStoreDao: " + authDataStoreDao);
        System.out.println("awsProfile: " + authDataStoreDao);
        System.out.println("httpHelper: " + httpHelper);
        return Optional.empty();
    }
}
