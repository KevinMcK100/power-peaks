package com.kmk.powerpeaks.strava.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.kmk.powerpeaks.strava.auth.http.AsyncHttpClient;
import com.kmk.powerpeaks.strava.auth.http.HttpHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RunWith(MockitoJUnitRunner.class)
public class HttpHelperTest {

    private HttpHelper target;

    @Mock
    private AsyncHttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    private static final String URL = "www.fake.co.uk";
    private static final List<String> HEADERS = List.of();

    @Before
    public void setUp() {
        target = new HttpHelper(httpClient);
    }

    @Test
    public void whenResponseCode200ReturnTrue() {

        // given
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpClient.executeGetRequest(any(), any())).thenReturn(CompletableFuture.completedFuture(httpResponse));

        // when
        boolean result = target.ping(URL, HEADERS);

        //then
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void whenResponseCode201ReturnTrue() {

        // given
        when(httpResponse.statusCode()).thenReturn(201);
        when(httpClient.executeGetRequest(any(), any())).thenReturn(CompletableFuture.completedFuture(httpResponse));

        // when
        boolean result = target.ping(URL, HEADERS);

        //then
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void whenResponseCode401ReturnTrue() {

        // given
        when(httpResponse.statusCode()).thenReturn(401);
        when(httpClient.executeGetRequest(any(), any())).thenReturn(CompletableFuture.completedFuture(httpResponse));

        // when
        boolean result = target.ping(URL, HEADERS);

        //then
        assertThat(result).isEqualTo(false);
    }
}