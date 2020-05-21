package com.kmk.powerpeaks.strava.api.activities;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.ActivitiesApi;
import io.swagger.client.model.DetailedActivity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ActivitiesApiClientTest {

    private static final Long ACTIVITY_ID = 1L;

    @Mock
    private ActivitiesApi activitiesApi;
    @InjectMocks
    private ActivitiesApiClient target;

    @Test
    public void whenUnderRateLimitReturnActivity() throws ApiException {

        // given
        ApiResponse<DetailedActivity> apiResponse = new ApiResponse<>(200, Map.of(), getDetailedActivity());
        when(activitiesApi.getActivityByIdWithHttpInfo(anyLong(), anyBoolean())).thenReturn(apiResponse);

        // when
        DetailedActivity result = target.getActivity(ACTIVITY_ID).get();

        // then
        assertEquals(result.getId(), ACTIVITY_ID);
    }

    private DetailedActivity getDetailedActivity() {

        DetailedActivity activity = new DetailedActivity();
        activity.setId(ACTIVITY_ID);

        return activity;
    }
}