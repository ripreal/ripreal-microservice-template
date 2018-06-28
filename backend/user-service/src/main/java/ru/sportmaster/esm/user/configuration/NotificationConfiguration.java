package ru.sportmaster.esm.user.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import ru.sportmaster.esm.loyalty.clubpro.ClubProSmsTemplate;
import ru.sportmaster.esm.notification.NotificationService;
import ru.sportmaster.esm.notification.SmsTemplateResolver;

@Configuration
public class NotificationConfiguration {

    @Bean
    public SmsTemplateResolver smsTemplateResolver() {
        return ClubProSmsTemplate::valueOf;
    }
}
