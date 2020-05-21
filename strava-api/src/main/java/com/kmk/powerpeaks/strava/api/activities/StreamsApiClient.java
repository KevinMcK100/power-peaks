package com.kmk.powerpeaks.strava.api.activities;

import com.google.inject.Inject;
import com.kmk.powerpeaks.strava.api.AbstractApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.StreamsApi;
import io.swagger.client.model.LatLng;
import io.swagger.client.model.PowerStream;
import io.swagger.client.model.StreamSet;
import io.swagger.client.model.TimeStream;

import java.util.List;
import java.util.Optional;

public class StreamsApiClient extends AbstractApiClient {

    private static final int MAX_RETRIES = 25;
    private final StreamsApi streamApi;

    @Inject
    public StreamsApiClient(StreamsApi streamApi) {
        this.streamApi = streamApi;
    }

    public Optional<PowerStream> streamPower(long activityId) {

        int retries = 0;
        while (retries <= MAX_RETRIES) {
            try {
                List<String> streamTypes = List.of(StreamType.WATTS.toString());
                ApiResponse<StreamSet> activityStreamsWithHttpInfo
                        = streamApi.getActivityStreamsWithHttpInfo(activityId, streamTypes, true);
                return Optional.of(activityStreamsWithHttpInfo.getData().getWatts());
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
}
