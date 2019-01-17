package pl.testaarosa.airmeasurements.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.domain.dtoFe.OnlineMeasurementDto;
import pl.testaarosa.airmeasurements.mapper.OnlineMeasurementMapper;

import java.util.ArrayList;
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

//    public List<OnlineMeasurementDto> fillMeasuringStationListStructure() throws ExecutionException, InterruptedException {
//        List<MeasuringStation> mStList = apiSupplierRetriever.measuringStationApiProcessor();
//        Map<Integer, AirMeasurement> airMeasurementMap = apiSupplierRetriever.airMeasurementsProcessor(mStList);
//        Map<String, SynopticMeasurement> synopticMeasurementMap = apiSupplierRetriever.synopticMeasurementProcessor();
//        return measuringStationMapper.mapToOnlineMeasurementDtoList(mStList, airMeasurementMap, synopticMeasurementMap);
//    }

    public List<OnlineMeasurementDto> fillMeasuringStationListStructure() {
        Map<MeasuringStation, AirMeasurement> airMeasurementMap = apiSupplierRetriever.airMeasurementsAndStProcessor();
        Map<String, SynopticMeasurement> synopticMeasurementMap = apiSupplierRetriever.synopticMeasurementProcessor();
        return measuringStationMapper.mapToOnlneMsDtoList(airMeasurementMap, synopticMeasurementMap);
    }
}
