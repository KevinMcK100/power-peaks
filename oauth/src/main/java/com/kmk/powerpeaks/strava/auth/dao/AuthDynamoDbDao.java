package com.kmk.powerpeaks.strava.auth.dao;

import com.google.inject.Inject;
import com.kmk.powerpeaks.strava.auth.model.OAuthData;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Optional;

public class AuthDynamoDbDao implements AuthDataStoreDao {

    DynamoDbTable<OAuthData> authTable;

    private static final String TABLE_NAME = "AUTH";

    @Inject
    public AuthDynamoDbDao(DynamoDbEnhancedClient enhancedClient) {
        authTable = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(OAuthData.class));
    }

    @Override
    public Optional<OAuthData> getToken(String athleteId) {

        Key key = Key.builder().partitionValue(athleteId).build();
        return Optional.ofNullable(authTable.getItem(key));
    }

    @Override
    public void putToken(OAuthData oAuthData) {

        authTable.putItem(oAuthData);
    }
}
