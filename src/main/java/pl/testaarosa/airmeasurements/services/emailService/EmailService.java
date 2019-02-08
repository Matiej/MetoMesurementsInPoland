package pl.testaarosa.airmeasurements.services.emailService;

import pl.testaarosa.airmeasurements.domain.Mail;

import java.io.File;

public interface EmailService {

    void sendEmail(final Mail mail);
    void sendEmailWithReport(final Mail mail, final File report);
}
