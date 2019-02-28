package pl.testaarosa.airmeasurements.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.mapper.OnlineMeasurementMapper;
import pl.testaarosa.airmeasurements.model.CityFeDto;
import pl.testaarosa.airmeasurements.model.OnlineMeasurementDto;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class OnlineMeasurementProcessor {

    private final ApiSupplierRetriever apiSupplierRetriever;
    private final OnlineMeasurementMapper measuringStationMapper;

    @Autowired
    public OnlineMeasurementProcessor(ApiSupplierRetriever apiSupplierRetriever, OnlineMeasurementMapper measuringStationMapper) {
        this.apiSupplierRetriever = apiSupplierRetriever;
        this.measuringStationMapper = measuringStationMapper;
    }

    public List<OnlineMeasurementDto> fillMeasuringStationListStructure() throws RestClientException, NoSuchElementException {
        Map<MeasuringStation, AirMeasurement> airMeasurementMap = apiSupplierRetriever.airMeasurementsAndStProcessor();
        Map<String, SynopticMeasurement> synopticMeasurementMap = apiSupplierRetriever.synopticMeasurementProcessor();
        fillCityFeDtoStructure();
        return measuringStationMapper.mapToOnlneMsDtoList(airMeasurementMap, synopticMeasurementMap);
    }

    public List<CityFeDto> fillCityFeDtoStructure() throws RestClientException, NoSuchElementException {
        Map<MeasuringStation, AirMeasurement> airMeasurementMap = apiSupplierRetriever.airMeasurementsAndStProcessor();
        Map<String, SynopticMeasurement> synopticMeasurementMap = apiSupplierRetriever.synopticMeasurementProcessor();
        return measuringStationMapper.mapToCityFeDto(airMeasurementMap,synopticMeasurementMap);
    }
}
