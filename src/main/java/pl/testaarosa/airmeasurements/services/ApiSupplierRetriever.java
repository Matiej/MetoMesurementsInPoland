package pl.testaarosa.airmeasurements.services;

import org.springframework.web.client.RestClientException;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;

import java.util.Map;
import java.util.NoSuchElementException;

public interface ApiSupplierRetriever {

    Map<String, SynopticMeasurement> synopticMeasurementProcessor() throws RestClientException;

    Map<MeasuringStation, AirMeasurement> airMeasurementsAndStProcessor() throws RestClientException, NoSuchElementException;
    Map<MeasuringStation, AirMeasurement> airMeasurementsAndStProcessor(Integer stationID) throws RestClientException,NoSuchElementException;

}
