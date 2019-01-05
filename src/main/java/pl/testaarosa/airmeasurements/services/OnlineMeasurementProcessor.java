package pl.testaarosa.airmeasurements.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.domain.dtoFe.OnlineMeasurementDto;
import pl.testaarosa.airmeasurements.domain.dtoApi.AirMeasurementDto;
import pl.testaarosa.airmeasurements.domain.dtoApi.MeasuringStationDto;
import pl.testaarosa.airmeasurements.domain.dtoApi.SynopticMeasurementDto;
import pl.testaarosa.airmeasurements.mapper.OnlineMeasurementMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class OnlineMeasurementProcessor {

    private final ApiSupplierRetriever apiSupplierRetriever;
    private final OnlineMeasurementMapper measuringStationMapper;

    @Autowired
    public OnlineMeasurementProcessor(ApiSupplierRetriever apiSupplierRetriever, OnlineMeasurementMapper measuringStationMapper) {
        this.apiSupplierRetriever = apiSupplierRetriever;
        this.measuringStationMapper = measuringStationMapper;
    }

    public List<OnlineMeasurementDto> fillMeasuringStationListStructure() throws ExecutionException, InterruptedException {
        CompletableFuture<List<MeasuringStation>> listCompletableFuture = apiSupplierRetriever.measuringStationApiProcessor();
        CompletableFuture<Map<String, SynopticMeasurement>> mapCompletableFuture = apiSupplierRetriever.synopticMeasurementProcessor();
        CompletableFuture<Map<Integer, AirMeasurement>> mapCompletableFuture1 = apiSupplierRetriever.airMeasurementsProcessor( new ArrayList<>(listCompletableFuture.join()));
        List<MeasuringStation> msDto = listCompletableFuture.get();
        Map<String, SynopticMeasurement> synoptic = mapCompletableFuture.get();
        Map<Integer, AirMeasurement> air = mapCompletableFuture1.get();
        return measuringStationMapper.mapToOnlineMeasurementDtoList(msDto, air, synoptic);
    }
}
