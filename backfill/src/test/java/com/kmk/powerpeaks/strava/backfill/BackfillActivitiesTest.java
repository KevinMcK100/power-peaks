package com.kmk.powerpeaks.strava.backfill;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kmk.powerpeaks.strava.api.activities.ActivitiesApiClient;
import com.kmk.powerpeaks.strava.backfill.config.BackfillActivitiesConfig;
import com.kmk.powerpeaks.strava.backfill.dao.ActivitiesDataStoreDao;
import com.kmk.powerpeaks.strava.backfill.model.Activity;
import io.swagger.client.model.SummaryActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class BackfillActivitiesTest {

    private BackfillActivities target;

    @Mock
    private ActivitiesApiClient activitiesApiClient;
    @Mock
    private ActivitiesDataStoreDao activitiesDataStoreDao;
    @Mock
    private BackfillActivitiesConfig config;

    @Captor
    private ArgumentCaptor<Activity> activityCaptor;

    private static final LocalDateTime CURRENT_TIME = LocalDateTime.of(2020, 5, 16, 12, 7, 3);
    private static final String FORMATTED_CURRENT_TIME = "2020-05-16 12:07:03";
    private static final String FORMATTED_CREATED_AT_TIME = "2020-04-06 09:14:55";
    private static final Long ACTIVITY_ID = 123456789L;

    @Before
    public void setUp() {

        target = new BackfillActivities(CURRENT_TIME, activitiesApiClient, activitiesDataStoreDao, config);
        when(activitiesApiClient.getActivities(anyInt(), anyInt(), anyInt(), anyInt()))
                .thenReturn(getListActivities()).thenReturn(List.of());
    }

    @Test
    public void whenActivityExistsUpdateUpdatedAtTimestamp() {

        // when
        target.backfillActivities();

        // then
        verify(activitiesDataStoreDao).saveActivity(activityCaptor.capture());
        Activity actualActivity = activityCaptor.getValue();
        assertEquals(actualActivity.getActivityId(), ACTIVITY_ID);
        assertEquals(actualActivity.getCreatedAt(), FORMATTED_CURRENT_TIME);
        assertEquals(actualActivity.getUpdatedAt(), FORMATTED_CURRENT_TIME);
    }

    @Test
    public void whenActivityNotExistsUpdateCreatedAtAndUpdatedAtTimestamps() {

        // given
        when(activitiesDataStoreDao.getActivityById(ACTIVITY_ID)).thenReturn(getActivityWithId(ACTIVITY_ID));

        // when
        target.backfillActivities();

        // then
        verify(activitiesDataStoreDao).saveActivity(activityCaptor.capture());
        Activity actualActivity = activityCaptor.getValue();

        assertEquals(actualActivity.getActivityId(), ACTIVITY_ID);
        assertEquals(actualActivity.getCreatedAt(), FORMATTED_CREATED_AT_TIME);
        assertEquals(actualActivity.getUpdatedAt(), FORMATTED_CURRENT_TIME);
    }

    private List<SummaryActivity> getListActivities() {
        SummaryActivity activity = new SummaryActivity();
        activity.setId(ACTIVITY_ID);
        return List.of(activity);
    }

    private Optional<Activity> getActivityWithId(Long activityId) {
        Activity activity = new Activity();
        activity.setActivityId(activityId);
        activity.setCreatedAt(FORMATTED_CREATED_AT_TIME);
        return Optional.of(activity);
    }
}