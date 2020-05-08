package com.kmk.powerpeaks.strava.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OAuthRequest {

    @JsonProperty("client_secret")
    private String clientSecret;
    @JsonProperty("client_id")
    private long clientId;

    public void setClientSecret(String clientSecret){
        this.clientSecret = clientSecret;
    }

    public String getClientSecret(){
        return clientSecret;
    }

    public void setClientId(long clientId){
        this.clientId = clientId;
    }

    public long getClientId(){
        return clientId;
    }
}
