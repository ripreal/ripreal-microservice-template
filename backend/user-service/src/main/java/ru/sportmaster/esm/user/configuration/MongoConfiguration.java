package ru.sportmaster.esm.user.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableReactiveMongoRepositories(basePackages = {"ru.sportmaster.esm.user.dao",
        "ru.sportmaster.esm.confirmation.repository"})
public class MongoConfiguration {
    @Bean
    MongoCustomConversions mongoCustomConversions() {
        List<Converter<?, ?>> conversions = new ArrayList<>();
        conversions.add(new StringToZonedDateTimeConverter());
        conversions.add(new ZonedDateTimeToStringConverter());
        return new MongoCustomConversions(conversions);
    }
}
