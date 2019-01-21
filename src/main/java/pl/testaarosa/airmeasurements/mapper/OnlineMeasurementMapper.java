package pl.testaarosa.airmeasurements.mapper;

import org.springframework.stereotype.Component;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.model.OnlineMeasurementDto;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class OnlineMeasurementMapper {

    public OnlineMeasurementDto mapToOnlineMeasurementDto(MeasuringStation measuringSt, AirMeasurement airMs,
                                                          SynopticMeasurement synopticMs) {
        return new OnlineMeasurementDto.OnlineMeasurementBuilder()
                .id(measuringSt.getStationId())
                .gegrLatitude(Double.parseDouble(measuringSt.getLatitude()))
                .gegrLongitude(Double.parseDouble(measuringSt.getLongitude()))
                .stationName(measuringSt.getStationName())
                .stationStreet(measuringSt.getStreet())
                .stationCity(measuringSt.getCity())
                .stationDistrict(measuringSt.getStationDetails().getDistrict())
                .stationVoivodeship(measuringSt.getStationDetails().getVoivodeship())
                .airMs(airMs)
                .synopticMs(synopticMs)
                .build();
    }

    public List<OnlineMeasurementDto> mapToOnlneMsDtoList(Map<MeasuringStation, AirMeasurement> measurementMap,
                                                          Map<String, SynopticMeasurement> synMap) {
        return  measurementMap.entrySet().stream()
                .map(t -> mapToOnlineMeasurementDto(t.getKey(), t.getValue(), synMap.get(t.getKey().getCity())))
                .collect(Collectors.toList());
    }
}
