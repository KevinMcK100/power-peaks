package com.kmk.powerpeaks.strava.api.activities;

public enum StreamType {

    TIME ("time"),
    DISTANCE ("distance"),
    LATLNG ("latlng"),
    ALTITUDE ("altitude"),
    VELOCITY_SMOOTH ("velocity_smooth"),
    HEARTRATE ("heartrate"),
    CADENCE ("cadence"),
    WATTS ("watts"),
    TEMP ("temp"),
    MOVING ("moving"),
    GARDE_SMOOTH ("grade_smooth");

    private final String streamType;

    StreamType(final String streamType) {
        this.streamType = streamType;
    }

    @Override
    public String toString() {
        return streamType;
    }
}
