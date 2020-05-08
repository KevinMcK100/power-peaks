package com.kmk.powerpeaks;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kmk.powerpeaks.strava.auth.service.AuthService;
import com.kmk.powerpeaks.strava.auth.guice.AuthModule;
import com.kmk.powerpeaks.strava.auth.service.StravaAuthService;
import com.kmk.powerpeaks.strava.auth.service.TestGuice;

public class Main {

    public static void main(String[] args) {

        Injector injector = Guice.createInjector(new AuthModule());

        AuthService authService = injector.getInstance(StravaAuthService.class);

        authService.getValidAuthToken(2402700L);
    }
}
