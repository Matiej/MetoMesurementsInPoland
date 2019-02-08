package pl.testaarosa.airmeasurements.services.emailService;

import pl.testaarosa.airmeasurements.domain.MeasuringStation;

import java.io.File;
import java.util.List;

public interface EmailNotifierService {

    void sendEmailBeforAddMeasuremetns(String shortMessage);
    void sendFirstErrorMail(String error);
    void sendSecondErrorMail(String error);
    String sendEmailAfterDownloadMeasurementsN(List<MeasuringStation> mSDtoList, String[] shortMess);
    String sendEmailAfterDownloadMeasurementsWithReport(File file, String[] shortMess);
}
