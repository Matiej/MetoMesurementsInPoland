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

@Service
public class MeasurementStationProcessor {

    private final ApiSupplierRetriever apiSupplierRetriever;
    private final MeasuringStationOnLineMapper measuringStationMapper;

    @Autowired
    public MeasurementStationProcessor(ApiSupplierRetriever apiSupplierRetriever, MeasuringStationOnLineMapper measuringStationMapper) {
        this.apiSupplierRetriever = apiSupplierRetriever;
        this.measuringStationMapper = measuringStationMapper;
    }

    public List<MeasuringStationOnLine> fillMeasuringStationListStructure() {
        List<MeasuringStationDto> msDto = apiSupplierRetriever.measuringStationApiProcessor();
        Map<String, SynopticMeasurementDto> synoptic = apiSupplierRetriever.synopticMeasurementProcessor();
        Map<Integer, AirMeasurementsDto> air = apiSupplierRetriever.airMeasurementsProcessor();
        return measuringStationMapper.mapToMeasuringStationList(msDto, air, synoptic);
    }
}
