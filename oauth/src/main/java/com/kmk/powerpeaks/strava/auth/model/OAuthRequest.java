package com.kmk.powerpeaks.strava.auth.model;

import com.google.api.client.util.Key;

public class OAuthRequest implements RequestBean {

    @Key("client_secret")
    private String clientSecret;
    @Key("client_id")
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
