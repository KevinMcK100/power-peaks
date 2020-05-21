package com.kmk.powerpeaks.strava.api.activities;

import com.google.inject.Inject;
import com.kmk.powerpeaks.strava.api.AbstractApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.ActivitiesApi;
import io.swagger.client.model.DetailedActivity;
import io.swagger.client.model.SummaryActivity;

import java.util.List;
import java.util.Optional;

public class ActivitiesApiClient extends AbstractApiClient {

    private static final int MAX_RETRIES = 25;
    private final ActivitiesApi activitiesApi;

    @Inject
    public ActivitiesApiClient(ActivitiesApi activitiesApi) {
        this.activitiesApi = activitiesApi;
    }

    public Optional<DetailedActivity> getActivity(long activityId) {

        int retries = 0;
        while (retries <= MAX_RETRIES) {
            try {
                ApiResponse<DetailedActivity> apiResponse = activitiesApi.getActivityByIdWithHttpInfo(activityId, false);
                return Optional.of(apiResponse.getData());
            } catch (ApiException ex) {
                boolean shouldRetry = handleApiException(ex);
                if (!shouldRetry) {
                    return Optional.empty();
                }
                retries += 1;
            }
        }
        return Optional.empty();
    }

    public List<SummaryActivity> getActivities(int before, int after, int page, int perPage) {

        int retries = 0;
        while (retries <= MAX_RETRIES) {
            try {
                ApiResponse<List<SummaryActivity>> apiResponse =
                        activitiesApi.getLoggedInAthleteActivitiesWithHttpInfo(before, after, page, perPage);
                return apiResponse.getData();
            } catch (ApiException ex) {
                boolean shouldRetry = handleApiException(ex);
                if (!shouldRetry) {
                    return List.of();
                }
                retries += 1;
            }
        }
        return List.of();
    }
}
