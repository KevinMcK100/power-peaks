package com.kmk.powerpeaks.strava.backfill.dao;

import com.google.inject.Inject;
import com.kmk.powerpeaks.strava.backfill.model.Power;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PowerDynamoDbDao implements PowerDataStoreDao {

    DynamoDbTable<Power> powerTable;

    private static final String TABLE_NAME = "POWER";

    @Inject
    public PowerDynamoDbDao(DynamoDbEnhancedClient enhancedClient) {
        powerTable = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(Power.class));
    }

    @Override
    public Optional<Power> getPowerByActivityId(Long activityId) {

        Key key = Key.builder().partitionValue(activityId).build();
        return Optional.ofNullable(powerTable.getItem(key));
    }

    @Override
    public List<Long> queryAllPowerDataActivityIds() {

        return powerTable.scan()
                         .items()
                         .stream()
                         .map(Power::getActivityId)
                         .collect(Collectors.toList());
    }

    @Override
    public void savePower(Power power) {

        powerTable.putItem(power);
    }
}
