package pl.testaarosa.airmeasurements.services;

import org.springframework.web.client.RestClientException;
import pl.testaarosa.airmeasurements.domain.MeasuringStationOnLine;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

public interface MeasuringOnlineServices {

    List<MeasuringStationOnLine> getAllMeasuringStations() throws RestClientException, NoSuchElementException;

    List<MeasuringStationOnLine> getGivenCityMeasuringStationsWithSynopticData(String cityName)
            throws RestClientException, IllegalArgumentException, NoSuchElementException;

    MeasuringStationOnLine getHottestOnlineStation() throws RestClientException, NoSuchElementException;

    MeasuringStationOnLine getColdestOnlineStation() throws RestClientException, NoSuchElementException;
}
