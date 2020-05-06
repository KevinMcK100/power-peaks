package com.kmk.powerpeaks.strava.auth.config;

import java.util.Optional;

public class AwsConfig {

    private String awsProfile;
    private String awsRegion;

    public Optional<String> getAwsProfile() {
        return Optional.ofNullable(awsProfile);
    }

    public void setAwsProfile(String awsProfile) {
        this.awsProfile = awsProfile;
    }

    public Optional<String> getAwsRegion() {
        return Optional.ofNullable(awsRegion);
    }

    public void setAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
    }

    @Override
    public String toString(){
        return
                "AwsConfig{" +
                        "awsProfile = '" + awsProfile + '\'' +
                        ", awsRegion = '" + awsRegion + '\'' +
                        "}";
    }
}
