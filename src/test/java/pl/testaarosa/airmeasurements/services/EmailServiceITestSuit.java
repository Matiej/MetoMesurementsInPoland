package pl.testaarosa.airmeasurements.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import pl.testaarosa.airmeasurements.domain.Mail;
import pl.testaarosa.airmeasurements.repositories.MockMailRepository;
import pl.testaarosa.airmeasurements.repositories.MockReportCreator;
import pl.testaarosa.airmeasurements.services.emailService.EmailService;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceITestSuit {

    @InjectMocks
    private EmailService emailService;
    @Mock
    private JavaMailSender javaMailSender;

    private MockMailRepository mockMailRepository;
    private MockReportCreator mockReportCreator;
    private static final String PATH = new File(System.getProperty("user.dir")
            + File.separator + "src/test/java/pl/testaarosa/airmeasurements/repositories/mockReport").getAbsolutePath();
    private static final String NAME_CONST = "_addAllMeasurementsReportTEST.xls";

    @Before
    public void init() {
        mockMailRepository = new MockMailRepository();
        mockReportCreator = new MockReportCreator();
    }

    @Test
    public void shouldSendEmail() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        EmailService underTest = new EmailService();
        Mail mail1 = mockMailRepository.mockMail();
        Method getSimpleMailMessage = underTest.getClass().getDeclaredMethod("getSimpleMailMessage", Mail.class);
        getSimpleMailMessage.setAccessible(true);
        SimpleMailMessage result = (SimpleMailMessage)getSimpleMailMessage.invoke(underTest, mail1);
        //when
        emailService.sendEmail(mail1);
        //then
        verify(javaMailSender, times(1)).send(result);
        verifyNoMoreInteractions(javaMailSender);
    }

    @Test
    public void shouldSendEmailWithReport() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        JavaMailSender js = new JavaMailSenderImpl();
        MimeMessage mimeMessage = js.createMimeMessage();
        String reportFileName = LocalDateTime.now().withNano(0).toString().replaceAll(":", "-") + NAME_CONST;
        File testFileReport = mockReportCreator.createTestFileReport(reportFileName, PATH);
        Mail mail1 = mockMailRepository.mockMail();
        Method getMimeMessageHelper = emailService.getClass().getDeclaredMethod("getMimeMessageHelper", MimeMessage.class,Mail.class, File.class);
        getMimeMessageHelper.setAccessible(true);
        MimeMessageHelper result = (MimeMessageHelper) getMimeMessageHelper.invoke(emailService,mimeMessage, mail1, testFileReport);
        //when
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        emailService.sendEmailWithReport(mail1, testFileReport);
        //then
        verify(javaMailSender, times(1)).send(result.getMimeMessage());
        verify(javaMailSender, times(1)).createMimeMessage();
        verifyNoMoreInteractions(javaMailSender);
        mockReportCreator.delTestFileReport(testFileReport);
    }

}
