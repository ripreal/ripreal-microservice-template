package ru.sportmaster.esm.confirmation.configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.sportmaster.esm.mail.configuration.EmailServerProperties;

@Configuration
public class ConfirmationConfiguration {

    @Value("${primary-locale}")
    private String locale;

    @Bean
    @RefreshScope
    public SMSConfirmationProperties smsConfirmationProperties(Environment environment) {
        Binder binder = Binder.get(environment);
        return binder.bind("sms-confirmation", SMSConfirmationProperties.class).get();
    }

    @Bean
    @RefreshScope
    public EmailConfirmationProperties emailConfirmationProperties(Environment environment) {
        Binder binder = Binder.get(environment);
        EmailConfirmationProperties props = binder.bind("email-confirmation", EmailConfirmationProperties.class).get();
        return props;
    }

    @Bean
    public HazelcastInstance hazelcastInstance(MongoOperations mongoOperations, SMSConfirmationProperties smsProps,
                                               EmailConfirmationProperties emailProps) {
        MulticastConfig multicastConfig = new MulticastConfig();
        multicastConfig.setEnabled(false);

        JoinConfig joinConfig = new JoinConfig();
        joinConfig.setMulticastConfig(multicastConfig);

        NetworkConfig networkConfig = new NetworkConfig();
        networkConfig.setJoin(joinConfig);

        Config config = new Config();
        config.setNetworkConfig(networkConfig);

        return Hazelcast.newHazelcastInstance(config);
    }
}
