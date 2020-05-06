package com.kmk.powerpeaks.strava.auth.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class OAuthData {

    private String athleteId;
    private String clientId;
    private String clientSecret;
    private String accessToken;
    private String refreshToken;
    private long expiresAt;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("athlete_id")
    public String getAthleteId() {
        return athleteId;
    }
    public void setAthleteId(String athleteId) {
        this.athleteId = athleteId;
    }

    @DynamoDbAttribute("client_id")
    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @DynamoDbAttribute("client_secret")
    public String getClientSecret() {
        return clientSecret;
    }
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    @DynamoDbAttribute("access_token")
    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @DynamoDbAttribute("refresh_token")
    public String getRefreshToken() {
        return refreshToken;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @DynamoDbAttribute("expires_at")
    public long getExpiresAt() {
        return expiresAt;
    }
    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public String toString(){
        return
                "OAuthData{" +
                        "athleteId = '" + athleteId + '\'' +
                        ", clientId = '" + clientId + '\'' +
                        ", clientSecret = '" + clientSecret + '\'' +
                        ", accessToken = '" + accessToken + '\'' +
                        ", refreshToken = '" + refreshToken + '\'' +
                        ", expiresAt = '" + expiresAt + '\'' +
                        "}";
    }
}
