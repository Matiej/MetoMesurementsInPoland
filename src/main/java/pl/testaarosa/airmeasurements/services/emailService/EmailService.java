package pl.testaarosa.airmeasurements.services.emailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.testaarosa.airmeasurements.domain.Mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static pl.testaarosa.airmeasurements.services.ConsolerData.*;

@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT =  new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(final Mail mail) {
        LOGGER.info(ANSI_BLUE + "Meteo is starting email preparation.." + ANSI_RESET);
        try {
            SimpleMailMessage mailMessage = getSimpleMailMessage(mail);
            javaMailSender.send(mailMessage);
            LOGGER.info(ANSI_BLUE + "Email has been sent to -> " + mail.getMailTo() + ANSI_RESET);
        } catch (MailException e) {
            LOGGER.error(ANSI_RED + "Error to process sending email to ->" + mail.getMailTo() + ANSI_RESET + e);
        }
    }

//    public void sendEmailWithReport(final Mail mail, final File report) {
//        LOGGER.info(ANSI_BLUE + "Meteo is starting email preparation.." + ANSI_RESET);
//        try {
//            MimeMessagePreparator mimeMessageHelper = getMimeMessageHelper(mail, report);
//            javaMailSender.send(mimeMessageHelper);
//            LOGGER.info(ANSI_BLUE + "Email has been sent to -> " + mail.getMailTo() + ANSI_RESET);
//        } catch (MailException e) {
//            LOGGER.error(ANSI_RED + "Error to process sending email to ->" + mail.getMailTo() + ANSI_RESET + e);
//        }
//    }

    public void sendEmailWithReport(final Mail mail, final File report) {
        LOGGER.info(ANSI_BLUE + "Meteo is starting email preparation.." + ANSI_RESET);
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = getMimeMessageHelper(message,mail, report);
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
            LOGGER.info(ANSI_BLUE + "Email has been sent to -> " + mail.getMailTo() + ANSI_RESET);
        } catch (MailException | MessagingException | UnsupportedEncodingException e) {
            LOGGER.error(ANSI_RED + "Error to process sending email to ->" + mail.getMailTo() + ANSI_RESET + e);
        }
    }

    private SimpleMailMessage getSimpleMailMessage(final Mail mail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mail.getMailTo());
        mailMessage.setSubject(mail.getSubject());
        mailMessage.setText(mail.getMessage());
        mailMessage.setFrom(mail.getFrom());
        try {
            mailMessage.setSentDate(SIMPLE_DATE_FORMAT.parse(mail.getSentDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mailMessage;
    }

    private MimeMessageHelper getMimeMessageHelper(final MimeMessage message, final Mail mail, final File report) throws MessagingException, UnsupportedEncodingException {
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(mail.getMailTo());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getMessage(), true);
        helper.setFrom("javamatiej@gmail.com", "Matiej MeteoCenter");
        try {
            helper.setSentDate(SIMPLE_DATE_FORMAT.parse(mail.getSentDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        helper.addAttachment(report.getName(), report);
        return helper;

//        MimeMessagePreparator mimeMessagePreparator = mimeMessage1 -> {
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage1, true);
//            helper.setTo(mail.getMailTo());
//            helper.setSubject(mail.getSubject());
//            helper.setText(mail.getMessage(), true);
//            helper.setFrom("javamatiej@gmail.com", "Matiej MeteoCenter");
//            helper.setSentDate(mail.getSentDate());
//            helper.addAttachment(report.getName(), report);
////            helper.setCc(new String[]{"znikenson@gmail.com"});
//        };
//        return mimeMessagePreparator;
    }
}
