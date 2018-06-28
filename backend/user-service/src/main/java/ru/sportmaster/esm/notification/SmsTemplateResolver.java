package ru.sportmaster.esm.notification;

@FunctionalInterface
public interface SmsTemplateResolver {

    SmsTemplate resolve(String templateCode);
}
