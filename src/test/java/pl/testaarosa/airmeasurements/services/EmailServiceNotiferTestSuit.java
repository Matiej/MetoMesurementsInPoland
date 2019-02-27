package pl.testaarosa.airmeasurements.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.testaarosa.airmeasurements.domain.Mail;
import pl.testaarosa.airmeasurements.repositories.MockMailRepository;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationRepository;
import pl.testaarosa.airmeasurements.repositories.MockReportCreator;
import pl.testaarosa.airmeasurements.services.emailService.EmailContentBuilder;
import pl.testaarosa.airmeasurements.services.emailService.EmailNotifierServiceImpl;
import pl.testaarosa.airmeasurements.services.emailService.EmailService;

import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceNotiferTestSuit {

    @InjectMocks
    private EmailNotifierServiceImpl emailNotifierService;

    @Mock
    private EmailService emailService;

    @Mock
    private EmailContentBuilder emailContentBuilder;

    private MockMailRepository mockMailRepository;
    private MockMeasuringStationRepository mockMeasuringStationRepository;
    private MockReportCreator mockReportCreator;
    private final String SUBJECT = "some test subject";
    private final String MESSAGE = "some test message";

    @Before
    public void init() {
        mockMailRepository = new MockMailRepository();
        mockMeasuringStationRepository = new MockMeasuringStationRepository();
        mockReportCreator = new MockReportCreator();
    }

    @Test
    public void shouldSendEmailBeforeAddMeasuremetns() {
        //given
        String shortMessage = "first";
        Mail mail = mockMailRepository.mockMailBeforeMeasurement();
        //when
        emailNotifierService.sendEmailBeforeAddMeasuremetns(shortMessage);
        //then
//        verify(emailService).sendEmail(argThat(mail));
        verify(emailService, times(1)).sendEmail(mail);
        verifyNoMoreInteractions(emailService);
    }

    @Test
    public void shouldSendFirstErrorMail() {
        //given
        Mail mail = mockMailRepository.mockMail();
        //when
//        emailService.
    }

}
