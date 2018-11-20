package pl.testaarosa.airmeasurements.services;

import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.measurementsdto.MeasuringStationDto;

import java.util.List;

public interface EmailNotifierService {

    void sendEmailBeforAddMeasuremetns(String shortMessage);
    void sendFirstErrorMail(String error);
    void sendSecondErrorMail(String error);
    String sendEmailAfterDownloadMeasurements(List<MeasuringStationDto> mSDtoList, String measurementTime);
    String sendEmailAfterDownloadMeasurementsN(List<MeasuringStation> mSDtoList, String[] shortMess);
}
