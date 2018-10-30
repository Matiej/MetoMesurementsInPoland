package pl.testaarosa.airmeasurements.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.testaarosa.airmeasurements.domain.Mail;
import pl.testaarosa.airmeasurements.domain.measurementsdto.MeasuringStationDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmailNotifierServiceImpl implements EmailNotifierService{

    @Value("${notification.mail}")
    private String notifyMail;
    @Value("${from.mail}")
    private String fromMail;
    @Autowired
    private EmailService emailService;

    @Override
    public void sendEmailBeforAddMeasuremetns(String shortMessage) {
        String subject = "Starting METEO measurements " + LocalDateTime.now();
        String message = "\n Scheduler is starting "+shortMessage +" try to download of measurements for Testaaarosa METO and AIR stations.";
        emailService.sendEmail(new Mail(notifyMail, subject, message, fromMail));
    }

    @Override
    public void sendFirstErrorMail(String error) {
        String subject = "Error METEO measurements " + LocalDateTime.now();
        StringBuilder message = new StringBuilder();
        message.append("\n Can't download of measurements for Testaaarosa METO and AIR stations because of some error.\n" +
                " Scheduler will try once again it 10minutes. Below log error report: \n");
        message.append("\n + REPORT -> \n"+error);

        emailService.sendEmail(new Mail(notifyMail, subject, message.toString(), fromMail));
    }


    @Override
    public void sendSecondErrorMail(String error) {
        String subject = "Error METEO measurements " + LocalDateTime.now();
        StringBuilder message = new StringBuilder();
        message.append("\n Can't download of measurements second try for Testaaarosa METO and AIR stations because of some error.\n" +
                " Scheduler will try once again it 8 hours. Below log error report: \n");
        message.append("\n + REPORT -> \n"+error);

        emailService.sendEmail(new Mail(notifyMail, subject, message.toString(), fromMail));
    }

    public String sendEmailAfterDownloadMeasurements(List<MeasuringStationDto> mSDtoList, String measurementTime) {
        String subject = "Meteo download status success.";
        String messageHead = "Measured time of downloading data: " + measurementTime + " minutes\n" + mSDtoList.size()
                + " measurements added to data base correct.\n + LIST OF STATIONS FOR WHICH MEASUREMENTS WERE TAKEK:+\n";
        StringBuilder messeage = new StringBuilder();
        messeage.append(messageHead);
        mSDtoList.forEach(m-> {
            messeage.append("Station id: " + m.getId() + ", Latiude: "+m.getGegrLat() + ", Longitude: " + m.getGegrLon() +",\n");
            messeage.append("name: " + m.getStationName()+", address: " + m.getAddressStreet() + ", city: " + m.getCityDto().getCityName());
        });
        emailService.sendEmail(new Mail(notifyMail, subject, messeage.toString(), fromMail));
        return messeage.toString();
    }

}
