package com.kmk.powerpeaks.strava.auth.model;

import com.google.api.client.util.Key;

public class AccessTokenResponse {

    @Key("access_token")
    private String accessToken;

    @Key("refresh_token")
    private String refreshToken;

    @Key("expires_at")
    private long expiresAt;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

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
                        ", accessToken = '" + accessToken + '\'' +
                        ", refreshToken = '" + refreshToken + '\'' +
                        ", expiresAt = '" + expiresAt + '\'' +
                        "}";
    }
}
