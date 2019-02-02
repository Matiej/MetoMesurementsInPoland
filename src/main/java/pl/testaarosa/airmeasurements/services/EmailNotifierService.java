package pl.testaarosa.airmeasurements.services;

import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.dtoApi.MeasuringStationDto;

import java.util.List;

public interface EmailNotifierService {

    void sendEmailBeforAddMeasuremetns(String shortMessage);
    void sendFirstErrorMail(String error);
    void sendSecondErrorMail(String error);
    String sendEmailAfterDownloadMeasurementsN(List<MeasuringStation> mSDtoList, String[] shortMess);
}
