package com.kmk.powerpeaks.strava.backfill.dao;

import com.google.inject.Inject;
import com.kmk.powerpeaks.strava.backfill.model.Activity;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ActivitiesDynamoDbDao implements ActivitiesDataStoreDao {

    DynamoDbTable<Activity> activitiesTable;

    private static final String TABLE_NAME = "ACTIVITIES";

    @Inject
    public ActivitiesDynamoDbDao(DynamoDbEnhancedClient enhancedClient) {
        activitiesTable = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(Activity.class));
    }

    @Override
    public Optional<Activity> getActivityById(long activityId) {

        Key key = Key.builder().partitionValue(activityId).build();
        return Optional.ofNullable(activitiesTable.getItem(key));
    }

    @Override
    public List<Long> queryAllActivityIdsWithPower() {

        AttributeValue attValueTrue = AttributeValue.builder().bool(true).build();
        Expression filterExpression = Expression.builder()
                                                .expression("device_watts = :true")
                                                .putExpressionValue(":true", attValueTrue)
                                                .build();

        ScanEnhancedRequest request = ScanEnhancedRequest.builder().filterExpression(filterExpression).build();

        return activitiesTable.scan(request)
                              .items()
                              .stream()
                              .map(Activity::getActivityId)
                              .collect(Collectors.toList());
    }

    @Override
    public void saveActivity(Activity activity) {

        activitiesTable.putItem(activity);
    }
}
