package pl.testaarosa.airmeasurements.services;

import org.springframework.web.client.RestClientException;
import pl.testaarosa.airmeasurements.model.OnlineMeasurementDto;

import java.util.List;
import java.util.NoSuchElementException;

public interface OnlineMeasurementService {

    List<OnlineMeasurementDto> getAllMeasuringStations() throws RestClientException, NoSuchElementException;

    List<OnlineMeasurementDto> getGivenCityMeasuringStationsWithSynopticData(String cityName)
            throws RestClientException, IllegalArgumentException, NoSuchElementException;

    OnlineMeasurementDto getHottestOnlineStation() throws RestClientException, NoSuchElementException;

    OnlineMeasurementDto getColdestOnlineStation() throws RestClientException, NoSuchElementException;
}
