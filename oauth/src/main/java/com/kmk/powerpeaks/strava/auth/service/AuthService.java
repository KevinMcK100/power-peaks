package com.kmk.powerpeaks.strava.auth.service;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface AuthService {

    Optional<String> getValidAuthToken(long userId);
}
