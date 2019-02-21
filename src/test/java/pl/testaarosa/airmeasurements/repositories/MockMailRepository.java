package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.Mail;

import java.util.Date;

public class MockMailRepository {

    public Mail mockMail() {
        Mail mail = new Mail("test@unitTest.com","method test", "Here we are testing something",
                "From@from.com", new Date());

        return mail;
    }
}
