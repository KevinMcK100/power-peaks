package com.kmk.powerpeaks.strava.backfill.dao;

import com.kmk.powerpeaks.strava.backfill.model.Power;

import java.util.List;
import java.util.Optional;

public interface PowerDataStoreDao {

    Optional<Power> getPowerByActivityId(Long activityId);

    List<Long> queryAllPowerDataActivityIds();

    void savePower(Power power);
}
