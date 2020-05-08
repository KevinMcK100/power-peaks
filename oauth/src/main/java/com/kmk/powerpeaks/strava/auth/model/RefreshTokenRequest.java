package com.kmk.powerpeaks.strava.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.util.Key;

public class RefreshTokenRequest extends OAuthRequest {

	private static final String REFRESH_TOKEN_GRANT_TYPE = "refresh_token";

	@JsonProperty("refresh_token")
	private String refreshToken;

	@JsonProperty("grant_type")
	private String grantType = REFRESH_TOKEN_GRANT_TYPE;

	public void setRefreshToken(String refreshToken){
		this.refreshToken = refreshToken;
	}

	public String getRefreshToken(){
		return refreshToken;
	}

	public void setGrantType(String grantType){
		this.grantType = grantType;
	}

	public String getGrantType(){
		return grantType;
	}

	@Override
 	public String toString(){
		return 
			"AuthorisationRequest{" +
			"refresh_token = '" + getRefreshToken() + '\'' +
			", grant_type = '" + getGrantType() + '\'' +
			", client_secret = '" + getClientSecret() + '\'' +
			", client_id = '" + getClientId() + '\'' +
			"}";
		}
}
