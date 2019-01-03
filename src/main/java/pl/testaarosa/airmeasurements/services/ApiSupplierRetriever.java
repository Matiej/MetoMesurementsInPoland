package pl.testaarosa.airmeasurements.services;

import org.springframework.web.client.RestClientException;
import pl.testaarosa.airmeasurements.domain.measurementsdto.AirMeasurementsDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.MeasuringStationDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.SynopticMeasurementDto;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface ApiSupplierRetriever {

    CompletableFuture<List<MeasuringStationDto>> measuringStationApiProcessor() throws RestClientException;

    CompletableFuture<Map<String, SynopticMeasurementDto>> synopticMeasurementProcessor() throws RestClientException;

    CompletableFuture<Map<Integer, AirMeasurementsDto>> airMeasurementsProcessor() throws ExecutionException,
            InterruptedException, RestClientException;

    CompletableFuture<AirMeasurementsDto> airMeasurementsProcessorNew(int stationId) throws RestClientException;
}
