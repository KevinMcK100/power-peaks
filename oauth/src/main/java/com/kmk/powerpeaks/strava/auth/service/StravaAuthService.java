package com.kmk.powerpeaks.strava.auth.service;

import com.google.inject.Inject;
import com.kmk.powerpeaks.strava.auth.dao.AuthDataStoreDao;
import com.kmk.powerpeaks.strava.auth.http.HttpHelper;
import com.kmk.powerpeaks.strava.auth.model.AccessTokenResponse;
import com.kmk.powerpeaks.strava.auth.model.AuthorisationRequest;
import com.kmk.powerpeaks.strava.auth.model.OAuthData;
import com.kmk.powerpeaks.strava.auth.model.OAuthRequest;
import com.kmk.powerpeaks.strava.auth.model.RefreshTokenRequest;
import com.kmk.powerpeaks.strava.auth.config.AuthConfig;
import com.kmk.powerpeaks.strava.auth.http.AuthHttpClient;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StravaAuthService implements AuthService {

    private AuthHttpClient authHttpClient;
    private AuthDataStoreDao authDataStoreDao;
    private AuthConfig authConfig;
    private HttpHelper httpHelper;

    private static final Logger LOGGER = Logger.getLogger(StravaAuthService.class.getName());
    private static final String MISSING_REQUIRED_CONFIG_ERROR =
            "%s must be set passed as config in order to fetch OAuth Token for athlete %s.";
    private static final String CLIENT_ID = "clientId";
    private static final String CLIENT_SECRET = "clientSecret";
    private static final String AUTH_CODE = "authCode";
    private static final String STRAVA_ATHLETE_REQUEST_URL = "https://www.strava.com/api/v3/athlete";

    @Inject
    public StravaAuthService(AuthHttpClient authHttpClient,
                             AuthDataStoreDao authDataStoreDao,
                             AuthConfig authConfig,
                             HttpHelper httpHelper) {
        this.authHttpClient = authHttpClient;
        this.authDataStoreDao = authDataStoreDao;
        this.authConfig = authConfig;
        this.httpHelper = httpHelper;
    }

    @Override
    public Optional<String> getValidAuthToken(long athleteId) {

        // Pull data from DB for this athlete
        Optional<OAuthData> oAuthDataOpt = authDataStoreDao.getToken(String.valueOf(athleteId));

        if (oAuthDataOpt.isEmpty()) {
            LOGGER.info("No OAuth data saved for athlete: " + athleteId);

            AuthorisationRequest authorisationRequest = buildAuthorisationRequest(athleteId);

            return getRefreshToken(athleteId, authorisationRequest);

        } else {
            LOGGER.info("OAuth data exists for athlete: " + athleteId);
            OAuthData existingOAuthData = oAuthDataOpt.get();
            LOGGER.info("Existing OAuth data: " + existingOAuthData);

            // Fetch new access token if it's expired
            if (existingOAuthData.getExpiresAt() <= Instant.now().getEpochSecond() ||
                    pingFails(existingOAuthData.getAccessToken())) {
                LOGGER.info("OAuth data expired, calling Strava Auth API for new access token");
                RefreshTokenRequest refreshTokenRequest = buildRefreshTokenRequest(existingOAuthData);
                LOGGER.info("RefreshTokenRequest: " + refreshTokenRequest);

                return getRefreshToken(athleteId, refreshTokenRequest);

            } else {
                LOGGER.info("Existing access is still valid.");
                return Optional.of(existingOAuthData.getAccessToken());
            }
        }
    }

    private AuthorisationRequest buildAuthorisationRequest(long athleteId) {

        // Throw exception with error if config has not been set
        AuthorisationRequest request = new AuthorisationRequest();

        String clientId = authConfig.getClientId().orElseThrow(() -> new RuntimeException(
                String.format(MISSING_REQUIRED_CONFIG_ERROR, CLIENT_ID, athleteId)));
        String clientSecret = authConfig.getClientSecret().orElseThrow(() -> new RuntimeException(
                String.format(MISSING_REQUIRED_CONFIG_ERROR, CLIENT_SECRET, athleteId)));
        String authCode = authConfig.getClientSecret().orElseThrow(() -> new RuntimeException(
                String.format(MISSING_REQUIRED_CONFIG_ERROR, AUTH_CODE, athleteId)));

        request.setClientId(Long.parseLong(clientId));
        request.setClientSecret(clientSecret);
        request.setCode(authCode);

        LOGGER.info("AuthorisationRequest: " + request.toString());

        return request;
    }

    private RefreshTokenRequest buildRefreshTokenRequest(OAuthData existingOAuthData) {

        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setClientId(Long.parseLong(existingOAuthData.getClientId()));
        refreshTokenRequest.setClientSecret(existingOAuthData.getClientSecret());
        refreshTokenRequest.setRefreshToken(existingOAuthData.getRefreshToken());

        return refreshTokenRequest;
    }

    private Optional<String> getRefreshToken(long athleteId, OAuthRequest oAuthRequest) {

        return authHttpClient.getAccessToken(oAuthRequest).exceptionally(throwable -> {
            LOGGER.log(Level.SEVERE, "Failed to retrieve access token from Strava OAuth API");
            return Optional.empty();
        }).thenCompose(responseOpt -> responseOpt
                .map(response -> saveOAuthData(athleteId,
                                               response,
                                               oAuthRequest.getClientId(),
                                               oAuthRequest.getClientSecret()))
                .orElseThrow(() -> new RuntimeException("AccessTokenResponse is empty"))
        ).toCompletableFuture().join();
    }

    private CompletionStage<Optional<String>> saveOAuthData(long athleteId,
                                    AccessTokenResponse accessTokenResponse,
                                    long clientId,
                                    String clientSecret) {

        OAuthData oAuthData = new OAuthData();
        oAuthData.setAthleteId(String.valueOf(athleteId));
        oAuthData.setAccessToken(accessTokenResponse.getAccessToken());
        oAuthData.setRefreshToken(accessTokenResponse.getRefreshToken());
        oAuthData.setExpiresAt(accessTokenResponse.getExpiresAt());
        oAuthData.setClientId(String.valueOf(clientId));
        oAuthData.setClientSecret(clientSecret);

        LOGGER.info("Saving OAuth data: " + oAuthData.toString());
        return CompletableFuture.supplyAsync(() -> {
            authDataStoreDao.putToken(oAuthData);
            return Optional.of(oAuthData.getAccessToken());
        });
    }

    private boolean pingFails(String existingAccessToken) {

        List<String> headers = Arrays.asList("Authorization", "Bearer " + existingAccessToken);
        boolean isFailed = !httpHelper.ping(STRAVA_ATHLETE_REQUEST_URL, headers);
        LOGGER.info("Ping to athlete endpoint failed: " + isFailed);
        return isFailed;
    }
}
