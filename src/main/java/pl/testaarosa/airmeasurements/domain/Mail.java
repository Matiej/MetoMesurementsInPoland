package pl.testaarosa.airmeasurements.domain;

public class Mail {

    private String mailTo;
    private String subject;
    private String message;
    private String toCc;
    private String from;

    public Mail() {
    }

    public Mail(String mailTo, String subject, String message, String from) {
        this.mailTo = mailTo;
        this.subject = subject;
        this.message = message;
        this.from = from;
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
}
