package pl.testaarosa.airmeasurements.services;

import org.springframework.web.client.RestClientException;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface ApiSupplierRetriever {

    CompletableFuture<List<MeasuringStation>> measuringStationApiProcessor() throws RestClientException;

    CompletableFuture<Map<String, SynopticMeasurement>> synopticMeasurementProcessor() throws RestClientException;

    CompletableFuture<Map<Integer, AirMeasurement>> airMeasurementsProcessor(List<MeasuringStation> measuringStationList) throws ExecutionException,
            InterruptedException, RestClientException;

    CompletableFuture<AirMeasurement> airMeasurementProcessorById(int stationId) throws RestClientException;
}
