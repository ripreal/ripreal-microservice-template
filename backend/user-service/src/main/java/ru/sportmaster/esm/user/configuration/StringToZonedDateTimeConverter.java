package ru.sportmaster.esm.user.configuration;

import org.springframework.core.convert.converter.Converter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class StringToZonedDateTimeConverter implements Converter<ZonedDateTime, String> {
    @Override
    public String convert(ZonedDateTime source) {
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(source);
    }
}
