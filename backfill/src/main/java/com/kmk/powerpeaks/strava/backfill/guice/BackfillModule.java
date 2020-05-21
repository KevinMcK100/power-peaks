package com.kmk.powerpeaks.strava.backfill.guice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.kmk.powerpeaks.strava.api.guice.StravaApiModule;
import com.kmk.powerpeaks.strava.backfill.config.BackfillActivitiesConfig;
import com.kmk.powerpeaks.strava.backfill.config.BackfillPowerConfig;
import com.kmk.powerpeaks.strava.backfill.dao.ActivitiesDataStoreDao;
import com.kmk.powerpeaks.strava.backfill.dao.ActivitiesDynamoDbDao;
import com.kmk.powerpeaks.strava.backfill.dao.PowerDataStoreDao;
import com.kmk.powerpeaks.strava.backfill.dao.PowerDynamoDbDao;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BackfillModule extends AbstractModule {

    private static final Logger LOGGER = Logger.getLogger(BackfillModule.class.getName());

    private static final String BACKFILL_ACTIVITIES_CONFIG_FILE = "backfill-activities-config.yml";
    private static final String BACKFILL_POWER_CONFIG_FILE = "backfill-power-config.yml";

    private static final String MISSING_CONFIG_LOG = "Unable to read config file: ";


    @Override
    protected void configure() {
        install(new StravaApiModule());
        bind(ActivitiesDataStoreDao.class).to(ActivitiesDynamoDbDao.class);
        bind(PowerDataStoreDao.class).to(PowerDynamoDbDao.class);
    }

    @Provides
    BackfillActivitiesConfig provideBackfillActivitiesConfig() {

        InputStream configFile = ClassLoader.getSystemClassLoader()
                                            .getResourceAsStream(BACKFILL_ACTIVITIES_CONFIG_FILE);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            return Optional.ofNullable(mapper.readValue(configFile, BackfillActivitiesConfig.class))
                           .orElseGet(BackfillActivitiesConfig::new);
        } catch (IOException e) {
            String errMsg = MISSING_CONFIG_LOG + BACKFILL_ACTIVITIES_CONFIG_FILE;
            LOGGER.log(Level.WARNING, errMsg, e);
            return new BackfillActivitiesConfig();
        }
    }

    @Provides
    BackfillPowerConfig provideBackfillPowerConfig() {

        InputStream configFile = ClassLoader.getSystemClassLoader()
                                            .getResourceAsStream(BACKFILL_POWER_CONFIG_FILE);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            return Optional.ofNullable(mapper.readValue(configFile, BackfillPowerConfig.class))
                           .orElseGet(BackfillPowerConfig::new);
        } catch (IOException e) {
            String errMsg = MISSING_CONFIG_LOG + BACKFILL_POWER_CONFIG_FILE;
            LOGGER.log(Level.WARNING, errMsg, e);
            return new BackfillPowerConfig();
        }
    }

    @Provides
    LocalDateTime provideInstant() {
        return LocalDateTime.now();
    }
}
