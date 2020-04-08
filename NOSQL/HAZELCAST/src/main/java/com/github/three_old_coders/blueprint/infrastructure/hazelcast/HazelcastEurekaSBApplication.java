package com.github.three_old_coders.blueprint.infrastructure.hazelcast;

import com.hazelcast.config.CardinalityEstimatorConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.eureka.one.EurekaOneDiscoveryStrategyFactory;
import com.hazelcast.eureka.one.EurekaOneProperties;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = { "com.github.three_old_coders.blueprint.infrastructure.hazelcast.controller" })
public class HazelcastEurekaSBApplication
{
    public static final String TRUE = "true";
    public static final String HAZELCAST = "hazelcast";

    @Autowired private Environment _env;

    public static void main(final String[] args)
    {
        SpringApplication.run(HazelcastEurekaSBApplication.class, args);
    }


    @Bean
    public Config hazelcastConfig(final EurekaClient eurekaClient)
    {
        final String groupName = _env.getProperty(HAZELCAST + ".groupName");
        EurekaOneDiscoveryStrategyFactory.setGroupName(groupName);
        EurekaOneDiscoveryStrategyFactory.setEurekaClient(eurekaClient);

        final Config config = new Config();

        final JoinConfig join = config.getNetworkConfig().getJoin();
        join.getMulticastConfig().setEnabled(false);
        join.getEurekaConfig()
            .setEnabled(true)
            .setProperty(EurekaOneProperties.SELF_REGISTRATION.key(), TRUE)
            .setProperty(EurekaOneProperties.NAMESPACE.key(), HAZELCAST)
            .setProperty(EurekaOneProperties.SKIP_EUREKA_REGISTRATION_VERIFICATION.key(), TRUE)
            .setProperty(EurekaOneProperties.USE_METADATA_FOR_HOST_AND_PORT.key(), TRUE);

        final CardinalityEstimatorConfig cardinalityEstimatorConfig = config.getCardinalityEstimatorConfig(groupName);
        final Integer syncBackupCount = getProperty(_env, "syncBackupCount", Integer.class, HAZELCAST, groupName);
        if (null != syncBackupCount) {
            cardinalityEstimatorConfig.setBackupCount(syncBackupCount);
        }
        final Integer asyncBackupCount = getProperty(_env, "asyncBackupCount", Integer.class, HAZELCAST, groupName);
        if (null != asyncBackupCount) {
            cardinalityEstimatorConfig.setAsyncBackupCount(asyncBackupCount);
        }

        return config;
    }

    //
    //
    //

    /**
     * hazelcast.groupName = TEST-Cluster
     * hazelcast.syncBackupCount.TEST-Cluster = 0
     * hazelcast.ayncBackupCount = 1
     *
     * @param env
     * @param key
     * @param tClass
     * @param prefix
     * @param postfix   (optional)
     * @return
     */
    private <T> T getProperty(final Environment env, final String key, final Class<T> tClass, final String prefix, final String postfix)
    {
        T t;
        if (null != postfix) {
            if (null != (t = _env.getProperty(prefix + "." + key + "." + postfix, tClass))) {
                return t;
            }
        }

        return _env.getProperty(prefix + "." + key, tClass);
    }
}
