package pl.testaarosa.airmeasurements.supplier;

import org.springframework.stereotype.Service;

@Service
public class MeasuringStationApiSupplier {
    public final static String allMeasuringStationsApi = "http://api.gios.gov.pl/pjp-api/rest/station/findAll";
    public final static String measurementsAdi = "http://api.gios.gov.pl/pjp-api/rest/aqindex/getIndex/";
}
