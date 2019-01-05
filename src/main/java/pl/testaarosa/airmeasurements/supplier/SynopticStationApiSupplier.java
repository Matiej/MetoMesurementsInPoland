package pl.testaarosa.airmeasurements.supplier;

import org.springframework.stereotype.Service;

@Service
public class SynopticStationApiSupplier {
    public final static String ALL_SYNOPTIC_STATIONS_API_URL = "https://danepubliczne.imgw.pl/api/data/synop";
    public final static String SYNOPTIC_STATION_BY_CITY = "https://danepubliczne.imgw.pl/api/data/synop/station/";
}
