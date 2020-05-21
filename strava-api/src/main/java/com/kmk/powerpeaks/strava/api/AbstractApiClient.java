package com.kmk.powerpeaks.strava.api;

import com.google.common.annotations.VisibleForTesting;
import io.swagger.client.ApiException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractApiClient {

    private static final Logger LOGGER = Logger.getLogger(AbstractApiClient.class.getName());

    private static final int RATE_LIMIT_RESPONSE_CODE = 429;
    private static final long ONE_HOUR_MILLIS = 3600000L;
    private static final long FIVE_MINUTES_MILLIS = 300000L;

    @VisibleForTesting
    protected boolean handleApiException(ApiException ex) {

        boolean shouldRetry = false;

        if (ex.getCode() == RATE_LIMIT_RESPONSE_CODE) {
            handleRateLimitException(ex);
            shouldRetry = true;
        } else {
            LOGGER.log(Level.WARNING, "Error occurred when trying to hit Strava API", ex);
        }

        return shouldRetry;
    }

    private void handleRateLimitException(ApiException ex) {

        Map<String, List<String>> responseHeaders = ex.getResponseHeaders();

        String limit = responseHeaders.get("X-RateLimit-Limit").get(0);
        String usage = responseHeaders.get("X-RateLimit-Usage").get(0);

        List<String> limitList = Arrays.asList(limit.split(","));
        List<String> usageList = Arrays.asList(usage.split(","));

        int fifteenMinLimit = Integer.parseInt(limitList.get(0));
        int dailyLimit = Integer.parseInt(limitList.get(1));
        int fifteenMinUsage = Integer.parseInt(usageList.get(0));
        int dailyUsage = Integer.parseInt(usageList.get(1));

        if (dailyUsage >= dailyLimit) {
            String errMsg = String.format("Daily rate limit exceeded. Daily limit: %d ::: Daily usage: %d. "
                                                  + "Will retry in 1 hour.", dailyLimit, dailyUsage);
            LOGGER.log(Level.WARNING, errMsg);
            sleep(ONE_HOUR_MILLIS);
        } else if (fifteenMinUsage >= fifteenMinLimit) {
            String errMsg = String.format("15 minute rate limit exceeded. 15 minute limit: %d ::: 15 minute usage: %d. "
                                                  + "Will retry in 5 minutes", fifteenMinLimit, fifteenMinUsage);
            LOGGER.log(Level.WARNING, errMsg);
            sleep(FIVE_MINUTES_MILLIS);
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
