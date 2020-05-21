package com.kmk.powerpeaks;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kmk.powerpeaks.strava.auth.guice.AuthModule;
import com.kmk.powerpeaks.strava.auth.service.AuthService;
import com.kmk.powerpeaks.strava.auth.service.StravaAuthService;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.api.AthletesApi;
import io.swagger.client.auth.OAuth;
import io.swagger.client.model.DetailedAthlete;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class StravaApiTest {

    private static final Long ATHLETE_ID = 1L;

    public static void main(String[] args) throws ApiException {

        Injector injector = Guice.createInjector(new AuthModule());
        AuthService authService = injector.getInstance(StravaAuthService.class);

        ApiClient defaultClient = Configuration.getDefaultApiClient();
        OAuth oAuth = (OAuth) defaultClient.getAuthentication("strava_oauth");
        oAuth.setAccessToken(authService.getValidAuthToken(ATHLETE_ID).get());

        AthletesApi athleteApi = new AthletesApi();
        DetailedAthlete athlete = athleteApi.getLoggedInAthlete();
        System.out.println("Logged in athlete: " + athlete.getFirstname() + " " + athlete.getLastname());
    }
}
