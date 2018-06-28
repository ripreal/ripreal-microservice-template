package ru.sportmaster.esm.mail;

public class EmailInfo {
    private String to;
    private String from;
    private String subject;
    private String content;

    public EmailInfo(String to, String from, String subject, String content) {
        this.to = to;
        this.from = from;
        this.subject = subject;
        this.content = content;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }
}
