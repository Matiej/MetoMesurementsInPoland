package pl.testaarosa.airmeasurements.services.reportService;

import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;

import java.io.File;
import java.util.LinkedHashMap;

public interface WorkBookReportService {


    File createXMLAddAllMeasurementsReport(LinkedHashMap<String, SynopticMeasurement> synopticMeasurementMap,
                                           LinkedHashMap<MeasuringStation, AirMeasurement> mStResponseMap) throws Exception;

    String delOldReports();
}
