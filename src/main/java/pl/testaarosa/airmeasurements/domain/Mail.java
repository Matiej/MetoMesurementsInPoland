package pl.testaarosa.airmeasurements.domain;

import java.util.Date;
import java.util.Objects;

public class Mail {

    private String mailTo;
    private String subject;
    private String message;
    private String toCc;
    private String from;
    private Date sentDate;

    public Mail() {
    }

    public Mail(String mailTo, String subject, String message, String from, Date sentdate) {
        this.mailTo = mailTo;
        this.subject = subject;
        this.message = message;
        this.from = from;
        this.sentDate = sentdate;
    }

    public Mail(String mailTo, String subject, String message, String toCc, String from) {
        this.mailTo = mailTo;
        this.subject = subject;
        this.message = message;
        this.toCc = toCc;
        this.from = from;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToCc() {
        return toCc;
    }

    public void setToCc(String toCc) {
        this.toCc = toCc;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mail mail = (Mail) o;
        return Objects.equals(mailTo, mail.mailTo) &&
                Objects.equals(subject, mail.subject) &&
                Objects.equals(message, mail.message) &&
                Objects.equals(toCc, mail.toCc) &&
                Objects.equals(from, mail.from) &&
                Objects.equals(sentDate, mail.sentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mailTo, subject, message, toCc, from, sentDate);
    }
}
