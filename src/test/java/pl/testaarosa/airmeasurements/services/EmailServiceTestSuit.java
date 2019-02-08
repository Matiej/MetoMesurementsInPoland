package pl.testaarosa.airmeasurements.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import pl.testaarosa.airmeasurements.domain.Mail;
import pl.testaarosa.airmeasurements.services.emailService.EmailServiceImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTestSuit {

    @InjectMocks
    private EmailServiceImpl emailService;
    @Mock
    private JavaMailSender javaMailSender;

    @Test
    public void shouldSendEmail() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        EmailServiceImpl underTest = new EmailServiceImpl();
        Mail mail = new Mail("test@unitTest.com","method test", "Here we are testing something",
                "From@from.com", new Date());
        Method getSimpleMailMessage = underTest.getClass().getDeclaredMethod("getSimpleMailMessage", Mail.class);
        getSimpleMailMessage.setAccessible(true);
        SimpleMailMessage result = (SimpleMailMessage)getSimpleMailMessage.invoke(underTest, mail);
        //when
        emailService.sendEmail(mail);
        //then
        verify(javaMailSender, times(1)).send(result);
        verifyNoMoreInteractions(javaMailSender);
    }
}
