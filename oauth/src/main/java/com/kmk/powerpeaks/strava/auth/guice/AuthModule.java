package com.kmk.powerpeaks.strava.auth.guice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.kmk.powerpeaks.datastore.guice.DataStoreModule;
import com.kmk.powerpeaks.strava.auth.config.AuthConfig;
import com.kmk.powerpeaks.strava.auth.dao.AuthDataStoreDao;
import com.kmk.powerpeaks.strava.auth.dao.AuthDynamoDbDao;
import com.kmk.powerpeaks.strava.auth.http.AuthHttpClient;
import com.kmk.powerpeaks.strava.auth.http.StravaAuthHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthModule extends AbstractModule {

    private static final Logger LOGGER = Logger.getLogger(AuthModule.class.getName());

    private static final String AUTH_CONFIG_FILE = "auth-config.yml";

    private static final String MISSING_AUTH_CREDENTIALS_LOG =
            String.format("Unable to read config file '%s'", AUTH_CONFIG_FILE);


    @Override
    protected void configure() {

        install(new DataStoreModule());
        bind(AuthHttpClient.class).to(StravaAuthHttpClient.class);
        bind(AuthDataStoreDao.class).to(AuthDynamoDbDao.class);
    }

    @Provides
    AuthConfig provideAuthConfig() {

        InputStream configFile = ClassLoader.getSystemClassLoader().getResourceAsStream(AUTH_CONFIG_FILE);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            return Optional.ofNullable(mapper.readValue(configFile, AuthConfig.class)).orElseGet(AuthConfig::new);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, MISSING_AUTH_CREDENTIALS_LOG, e);
            return new AuthConfig();
        }
    }
}
