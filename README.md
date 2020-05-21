# Power Peaks

## What is Power Peaks
The Power Peaks project is intended to make use of the Strava API to fetch an athlete's activity and power data for 
analysis. Streams of power data ingested from the Strava API are used to calculate power PRs over various durations. 
Users are then notified of any PRs achieved on their latest activities.

Currently this project does not support multiple users. It is intended to be used by anyone who has an API Application
set up with Strava and can use their Client ID and Client Secret to preform OAuth

## Installation

### OAuth with Strava API
Before running the project you will need to undertake a few steps to authenticate with Strava's API using the `oauth`
module provided. 
- Sign up for an API Application with Strava if you don't already have one - https://www.strava.com/register
- Under the `resources` directory of the `oauth` module, copy the `auth-config.sample.yml` file to create an 
`auth-config.yml` file
- Update the `clientId` and `clientSecret` to the values found at - https://www.strava.com/settings/api
- On the same API settings page, add `developers.strava.com` as the **Authorization Callback Domain**
- Click on **OAuth Authorization page** link (appending `&scope=activity:read_all` to the end if you want to grant 
access to private activities also)
- Click the **Authorize** button, at which point you'll be redirected to the callback domain specified previously
- Copy the auth code from the callback URL
- Paste the code into the `auth-config.yml` file under the `authCode` property
- Open the `StravaApiTest` main class in the `oauth` module and update the athlete ID to the athlete you registered the 
API Application against
- When you execute the application you should see your athlete name logged to the console

##### Few Side Notes on OAuth Process
- The auth code generated in the steps above can only be used once
- You will only need to undertake the above steps on the initial set up of the application. From that point forward, the 
expiry time and refresh token are used for token management and you should always receive a valid token by simply 
calling the `StravaAuthService.getValidAuthToken`
- You will need to have an AWS profile set up as DynamoDB is used for token storage. You can change your AWS profile
from the default profile by copying the `aws-config.sample.yml` file found under the `core` module and creating a 
`aws-config.yml` config file with your profile name and region set as properties
- You will need to create the `AUTH` table in DynamoDB with an athlete_id as the key

### Backfill

##### Backfill Activities
- Create a new `ACTIVITIES` table in DynamoDB with `activity_id` as the key
- Under the `resources` folder in the `backfill` module, update the `backfill-activities-config.yml` config file
setting the `startEpoch` and `endEpoch` properties to the time window you wish to backfill 
    - Setting `endEpoch` to `0` will backfill up to current time
- Set the `perPage` property to indicate how many activities should be returned in each page from the Strava's 
activities API. 100 tended to give best results on a full backfill
- With config set, kick it off the backfill by executing `Main.backfillActivities` 

##### Backfill Power Streams
- Create a new `POWER` table in DynamoDB with `activity_id` as the key
- Under the `resources` folder in the `backfill` module, update the  `backfill-power-config.yml` config file setting the 
`overwriteExisting` boolean and `activities` list
    - Setting `overwriteExisting` to true will overwrite existing records in the power stream DynamoDB table, false will
    skip over all existing records
    - Backfill specific activities by specifying activity IDs in the `activities` list
    - Leave the `activities` list empty to backfill all activities in the `ACTIVITIES` table
- With config set, kick it off the backfill by executing `Main.backfillPower`

## Completed
- OAuth authentication with Strava
- Backfill of all historical Strava activities
- Backfill of all historical power streams

## WIP
- Ingest new and updated activities from Strava activities webhook
- Fetch power stream data for new activities
- Implement scrolling window averaging algorithm to calculate highest average power of various durations in power stream
- Backfill scrolling window average data for historical activities
- Implement scrolling window average for all new activities
- Calculate PRs in new activity power streams using historical power data
- Implement email notification mechanism (hopefully move to a simple Android app in phase 2)

## Technologies
- AWS DynamoDB
- AWS API Gateway
- AWS Lambda Functions
- OAuth 2.0
- Guice DI
- Mockito