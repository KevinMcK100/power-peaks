package com.kmk.powerpeaks.strava.auth.guice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.kmk.powerpeaks.strava.auth.config.AuthConfig;
import com.kmk.powerpeaks.strava.auth.config.AwsConfig;
import com.kmk.powerpeaks.strava.auth.dao.AuthDataStoreDao;
import com.kmk.powerpeaks.strava.auth.dao.AuthDynamoDbDao;
import com.kmk.powerpeaks.strava.auth.http.AuthHttpClient;
import com.kmk.powerpeaks.strava.auth.http.StravaAuthHttpClient;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthModule extends AbstractModule {

    private static final Logger LOGGER = Logger.getLogger(AuthModule.class.getName());

    private static final String AUTH_CONFIG_FILE = "auth-config.yml";
    private static final String AWS_CONFIG_FILE = "aws-config.yml";

    private static final String MISSING_AUTH_CREDENTIALS_LOG =
            String.format("Unable to read config file '%s'", AUTH_CONFIG_FILE);
    private static final String MISSING_AWS_PROFILE_LOG = String.format("Unable to read AWS profile from '%s'." +
            " Proceeding with default AWS profile.", AWS_CONFIG_FILE);
    private static final String MISSING_AWS_REGION_LOG = String.format("Unable to read AWS region from '%s'." +
            " Proceeding with default AWS region.", AWS_CONFIG_FILE);


    @Override
    protected void configure() {

        bind(AuthHttpClient.class).to(StravaAuthHttpClient.class);
        bind(AuthDataStoreDao.class).to(AuthDynamoDbDao.class);
    }

    @Provides
    AuthConfig provideAuthConfig() {

        File configFile = getYamlFile(AUTH_CONFIG_FILE);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            return Optional.ofNullable(mapper.readValue(configFile, AuthConfig.class)).orElseGet(AuthConfig::new);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, MISSING_AUTH_CREDENTIALS_LOG, e);
            return new AuthConfig();
        }
    }

    @Provides
    DynamoDbEnhancedClient provideDynamoDbClient() {

        File configFile = getYamlFile(AWS_CONFIG_FILE);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Optional<AwsConfig> awsConfig = Optional.empty();

        try {
            awsConfig = Optional.ofNullable(mapper.readValue(configFile, AwsConfig.class));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, MISSING_AUTH_CREDENTIALS_LOG, e);
        }

        ProfileCredentialsProvider.Builder profileCredsBuilder = ProfileCredentialsProvider.builder();
        awsConfig.flatMap(AwsConfig::getAwsProfile)
                 .ifPresentOrElse(profileCredsBuilder::profileName,
                                  () -> LOGGER.log(Level.WARNING, MISSING_AWS_PROFILE_LOG));

        DynamoDbClientBuilder ddbClientBuilder = DynamoDbClient.builder();
        awsConfig.flatMap(AwsConfig::getAwsRegion)
                 .map(Region::of)
                 .ifPresentOrElse(ddbClientBuilder::region,
                                  () -> LOGGER.log(Level.WARNING, MISSING_AWS_REGION_LOG));

        ddbClientBuilder.credentialsProvider(profileCredsBuilder.build());

        return DynamoDbEnhancedClient.builder().dynamoDbClient(ddbClientBuilder.build()).build();
    }

    private File getYamlFile(String fileName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
    }
}
