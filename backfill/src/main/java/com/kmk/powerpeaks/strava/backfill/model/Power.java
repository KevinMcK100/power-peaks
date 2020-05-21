package com.kmk.powerpeaks.strava.backfill.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;

@DynamoDbBean
public class Power extends BaseModel {

    private Long activityId;
    private List<Integer> powerStream;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("activity_id")
    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    @DynamoDbAttribute("power_stream")
    public List<Integer> getPowerStream() {
        return powerStream;
    }

    public void setPowerStream(List<Integer> powerStream) {
        this.powerStream = powerStream;
    }


    @Override
    public String toString() {
        return "Power{" +
                "activityId=" + activityId +
                ", powerStream=" + powerStream +
                ", createdAt='" + getCreatedAt() + '\'' +
                ", updatedAt='" + getUpdatedAt() + '\'' +
                '}';
    }
}
