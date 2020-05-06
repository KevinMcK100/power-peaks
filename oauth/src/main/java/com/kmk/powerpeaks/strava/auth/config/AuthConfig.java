package com.kmk.powerpeaks.strava.auth.config;

import java.util.Optional;

public class AuthConfig {

    private String clientId;
    private String clientSecret;
    private String authCode;

    public Optional<String> getClientId() {
        return Optional.ofNullable(clientId);
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Optional<String> getClientSecret() {
        return Optional.ofNullable(clientSecret);
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Optional<String> getAuthCode() {
        return Optional.ofNullable(authCode);
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    @Override
    public String toString(){
        return
                "AuthConfig{" +
                        "clientId = '" + clientId + '\'' +
                        ", clientSecret = '" + clientSecret + '\'' +
                        ", authCode = '" + authCode + '\'' +
                        "}";
    }
}
