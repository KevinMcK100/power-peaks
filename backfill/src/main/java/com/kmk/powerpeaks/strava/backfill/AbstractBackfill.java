package com.kmk.powerpeaks.strava.backfill;

import com.kmk.powerpeaks.strava.backfill.model.BaseModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class AbstractBackfill {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    LocalDateTime currentTime;

    public AbstractBackfill(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }

    protected <T extends BaseModel> T setTimestamps(T dataModel, Optional<T> existingRecord) {

        String nowTime = currentTime.format(DATE_TIME_FORMATTER);
        dataModel.setUpdatedAt(nowTime);

        if (existingRecord.isPresent()) {
            dataModel.setCreatedAt(existingRecord.get().getCreatedAt());
        } else {
            dataModel.setCreatedAt(nowTime);
        }

        return dataModel;
    }
}
