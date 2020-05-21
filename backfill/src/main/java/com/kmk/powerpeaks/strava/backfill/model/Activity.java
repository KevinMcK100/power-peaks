package com.kmk.powerpeaks.strava.backfill.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;


@DynamoDbBean
public class Activity extends BaseModel {

    private Long activityId;
    private Integer athleteId;
    private String name;
    private String description;
    private Float distance;
    private Integer movingTime;
    private Integer elapsedTime;
    private Float totalElevationGain;
    private Integer workoutType;
    private String startDate;
    private Integer achievementCount;
    private Integer athleteCount;
    private Integer totalPhotoCount;
    private Boolean trainer;
    private Boolean commute;
    private Boolean manual;
    private Boolean _private;
    private Boolean flagged;
    private String gearId;
    private Float averageSpeed;
    private Float maxSpeed;
    private Float averageWatts;
    private Integer weightedAverageWatts;
    private Float kilojoules;
    private Boolean deviceWatts;
    private Integer maxWatts;
    private Float elevHigh;
    private Float elevLow;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("activity_id")
    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    @DynamoDbAttribute("athlete_id")
    public Integer getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(Integer athleteId) {
        this.athleteId = athleteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    @DynamoDbAttribute("moving_time")
    public Integer getMovingTime() {
        return movingTime;
    }

    public void setMovingTime(Integer movingTime) {
        this.movingTime = movingTime;
    }

    @DynamoDbAttribute("elapsed_time")
    public Integer getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(Integer elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    @DynamoDbAttribute("total_elevation_gain")
    public Float getTotalElevationGain() {
        return totalElevationGain;
    }

    public void setTotalElevationGain(Float totalElevationGain) {
        this.totalElevationGain = totalElevationGain;
    }

    @DynamoDbAttribute("workout_type")
    public Integer getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(Integer workoutType) {
        this.workoutType = workoutType;
    }

    @DynamoDbAttribute("start_date")
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @DynamoDbAttribute("achievement_count")
    public Integer getAchievementCount() {
        return achievementCount;
    }

    public void setAchievementCount(Integer achievementCount) {
        this.achievementCount = achievementCount;
    }

    @DynamoDbAttribute("athlete_count")
    public Integer getAthleteCount() {
        return athleteCount;
    }

    public void setAthleteCount(Integer athleteCount) {
        this.athleteCount = athleteCount;
    }

    @DynamoDbAttribute("total_photo_count")
    public Integer getTotalPhotoCount() {
        return totalPhotoCount;
    }

    public void setTotalPhotoCount(Integer totalPhotoCount) {
        this.totalPhotoCount = totalPhotoCount;
    }

    public Boolean getTrainer() {
        return trainer;
    }

    public void setTrainer(Boolean trainer) {
        this.trainer = trainer;
    }

    public Boolean getCommute() {
        return commute;
    }

    public void setCommute(Boolean commute) {
        this.commute = commute;
    }

    public Boolean getManual() {
        return manual;
    }

    public void setManual(Boolean manual) {
        this.manual = manual;
    }

    @DynamoDbAttribute("private")
    public Boolean get_private() {
        return _private;
    }

    public void set_private(Boolean _private) {
        this._private = _private;
    }

    public Boolean getFlagged() {
        return flagged;
    }

    public void setFlagged(Boolean flagged) {
        this.flagged = flagged;
    }

    @DynamoDbAttribute("gear_id")
    public String getGearId() {
        return gearId;
    }

    public void setGearId(String gearId) {
        this.gearId = gearId;
    }

    @DynamoDbAttribute("average_speed")
    public Float getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(Float averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    @DynamoDbAttribute("max_speed")
    public Float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    @DynamoDbAttribute("average_watts")
    public Float getAverageWatts() {
        return averageWatts;
    }

    public void setAverageWatts(Float averageWatts) {
        this.averageWatts = averageWatts;
    }

    @DynamoDbAttribute("weighted_average_watts")
    public Integer getWeightedAverageWatts() {
        return weightedAverageWatts;
    }

    public void setWeightedAverageWatts(Integer weightedAverageWatts) {
        this.weightedAverageWatts = weightedAverageWatts;
    }

    public Float getKilojoules() {
        return kilojoules;
    }

    public void setKilojoules(Float kilojoules) {
        this.kilojoules = kilojoules;
    }

    @DynamoDbAttribute("device_watts")
    public Boolean getDeviceWatts() {
        return deviceWatts;
    }

    public void setDeviceWatts(Boolean deviceWatts) {
        this.deviceWatts = deviceWatts;
    }

    @DynamoDbAttribute("max_watts")
    public Integer getMaxWatts() {
        return maxWatts;
    }

    public void setMaxWatts(Integer maxWatts) {
        this.maxWatts = maxWatts;
    }

    @DynamoDbAttribute("highest_elevation")
    public Float getElevHigh() {
        return elevHigh;
    }

    public void setElevHigh(Float elevHigh) {
        this.elevHigh = elevHigh;
    }

    @DynamoDbAttribute("lowest_elevation")
    public Float getElevLow() {
        return elevLow;
    }

    public void setElevLow(Float elevLow) {
        this.elevLow = elevLow;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "activityId=" + activityId +
                ", athleteId=" + athleteId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", distance=" + distance +
                ", movingTime=" + movingTime +
                ", elapsedTime=" + elapsedTime +
                ", totalElevationGain=" + totalElevationGain +
                ", workoutType=" + workoutType +
                ", startDate='" + startDate + '\'' +
                ", achievementCount=" + achievementCount +
                ", athleteCount=" + athleteCount +
                ", totalPhotoCount=" + totalPhotoCount +
                ", trainer=" + trainer +
                ", commute=" + commute +
                ", manual=" + manual +
                ", _private=" + _private +
                ", flagged=" + flagged +
                ", gearId='" + gearId + '\'' +
                ", averageSpeed=" + averageSpeed +
                ", maxSpeed=" + maxSpeed +
                ", averageWatts=" + averageWatts +
                ", weightedAverageWatts=" + weightedAverageWatts +
                ", kilojoules=" + kilojoules +
                ", deviceWatts=" + deviceWatts +
                ", maxWatts=" + maxWatts +
                ", elevHigh=" + elevHigh +
                ", elevLow=" + elevLow +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}
