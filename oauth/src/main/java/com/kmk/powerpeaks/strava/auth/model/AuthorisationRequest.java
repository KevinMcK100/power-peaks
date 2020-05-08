package com.kmk.powerpeaks.strava.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorisationRequest extends OAuthRequest {

	private static final String AUTH_CODE_GRANT_TYPE = "authorization_code";

	@JsonProperty("code")
	private String authCode;

	@JsonProperty("grant_type")
	private String grantType = AUTH_CODE_GRANT_TYPE;

	public void setAuthCode(String authCode){
		this.authCode = authCode;
	}

	public String getAuthCode(){
		return authCode;
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
			"authCode = '" + getAuthCode() + '\'' +
			", grant_type = '" + getGrantType() + '\'' +
			", client_secret = '" + getClientSecret() + '\'' +
			", client_id = '" + getClientId() + '\'' +
			"}";
		}
}
