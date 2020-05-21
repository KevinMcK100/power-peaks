package com.kmk.powerpeaks.strava.backfill.dao;

import com.kmk.powerpeaks.strava.backfill.model.Activity;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

import java.util.List;
import java.util.Optional;

public interface ActivitiesDataStoreDao {

    Optional<Activity> getActivityById(long activityId);

    List<Long> queryAllActivityIdsWithPower();

    void saveActivity(Activity activity);
}
