package com.kmk.powerpeaks.strava.auth.model;

import com.google.api.client.util.Key;

public class AuthorisationRequest extends OAuthRequest {

	private static final String AUTH_CODE_GRANT_TYPE = "authorization_code";

	@Key
	private String code;

	@Key("grant_type")
	private String grantType = AUTH_CODE_GRANT_TYPE;

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
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
			"code = '" + getCode() + '\'' +
			", grant_type = '" + getGrantType() + '\'' +
			", client_secret = '" + getClientSecret() + '\'' +
			", client_id = '" + getClientId() + '\'' +
			"}";
		}
}
