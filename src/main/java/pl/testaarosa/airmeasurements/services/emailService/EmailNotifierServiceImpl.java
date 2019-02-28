package pl.testaarosa.airmeasurements.services.emailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.testaarosa.airmeasurements.domain.Mail;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailNotifierServiceImpl implements EmailNotifierService{

    @Value("${notification.mail}")
    private String notifyMail;
    @Value("${from.mail}")
    private String fromMail;
    private final static String DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
    private final EmailService emailService;
    private final EmailContentBuilder emailContentBuilder;

    @Autowired
    public EmailNotifierServiceImpl(EmailService emailService, EmailContentBuilder emailContentBuilder) {
        this.emailService = emailService;
        this.emailContentBuilder = emailContentBuilder;
    }

    @Override
    public void sendEmailBeforeAddMeasuremetns(String shortMessage) {
        String subject = "Starting METEO measurements " + LocalDateTime.now().withNano(0);
        String message = "\n Scheduler is starting "+shortMessage +" try to download of measurements for Testaaarosa METEO and AIR stations.";
        emailService.sendEmail(new Mail(notifyMail, subject, message, fromMail, DATE));
    }

    @Override
    public void sendFirstErrorMail(String error) {
        String subject = "Error METEO measurements " + LocalDateTime.now().withNano(0);
        StringBuilder message = new StringBuilder();
        message.append("\n Can't download of measurements for Testaaarosa METEO and AIR stations because of some error.\n" +
                " Scheduler will try once again it 10minutes. Below log error report: \n");
        message.append("\n  REPORT -> \n"+error);

        emailService.sendEmail(new Mail(notifyMail, subject, message.toString(), fromMail, DATE));
    }

    @Override
    public void sendSecondErrorMail(String error) {
        String subject = "Error METEO measurements " + LocalDateTime.now().withNano(0);
        StringBuilder message = new StringBuilder();
        message.append("\n Can't download of measurements second try for Testaaarosa METEO and AIR stations because of some error.\n" +
                " Scheduler will try once again it 8 hours. Below log error report: \n");
        message.append("\n + REPORT -> \n"+error);
        emailService.sendEmail(new Mail(notifyMail, subject, message.toString(), fromMail, DATE));
    }

    @Override
    public String sendEmailAfterDownloadMeasurementsN(List<MeasuringStation> stationList, String[] shortMess) {
        String subject = "Meteo download status success.";
        String messageHead = "Measured time of downloading data: " + shortMess[2] + " minutes\n" + shortMess[1]
                + " air measurements, " +shortMess[0] + " synoptic measurements added to data base correct."
                + "\n  LIST OF STATIONS FOR WHICH MEASUREMENTS WERE TAKEN:\n";
        StringBuilder messeage = new StringBuilder();
        messeage.append(messageHead);
        stationList.forEach(m-> {
            messeage.append("Station id: " + m.getId() + ", Latiude: "+m.getLatitude() + ", Longitude: " + m.getLatitude() +",\n");
            messeage.append("name: " + m.getStationName()+", address: " + m.getStreet() + ", city: "
                    + m.getCity() +"\n");
        });
        emailService.sendEmail(new Mail(notifyMail, subject, messeage.toString(), fromMail, DATE));
        return messeage.toString();
    }

    @Override
    public synchronized String sendEmailAfterDownloadMeasurementsWithReport(File file, String[] shortMess, String delReport) {
        String subject = "Matiej Meteo data download status success.";
        String messageHead = "Measured time of downloading: " + shortMess[2] + " minutes\n , " + shortMess[0]
                + " air measurements, " +shortMess[1] + " synoptic measurements added to data base correct."
                + "\nDownloaded data details you can find in attached file: " +
                "\n-> "+file.getName();
        Map model = new HashMap();
        model.put("reportMessage", messageHead);
        model.put("delMessage", delReport);
        String content = emailContentBuilder.multiVarBuilder(model);
        emailService.sendEmailWithReport(new Mail(notifyMail, subject, content, fromMail, DATE), file);
        return messageHead;
    }

}
