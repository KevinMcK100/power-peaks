package com.kmk.powerpeaks.strava.backfill.config;

import java.time.Instant;

public class BackfillActivitiesConfig {

    private Integer startEpoch;
    private Integer endEpoch;
    private Integer perPage;

    public Integer getStartEpoch() {
        return startEpoch;
    }

    public void setStartEpoch(Integer startEpoch) {
        this.startEpoch = startEpoch;
    }

    public Integer getEndEpoch() {
        if (endEpoch == null || endEpoch <= 0) {
            endEpoch = (int) Instant.now().getEpochSecond();
        }
        return endEpoch;
    }

    public void setEndEpoch(Integer endEpoch) {
        this.endEpoch = endEpoch;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }
}
