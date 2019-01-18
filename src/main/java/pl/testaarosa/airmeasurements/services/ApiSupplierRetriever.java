package pl.testaarosa.airmeasurements.services;

import org.springframework.web.client.RestClientException;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;

import java.util.List;
import java.util.Map;

public interface ApiSupplierRetriever {

    List<MeasuringStation> measuringStationApiProcessor() throws RestClientException;

    Map<String, SynopticMeasurement> synopticMeasurementProcessor() throws RestClientException;

    Map<MeasuringStation, AirMeasurement> airMeasurementsAndStProcessor() throws RestClientException;

    AirMeasurement airMeasurementProcessorById(int stationId) throws RuntimeException;
}
