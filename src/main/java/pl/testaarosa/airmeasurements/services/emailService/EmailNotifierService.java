package pl.testaarosa.airmeasurements.services.emailService;

import pl.testaarosa.airmeasurements.domain.MeasuringStation;

import java.io.File;
import java.util.List;

public interface EmailNotifierService {

    void sendEmailBeforeAddMeasuremetns(String shortMessage);
    void sendFirstErrorMail(String error);
    void sendSecondErrorMail(String error);
    String sendEmailAfterDownloadMeasurementsN(List<MeasuringStation> stationList, String[] shortMess);
    String sendEmailAfterDownloadMeasurementsWithReport(File file, String[] shortMess, String delReport);
}
