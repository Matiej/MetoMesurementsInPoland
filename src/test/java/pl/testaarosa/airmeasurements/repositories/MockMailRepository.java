package pl.testaarosa.airmeasurements.repositories;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.testaarosa.airmeasurements.domain.Mail;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.services.emailService.EmailContentBuilder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockMailRepository {

    private final static String DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());


    public Mail mockMail() {
        Mail mail = new Mail("test@unitTest.com","method test", "Here we are testing something",
                "From@from.com", DATE);
        return mail;
    }

    public Mail mockMailBeforeMeasurement() {
        String shortMessage = "first";
        String subject = "Starting METEO measurements " + LocalDateTime.now().withNano(0);
        String message = "\n Scheduler is starting "+shortMessage +" try to download of measurements for Testaaarosa METEO and AIR stations.";
        return new Mail(null, subject, message, null, DATE);
    }

    public Mail mockFirstErrorMail(String error) {
        String subject = "Error METEO measurements " + LocalDateTime.now().withNano(0);
        StringBuilder message = new StringBuilder();
        message.append("\n Can't download of measurements for Testaaarosa METEO and AIR stations because of some error.\n" +
                " Scheduler will try once again it 10minutes. Below log error report: \n");
        message.append("\n  REPORT -> \n"+error);
        return new Mail(null, subject, message.toString(), null, DATE);
    }

    public Mail mockSecondErrorMail(String error) {
        String subject = "Error METEO measurements " + LocalDateTime.now().withNano(0);
        StringBuilder message = new StringBuilder();
        message.append("\n Can't download of measurements second try for Testaaarosa METEO and AIR stations because of some error.\n" +
                " Scheduler will try once again it 8 hours. Below log error report: \n");
        message.append("\n + REPORT -> \n"+error);
        return new Mail(null, subject, message.toString(), null, DATE);
    }

    public Mail mockAfterDownloadMeasurementNMail(List<MeasuringStation> stationList, String[] shortMess) {
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
        return new Mail(null, subject, messeage.toString(), null, DATE);
    }

    public Mail mockAfterDownloadMeasurementsWithReportMail(File file, String[] shortMess, String deleteMessage) {
        TemplateEngine templateEngine = new TemplateEngine();
        Context context = new Context();
        String subject = "Matiej Meteo data download status success.";
        String messageHead = "Measured time of downloading: " + shortMess[2] + " minutes\n , " + shortMess[0]
                + " air measurements, " +shortMess[1] + " synoptic measurements added to data base correct."
                + "\nDownloaded data details you can find in attached file: " +
                "\n-> "+file.getName();
        return new Mail(null, subject, messageHead, null, DATE);
    }
}
