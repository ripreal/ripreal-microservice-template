package ru.sportmaster.esm.mail.configuration;

public class EmailServerProperties {
    private String appHost;
    private String smtpHost;
    private String sender;
    private String locale;

    public String getAppHost() {
        return "http://" + appHost;
    }

    public void setAppHost(String appHost) {
        this.appHost = appHost;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
