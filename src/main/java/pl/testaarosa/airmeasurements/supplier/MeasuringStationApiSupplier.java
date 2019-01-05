package pl.testaarosa.airmeasurements.supplier;

import org.springframework.stereotype.Service;

@Service
public class MeasuringStationApiSupplier {
    public final static String ALL_MEASURING_STATIONS_API_URL = "http://api.gios.gov.pl/pjp-api/rest/station/findAll";
    public final static String MEASRURING_STATION_API_URL_BY_ID = "http://api.gios.gov.pl/pjp-api/rest/aqindex/getIndex/";
}
