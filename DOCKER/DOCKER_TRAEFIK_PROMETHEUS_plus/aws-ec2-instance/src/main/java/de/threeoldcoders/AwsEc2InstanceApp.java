package de.threeoldcoders;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import static java.util.Objects.requireNonNull;

public class AwsEc2InstanceApp {
    @Getter
    @Accessors(prefix = "_")
    @Builder
    static public class AppConfiguration {
        private final String _accountId;
        private final String _region;
        private final String _environmentName;
        private final String _keyPairName;
    }

    public static void main(final String[] args) {
        final App app = new App();
        final AppConfiguration.AppConfigurationBuilder appConfigBuilder=AppConfiguration.builder();

        final String accountId = (String) app
                .getNode()
                .tryGetContext("accountId");
        requireNonNull(accountId, "context variable 'accountId' must not be null");
        appConfigBuilder.accountId(accountId);

        final String region = (String) app
                .getNode()
                .tryGetContext("region");
        requireNonNull(region, "context variable 'region' must not be null");
        appConfigBuilder.region(region);

        final String environmentName = (String) app
                .getNode()
                .tryGetContext("environmentName");
        requireNonNull(environmentName, "context variable 'environmentName' must not be null");
        appConfigBuilder.environmentName(environmentName);

        final String keyPairName = (String) app
                .getNode()
                .tryGetContext("keyPairName");
        requireNonNull(keyPairName, "context variable 'keyPairName' must not be null");
        final AppConfiguration ac = appConfigBuilder.keyPairName(keyPairName).build();

        new AwsEc2InstanceStack(app, ac.getEnvironmentName()+"-AwsEc2InstanceStack", StackProps.builder()
                // For more information, see https://docs.aws.amazon.com/cdk/latest/guide/environments.html
                .env(buildEnv(ac))
                .build(),ac);

        app.synth();
    }

    static Environment buildEnv(final AppConfiguration ac) {
        return Environment.builder()
                .account(ac.getAccountId())
                .region(ac.getRegion())
                .build();
    }
}

