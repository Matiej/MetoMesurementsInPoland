package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.Mail;

import java.time.LocalDateTime;
import java.util.Date;

public class MockMailRepository {

    public Mail mockMail() {
        Mail mail = new Mail("test@unitTest.com","method test", "Here we are testing something",
                "From@from.com", new Date().toString());

        return mail;
    }

    public Mail mockMailBeforeMeasurement() {
        String shortMessage = "first";
        String subject = "Starting METEO measurements " + LocalDateTime.now().withNano(0);
        String message = "\n Scheduler is starting "+shortMessage +" try to download of measurements for Testaaarosa METO and AIR stations.";
        return new Mail(null, subject, message, null, new Date().toString());
    }
}
