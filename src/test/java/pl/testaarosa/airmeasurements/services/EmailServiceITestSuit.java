package pl.testaarosa.airmeasurements.services;

import org.junit.After;
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
import org.springframework.mail.javamail.MimeMessagePreparator;
import pl.testaarosa.airmeasurements.domain.Mail;
import pl.testaarosa.airmeasurements.repositories.MockMailRepository;
import pl.testaarosa.airmeasurements.services.emailService.EmailService;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
//        Mail mail = new Mail("test@unitTest.com","method test", "Here we are testing something",
//                "From@from.com", new Date());
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
        EmailService underTest = new EmailService();
        JavaMailSender js = new JavaMailSenderImpl();
        MimeMessage mimeMessage = js.createMimeMessage();
        File testFileReport = mockReportCreator.createTestFileReport();
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
