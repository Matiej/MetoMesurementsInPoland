package pl.testaarosa.airmeasurements.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.testaarosa.airmeasurements.domain.MeasuringStationOnLine;
import pl.testaarosa.airmeasurements.domain.measurementsdto.AirMeasurementsDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.MeasuringStationDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.SynopticMeasurementDto;
import pl.testaarosa.airmeasurements.mapper.MeasuringStationOnLineMapper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class MeasurementStationProcessor {

    private final ApiSupplierRetriever apiSupplierRetriever;
    private final MeasuringStationOnLineMapper measuringStationMapper;

    @Autowired
    public MeasurementStationProcessor(ApiSupplierRetriever apiSupplierRetriever, MeasuringStationOnLineMapper measuringStationMapper) {
        this.apiSupplierRetriever = apiSupplierRetriever;
        this.measuringStationMapper = measuringStationMapper;
    }

    public List<MeasuringStationOnLine> fillMeasuringStationListStructure() throws ExecutionException, InterruptedException {
        CompletableFuture<List<MeasuringStationDto>> listCompletableFuture = apiSupplierRetriever.measuringStationApiProcessor();
        CompletableFuture<Map<String, SynopticMeasurementDto>> mapCompletableFuture = apiSupplierRetriever.synopticMeasurementProcessor();
        CompletableFuture<Map<Integer, AirMeasurementsDto>> mapCompletableFuture1 = apiSupplierRetriever.airMeasurementsProcessor();
        List<MeasuringStationDto> msDto = listCompletableFuture.get();
        Map<String, SynopticMeasurementDto> synoptic = mapCompletableFuture.get();
        Map<Integer, AirMeasurementsDto> air = mapCompletableFuture1.get();
        return measuringStationMapper.mapToMeasuringStationList(msDto, air, synoptic);
    }
}
