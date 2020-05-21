package com.kmk.powerpeaks.strava.backfill.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.kmk.powerpeaks.strava.backfill.model.Activity;
import io.swagger.client.model.DetailedActivity;
import io.swagger.client.model.MetaAthlete;
import io.swagger.client.model.SummaryActivity;
import org.joda.time.DateTime;
import org.junit.Test;

public class ActivityMapperTest {

    private static final long ACTIVITY_ID = 123456789L;
    private static final Integer ATHLETE_ID = 12345;
    private static final String NAME = "TrainerRoad - Eclipse";
    private static final String DESCRIPTION = "That was hard!";
    private static final float DISTANCE = 12.1F;
    private static final DateTime START_DATE = new DateTime(2020, 5, 15, 0, 0, 0, 0);
    private static final String START_DATE_STRING = "2020-05-15 00:00:00";

    @Test
    public void testMappingDetailedActivityToActivityBean() {

        // given
        MetaAthlete athlete = new MetaAthlete();
        athlete.setId(ATHLETE_ID);

        DetailedActivity sourceActivity = new DetailedActivity();
        sourceActivity.setId(ACTIVITY_ID);
        sourceActivity.setAthlete(athlete);
        sourceActivity.setDescription(DESCRIPTION);
        sourceActivity.setName(NAME);
        sourceActivity.setDistance(DISTANCE);
        sourceActivity.setStartDate(START_DATE);
        sourceActivity.setCommute(false);

        // when
        Activity targetActivity = ActivityMapper.INSTANCE.detailedActivityMapper(sourceActivity);

        // then
        assertThat(targetActivity).isNotNull();
        assertThat(targetActivity.getActivityId()).isEqualTo(ACTIVITY_ID);
        assertThat(targetActivity.getAthleteId()).isEqualTo(ATHLETE_ID);
        assertThat(targetActivity.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(targetActivity.getName()).isEqualTo(NAME);
        assertThat(targetActivity.getDistance()).isEqualTo(DISTANCE);
        assertThat(targetActivity.getStartDate()).isEqualTo(START_DATE_STRING);
        assertThat(targetActivity.getCommute()).isEqualTo(false);
    }

    @Test
    public void testMappingSummaryActivityToActivityBean() {

        // given
        MetaAthlete athlete = new MetaAthlete();
        athlete.setId(ATHLETE_ID);

        SummaryActivity sourceActivity = new SummaryActivity();
        sourceActivity.setId(ACTIVITY_ID);
        sourceActivity.setAthlete(athlete);
        sourceActivity.setName(NAME);
        sourceActivity.setDistance(DISTANCE);
        sourceActivity.setStartDate(START_DATE);
        sourceActivity.setCommute(false);

        // when
        Activity targetActivity = ActivityMapper.INSTANCE.summaryActivityMapper(sourceActivity);

        // then
        assertThat(targetActivity).isNotNull();
        assertThat(targetActivity.getActivityId()).isEqualTo(ACTIVITY_ID);
        assertThat(targetActivity.getAthleteId()).isEqualTo(ATHLETE_ID);
        assertThat(targetActivity.getName()).isEqualTo(NAME);
        assertThat(targetActivity.getDistance()).isEqualTo(DISTANCE);
        assertThat(targetActivity.getStartDate()).isEqualTo(START_DATE_STRING);
        assertThat(targetActivity.getCommute()).isEqualTo(false);
    }
}