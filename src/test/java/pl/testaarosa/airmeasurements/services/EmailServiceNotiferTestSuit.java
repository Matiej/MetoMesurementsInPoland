package pl.testaarosa.airmeasurements.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.testaarosa.airmeasurements.domain.Mail;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.repositories.MockMailRepository;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationRepository;
import pl.testaarosa.airmeasurements.repositories.MockReportCreator;
import pl.testaarosa.airmeasurements.services.emailService.EmailContentBuilder;
import pl.testaarosa.airmeasurements.services.emailService.EmailNotifierServiceImpl;
import pl.testaarosa.airmeasurements.services.emailService.EmailService;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private final String ERROR = "Exception test message";
    private static final String PATH = new File(System.getProperty("user.dir")
            + File.separator + "src/test/java/pl/testaarosa/airmeasurements/repositories/mockReport").getAbsolutePath();
    private static final String NAME_CONST = "ReportTEST.xls";

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
        verify(emailService, times(1)).sendEmail(mail);
        verifyNoMoreInteractions(emailService);
    }

    @Test
    public void shouldSendFirstErrorMail() {
        //given
        Mail mail = mockMailRepository.mockFirstErrorMail(ERROR);
        //when
        emailNotifierService.sendFirstErrorMail(ERROR);
        //then
        verify(emailService, times(1)).sendEmail(mail);
        verifyNoMoreInteractions(emailService);
    }

    @Test
    public void shouldSendSecondErrorMail() {
        //given
        Mail mail = mockMailRepository.mockSecondErrorMail(ERROR);
        //when
        emailNotifierService.sendSecondErrorMail(ERROR);
        //then
        verify(emailService, times(1)).sendEmail(mail);
        verifyNoMoreInteractions(emailService);
    }

    @Test
    public void shouldSendEmailAfterDownloadMeasurementsN(){
        //given
        List<MeasuringStation> stations = mockMeasuringStationRepository.stations();
        String[] shortMess = new String[]{"1","2","3"};
        Mail mail = mockMailRepository.mockAfterDownloadMeasurementNMail(stations, shortMess);
        //when
        String s = emailNotifierService.sendEmailAfterDownloadMeasurementsN(stations, shortMess);
        //then
        assertEquals(mail.getMessage(), s);
        verify(emailService, times(1)).sendEmail(mail);
        verifyNoMoreInteractions(emailService);
    }

    @Test
    public void shouldSendEmailAfterDownloadMeasurementsWithReport() {
        //given
        String deleteMessage = "Delete test message";
        String[] shortMess = new String[]{"1","2","3"};
        File file = mockReportCreator.createTestFileReport(NAME_CONST, PATH);
        Mail mail = mockMailRepository.mockAfterDownloadMeasurementsWithReportMail(file, shortMess, deleteMessage);
        String mailMessage = mail.getMessage();
        Map model = new HashMap();
        model.put("reportMessage", mail.getMessage());
        model.put("delMessage", "Delete test message");
        TemplateEngine templateEngine = new TemplateEngine();
        Context context = new Context();
        //when
        context.setVariables(model);
        String mailTemplate = templateEngine.process("mailTemplate", context);
        mail.setMessage(mailTemplate);
        when(emailContentBuilder.multiVarBuilder(model)).thenReturn(mailTemplate);
        String s = emailNotifierService.sendEmailAfterDownloadMeasurementsWithReport(file, shortMess, deleteMessage);
        //then
        assertEquals(mailMessage,s);
        verify(emailService, times(1)).sendEmailWithReport(mail,file);
        verifyNoMoreInteractions(emailService);
    }

}
