package com.kmk.powerpeaks.strava.backfill.config;

import java.util.List;

public class BackfillPowerConfig {

    private boolean overwriteExisting;
    private List<Long> activities;

    public boolean isOverwriteExisting() {
        return overwriteExisting;
    }

    public void setOverwriteExisting(boolean overwriteExisting) {
        this.overwriteExisting = overwriteExisting;
    }

    public List<Long> getActivities() {
        return activities;
    }

    public void setActivities(List<Long> activities) {
        this.activities = activities;
    }

    @Override
    public String toString() {
        return "BackfillPowerConfig{" +
                "overwriteExisting=" + overwriteExisting +
                ", activities=" + activities +
                '}';
    }
}
