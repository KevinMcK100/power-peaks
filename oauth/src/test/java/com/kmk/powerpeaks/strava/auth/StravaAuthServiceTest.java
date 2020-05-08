package com.kmk.powerpeaks.strava.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.kmk.powerpeaks.strava.auth.config.AuthConfig;
import com.kmk.powerpeaks.strava.auth.dao.AuthDataStoreDao;
import com.kmk.powerpeaks.strava.auth.http.AuthHttpClient;
import com.kmk.powerpeaks.strava.auth.http.HttpHelper;
import com.kmk.powerpeaks.strava.auth.model.AccessTokenResponse;
import com.kmk.powerpeaks.strava.auth.model.AuthorisationRequest;
import com.kmk.powerpeaks.strava.auth.model.OAuthData;
import com.kmk.powerpeaks.strava.auth.model.OAuthRequest;
import com.kmk.powerpeaks.strava.auth.model.RefreshTokenRequest;
import com.kmk.powerpeaks.strava.auth.service.StravaAuthService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RunWith(MockitoJUnitRunner.class)
public class StravaAuthServiceTest {

    private static final String ATHLETE_ID = "1";
    private static final long ATHLETE_ID_LONG = 1L;

    private static final String CLIENT_ID_VAL = "12345";
    private static final String CLIENT_SECRET_VAL = "3fs481g2fd31hafl3beu3";
    private static final String AUTH_CODE_VAL = "k6f6ssj914ba681vmuf553f";

    private static final String ACCESS_TOKEN = "hfj93gg820fodd024r";
    private static final String REFRESH_TOKEN = "0lkgd9405v41lm5wlhv1";
    private static final long EXPIRED_EPOCH = 78691238L;
    private static final long FUTURE_EPOCH = Instant.now().plusSeconds(60).getEpochSecond();

    private StravaAuthService target;

    @Mock
    AuthConfig authConfig;

    @Mock
    AuthHttpClient authHttpClient;

    @Mock
    AuthDataStoreDao authDataStoreDao;

    @Mock
    HttpHelper httpHelper;

    @Captor
    ArgumentCaptor<OAuthData> captor;

    @Before
    public void setUp() {

        when(authConfig.getClientId()).thenReturn(Optional.of(CLIENT_ID_VAL));
        when(authConfig.getClientSecret()).thenReturn(Optional.of(CLIENT_SECRET_VAL));
        when(authConfig.getAuthCode()).thenReturn(Optional.of(AUTH_CODE_VAL));

        target = new StravaAuthService(authHttpClient, authDataStoreDao, authConfig, httpHelper);
    }

    @Test
    public void whenTokenDoesNotExistRequestInitialAuthCode() {

        // given
        when(authDataStoreDao.getToken(any())).thenReturn(Optional.empty());
        CompletionStage<Optional<AccessTokenResponse>> expectedResponse =
                CompletableFuture.completedFuture(Optional.of(createRefreshTokenResponse()));
        when(authHttpClient.getAccessToken(any(OAuthRequest.class))).thenReturn(expectedResponse);

        // when
        String accessToken = target.getValidAuthToken(ATHLETE_ID_LONG).orElse("");

        // then
        verify(authHttpClient).getAccessToken(any(AuthorisationRequest.class));
        verify(authDataStoreDao).putToken(captor.capture());
        OAuthData actualOAuthData = captor.getValue();
        OAuthData expectedOAuthData = createOAuthData(FUTURE_EPOCH);
        assertThat(actualOAuthData.getAthleteId()).isEqualTo(expectedOAuthData.getAthleteId());
        assertThat(actualOAuthData.getClientId()).isEqualTo(expectedOAuthData.getClientId());
        assertThat(actualOAuthData.getClientSecret()).isEqualTo(expectedOAuthData.getClientSecret());
        assertThat(actualOAuthData.getAccessToken()).isEqualTo(expectedOAuthData.getAccessToken());
        assertThat(actualOAuthData.getRefreshToken()).isEqualTo(expectedOAuthData.getRefreshToken());
        assertThat(actualOAuthData.getExpiresAt()).isEqualTo(expectedOAuthData.getExpiresAt());
        assertThat(accessToken).isEqualTo(ACCESS_TOKEN);
    }

