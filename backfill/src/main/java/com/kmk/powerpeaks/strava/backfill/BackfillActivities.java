package com.kmk.powerpeaks.strava.backfill;

import com.google.inject.Inject;
import com.kmk.powerpeaks.strava.api.activities.ActivitiesApiClient;
import com.kmk.powerpeaks.strava.backfill.config.BackfillActivitiesConfig;
import com.kmk.powerpeaks.strava.backfill.dao.ActivitiesDataStoreDao;
import com.kmk.powerpeaks.strava.backfill.mapper.ActivityMapper;
import com.kmk.powerpeaks.strava.backfill.model.Activity;
import com.kmk.powerpeaks.strava.backfill.model.BaseModel;
import io.swagger.client.model.SummaryActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BackfillActivities extends AbstractBackfill {

    private static final Logger LOGGER = Logger.getLogger(BackfillActivities.class.getName());

    ActivitiesApiClient activitiesApiClient;
    ActivitiesDataStoreDao activitiesDataStoreDao;
    BackfillActivitiesConfig config;

    @Inject
    public BackfillActivities(LocalDateTime currentTime,
                              ActivitiesApiClient activitiesApiClient,
                              ActivitiesDataStoreDao activitiesDataStoreDao,
                              BackfillActivitiesConfig config) {
        super(currentTime);
        this.activitiesApiClient = activitiesApiClient;
        this.activitiesDataStoreDao = activitiesDataStoreDao;
        this.config = config;
    }

    public void backfillActivities() {

        List<SummaryActivity> activityList;
        int activitiesProcessed = 0;
        int page = 1;
        do {
            activityList = activitiesApiClient.getActivities(config.getEndEpoch(), config.getStartEpoch(),
                                                             page, config.getPerPage());

            activityList.stream()
                        .map(ActivityMapper.INSTANCE::summaryActivityMapper)
                        .map(activity -> setTimestamps(
                                activity, activitiesDataStoreDao.getActivityById(activity.getActivityId())))
                        .peek(activity -> LOGGER.log(Level.INFO, activity.toString()))
                        .forEach(activitiesDataStoreDao::saveActivity);
            page += 1;
            activitiesProcessed += activityList.size();
        } while (activityList.size() != 0);

        LOGGER.log(Level.INFO, "Backfill of activities completed. Total activities processed: " + activitiesProcessed);
    }
}
