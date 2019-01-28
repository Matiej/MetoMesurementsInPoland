package pl.testaarosa.airmeasurements.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.testaarosa.airmeasurements.domain.Mail;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.dtoApi.MeasuringStationDto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class EmailNotifierServiceImpl implements EmailNotifierService{

    @Value("${notification.mail}")
    private String notifyMail;
    @Value("${from.mail}")
    private String fromMail;
    @Autowired
    private EmailService emailService;
    private final static Date date = new Date();

    @Override
    public void sendEmailBeforAddMeasuremetns(String shortMessage) {
        String subject = "Starting METEO measurements " + LocalDateTime.now();
        String message = "\n Scheduler is starting "+shortMessage +" try to download of measurements for Testaaarosa METO and AIR stations.";
        emailService.sendEmail(new Mail(notifyMail, subject, message, fromMail, date));
    }

    @Override
    public void sendFirstErrorMail(String error) {
        String subject = "Error METEO measurements " + LocalDateTime.now();
        StringBuilder message = new StringBuilder();
        message.append("\n Can't download of measurements for Testaaarosa METO and AIR stations because of some error.\n" +
                " Scheduler will try once again it 10minutes. Below log error report: \n");
        message.append("\n  REPORT -> \n"+error);

        emailService.sendEmail(new Mail(notifyMail, subject, message.toString(), fromMail, date));
    }


    @Override
    public void sendSecondErrorMail(String error) {
        String subject = "Error METEO measurements " + LocalDateTime.now();
        StringBuilder message = new StringBuilder();
        message.append("\n Can't download of measurements second try for Testaaarosa METO and AIR stations because of some error.\n" +
                " Scheduler will try once again it 8 hours. Below log error report: \n");
        message.append("\n + REPORT -> \n"+error);

        emailService.sendEmail(new Mail(notifyMail, subject, message.toString(), fromMail, date));
    }

    public String sendEmailAfterDownloadMeasurements(List<MeasuringStationDto> mSDtoList, String measurementTime) {
        String subject = "Meteo download status success.";
        String messageHead = "Measured time of downloading data: " + measurementTime + " minutes\n" + mSDtoList.size()
                + " measurements added to data base correct.\n  LIST OF STATIONS FOR WHICH MEASUREMENTS WERE TAKEN:\n";
        StringBuilder messeage = new StringBuilder();
        messeage.append(messageHead);
        mSDtoList.forEach(m-> {
            messeage.append("Station id: " + m.getId() + ", Latiude: "+m.getGegrLat() + ", Longitude: " + m.getGegrLon() +",\n");
            messeage.append("name: " + m.getStationName()+", address: " + m.getAddressStreet() + ", city: "
                    + m.getCityDto().getCityName() +"\n");
        });
        emailService.sendEmail(new Mail(notifyMail, subject, messeage.toString(), fromMail, date));
        return messeage.toString();
    }

    public String sendEmailAfterDownloadMeasurementsN(List<MeasuringStation> stationList, String[] shortMess) {
        String subject = "Meteo download status success.";
        String messageHead = "Measured time of downloading data: " + shortMess[2] + " minutes\n" + shortMess[0]
                + " air measurements, " +shortMess[1] + " synoptic measurementes added to data base correct."
                + "\n  LIST OF STATIONS FOR WHICH MEASUREMENTS WERE TAKEN:\n";
        StringBuilder messeage = new StringBuilder();
        messeage.append(messageHead);
        stationList.forEach(m-> {
            messeage.append("Station id: " + m.getId() + ", Latiude: "+m.getLatitude() + ", Longitude: " + m.getLatitude() +",\n");
            messeage.append("name: " + m.getStationName()+", address: " + m.getStreet() + ", city: "
                    + m.getCity() +"\n");
        });
        emailService.sendEmail(new Mail(notifyMail, subject, messeage.toString(), fromMail, date));
        return messeage.toString();
    }

}
