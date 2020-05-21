package com.kmk.powerpeaks.strava.backfill;

import static java.util.stream.Collectors.joining;

import com.google.inject.Inject;
import com.kmk.powerpeaks.strava.api.activities.StreamsApiClient;
import com.kmk.powerpeaks.strava.backfill.config.BackfillPowerConfig;
import com.kmk.powerpeaks.strava.backfill.dao.ActivitiesDataStoreDao;
import com.kmk.powerpeaks.strava.backfill.dao.PowerDataStoreDao;
import com.kmk.powerpeaks.strava.backfill.model.Power;
import io.swagger.client.model.PowerStream;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BackfillPower extends AbstractBackfill {

    private static final Logger LOGGER = Logger.getLogger(BackfillPower.class.getName());

    private StreamsApiClient streamsApiClient;
    private ActivitiesDataStoreDao activitiesDataStoreDao;
    private PowerDataStoreDao powerDataStoreDao;
    private BackfillPowerConfig config;

    @Inject
    public BackfillPower(LocalDateTime currentTime,
                         StreamsApiClient streamsApiClient,
                         ActivitiesDataStoreDao activitiesDataStoreDao,
                         PowerDataStoreDao powerDataStoreDao,
                         BackfillPowerConfig config) {
        super(currentTime);
        this.streamsApiClient = streamsApiClient;
        this.activitiesDataStoreDao = activitiesDataStoreDao;
        this.powerDataStoreDao = powerDataStoreDao;
        this.config = config;
    }

    public void backfillPower() {

        activitiesToBackfill().stream()
                              .map(activityId -> streamsApiClient
                                      .streamPower(activityId)
                                      .map(powerStream -> mapPowerStream(activityId, powerStream)))
                              .filter(Optional::isPresent)
                              .flatMap(Optional::stream)
                              .peek(power -> LOGGER.log(Level.INFO, power.toString()))
                              .forEach(powerDataStoreDao::savePower);
    }

    private List<Long> activitiesToBackfill() {

        List<Long> activities;

        if (config.getActivities() != null && config.getActivities().size() > 0) {
            activities = new ArrayList<>(config.getActivities());
            LOGGER.log(Level.INFO, "Fetching power data for activity IDs specified in config. Activity IDs: " +
                    activities.stream().map(String::valueOf).collect(joining(", ")));
        } else {
            activities = activitiesDataStoreDao.queryAllActivityIdsWithPower();
            LOGGER.log(Level.INFO, "Fetching activity IDs in ACTIVITIES table. List Size: " +
                    activities.size());
        }

        if (!config.isOverwriteExisting()) {
            activities.removeAll(powerDataStoreDao.queryAllPowerDataActivityIds());
            LOGGER.log(Level.INFO, String.format("Existing power streams will not be overwritten. Fetching power " +
                                                         "data for %d activities.", activities.size()));
        }

        return activities;
    }

    private Power mapPowerStream(Long activityId, PowerStream powerStream) {

        Power power = new Power();
        power.setActivityId(activityId);
        List<Integer> powerList = powerStream.getData();
        Collections.replaceAll(powerList, null, 0);
        power.setPowerStream(powerList);
        setTimestamps(power, powerDataStoreDao.getPowerByActivityId(activityId));

        return power;
    }
}