    @Test
    public void whenTokenExistsButHasExpiredRequestNewToken() {

        // given
        when(authDataStoreDao.getToken(any())).thenReturn(Optional.of(createOAuthData(EXPIRED_EPOCH)));
        CompletionStage<Optional<AccessTokenResponse>> expectedResponse =
                CompletableFuture.completedFuture(Optional.of(createRefreshTokenResponse()));
        when(authHttpClient.getAccessToken(any(OAuthRequest.class))).thenReturn(expectedResponse);

        // when
        String accessToken = target.getValidAuthToken(ATHLETE_ID_LONG).orElse("");

        // then
        verify(authHttpClient).getAccessToken(any(RefreshTokenRequest.class));
        verify(authDataStoreDao).putToken(captor.capture());
        OAuthData actualOAuthData = captor.getValue();
        OAuthData expectedOAuthData = createOAuthData(FUTURE_EPOCH);
        assertThat(actualOAuthData.getAthleteId()).isEqualTo(expectedOAuthData.getAthleteId());
        assertThat(actualOAuthData.getClientId()).isEqualTo(expectedOAuthData.getClientId());
        assertThat(actualOAuthData.getClientSecret()).isEqualTo(expectedOAuthData.getClientSecret());
        assertThat(actualOAuthData.getAccessToken()).isEqualTo(expectedOAuthData.getAccessToken());
        assertThat(actualOAuthData.getRefreshToken()).isEqualTo(expectedOAuthData.getRefreshToken());
        assertThat(actualOAuthData.getExpiresAt()).isEqualTo(expectedOAuthData.getExpiresAt());
        assertThat(accessToken).isEqualTo(ACCESS_TOKEN);
    }

    @Test
    public void whenTokenNotExpiredButPingFailedRefreshToken() {

        // given
        when(httpHelper.ping(any(), any())).thenReturn(false);
        when(authDataStoreDao.getToken(any())).thenReturn(Optional.of(createOAuthData(FUTURE_EPOCH)));
        CompletionStage<Optional<AccessTokenResponse>> expectedResponse =
                CompletableFuture.completedFuture(Optional.of(createRefreshTokenResponse()));
        when(authHttpClient.getAccessToken(any(OAuthRequest.class))).thenReturn(expectedResponse);

        // when
        String accessToken = target.getValidAuthToken(ATHLETE_ID_LONG).orElse("");

        // then
        verify(authHttpClient).getAccessToken(any(RefreshTokenRequest.class));
        verify(authDataStoreDao).putToken(captor.capture());
        OAuthData actualOAuthData = captor.getValue();
        OAuthData expectedOAuthData = createOAuthData(FUTURE_EPOCH);
        assertThat(actualOAuthData.getAthleteId()).isEqualTo(expectedOAuthData.getAthleteId());
        assertThat(actualOAuthData.getClientId()).isEqualTo(expectedOAuthData.getClientId());
        assertThat(actualOAuthData.getClientSecret()).isEqualTo(expectedOAuthData.getClientSecret());
        assertThat(actualOAuthData.getAccessToken()).isEqualTo(expectedOAuthData.getAccessToken());
        assertThat(actualOAuthData.getRefreshToken()).isEqualTo(expectedOAuthData.getRefreshToken());
        assertThat(actualOAuthData.getExpiresAt()).isEqualTo(expectedOAuthData.getExpiresAt());
        assertThat(accessToken).isEqualTo(ACCESS_TOKEN);
    }

    @Test
    public void whenTokenExistsAndValidReturnExistingToken() {

        // given
        when(httpHelper.ping(any(), any())).thenReturn(true);
        when(authDataStoreDao.getToken(any())).thenReturn(Optional.of(createOAuthData(FUTURE_EPOCH)));

        // when
        String accessToken = target.getValidAuthToken(ATHLETE_ID_LONG).orElse("");

        // then
        verifyZeroInteractions(authHttpClient);
        verify(authDataStoreDao, never()).putToken(any());
        assertThat(accessToken).isEqualTo(ACCESS_TOKEN);
    }

    @Test
    public void whenTokenDoesNotExistAndClientIdNotSetThrowException() {

        // given
        when(authDataStoreDao.getToken(any())).thenReturn(Optional.empty());
        CompletionStage<Optional<AccessTokenResponse>> expectedResponse =
                CompletableFuture.completedFuture(Optional.of(createRefreshTokenResponse()));
        when(authConfig.getClientId()).thenReturn(Optional.empty());

        // when
        assertThatCode(() -> target.getValidAuthToken(ATHLETE_ID_LONG))
                // then
                .isInstanceOf(RuntimeException.class).hasMessageContaining("clientId");
        verifyZeroInteractions(authHttpClient);

    }

    private AccessTokenResponse createRefreshTokenResponse() {

        AccessTokenResponse accessTokenResponse = new AccessTokenResponse();
        accessTokenResponse.setAccessToken(ACCESS_TOKEN);
        accessTokenResponse.setRefreshToken(REFRESH_TOKEN);
        accessTokenResponse.setExpiresAt(FUTURE_EPOCH);

        return accessTokenResponse;
    }

    private OAuthData createOAuthData(long expiresAt) {

        OAuthData oAuthData = new OAuthData();
        oAuthData.setAthleteId(ATHLETE_ID);
        oAuthData.setAccessToken(ACCESS_TOKEN);
        oAuthData.setRefreshToken(REFRESH_TOKEN);
        oAuthData.setExpiresAt(expiresAt);
        oAuthData.setClientId(CLIENT_ID_VAL);
        oAuthData.setClientSecret(CLIENT_SECRET_VAL);

        return oAuthData;
    }
}
