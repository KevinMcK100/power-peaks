package com.kmk.powerpeaks;

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

    public static void main(String[] args) throws ApiException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(URI.create("http://openjdk.java.net/"))
                                         .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
              .thenApply(HttpResponse::body)
              .thenAccept(System.out::println)
              .join();


        ApiClient defaultClient = Configuration.getDefaultApiClient();
        OAuth oAuth = (OAuth) defaultClient.getAuthentication("strava_oauth");
        oAuth.setAccessToken("");

        AthletesApi athleteApi = new AthletesApi();
        DetailedAthlete athlete = athleteApi.getLoggedInAthlete();
        System.out.println("Logged in athlete: " + athlete.getFirstname() + " " + athlete.getLastname());
    }
}
