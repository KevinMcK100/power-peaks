package com.kmk.powerpeaks.strava.auth.dao;

import com.kmk.powerpeaks.strava.auth.model.OAuthData;

import java.util.Optional;

public interface AuthDataStoreDao {

    Optional<OAuthData> getToken(String athleteId);

    void putToken(OAuthData oAuthData);

}
