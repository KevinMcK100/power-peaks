package com.kmk.powerpeaks.strava.backfill.mapper;

import com.kmk.powerpeaks.strava.backfill.model.Activity;
import io.swagger.client.model.DetailedActivity;
import io.swagger.client.model.SummaryActivity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ActivityMapper {

    ActivityMapper INSTANCE = Mappers.getMapper(ActivityMapper.class);

    @Mapping(target = "activityId", source = "id")
    @Mapping(target = "athleteId", source = "athlete.id")
    @Mapping(target = "startDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    Activity detailedActivityMapper(DetailedActivity sourceActivity);

    @Mapping(target = "activityId", source = "id")
    @Mapping(target = "athleteId", source = "athlete.id")
    @Mapping(target = "startDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    Activity summaryActivityMapper(SummaryActivity sourceActivity);
}
