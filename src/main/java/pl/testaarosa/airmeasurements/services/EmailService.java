package pl.testaarosa.airmeasurements.services;

import pl.testaarosa.airmeasurements.domain.Mail;

public interface EmailService {

    public void sendEmail(final Mail mail);
}
