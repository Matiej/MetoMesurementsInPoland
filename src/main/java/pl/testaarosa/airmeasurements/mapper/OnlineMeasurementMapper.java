package pl.testaarosa.airmeasurements.mapper;

import org.springframework.stereotype.Component;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.City;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.model.CityFeDto;
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
        return measurementMap.entrySet().stream()
                .map(t -> mapToOnlineMeasurementDto(t.getKey(), t.getValue(), synMap.get(t.getKey().getCity())))
                .collect(Collectors.toList());
    }

    public List<CityFeDto> mapToCityFeDto(Map<MeasuringStation, AirMeasurement> measurementMap,
                                          Map<String, SynopticMeasurement> synMap) {
        Set<CityFeDto> cityFeDtos = new LinkedHashSet<>();
        measurementMap.forEach((k, v) -> {
            if (cityFeDtos.stream().noneMatch(c -> k.getCity().equalsIgnoreCase(c.getName()))) {
                CityFeDto cityFeDtoNew = new CityFeDto();
                cityFeDtoNew.setName(k.getCity());
                cityFeDtoNew.getAirMeasurementList().add(v);
                cityFeDtos.add(cityFeDtoNew);
            } else {
                cityFeDtos.stream()
                        .filter(c -> c.getName().equalsIgnoreCase(k.getCity()))
                        .forEach(c -> {
                            c.getAirMeasurementList().add(v);
                            cityFeDtos.add(c);
                        });
            }
        });

        synMap.forEach((k, v) -> {
            if (cityFeDtos.stream().noneMatch(c -> k.equalsIgnoreCase(c.getName()))) {
                CityFeDto cityFeDtoNew = new CityFeDto();
                cityFeDtoNew.setName(k);
                cityFeDtoNew.setSynopticMeasurement(v);
                cityFeDtos.add(cityFeDtoNew);
            } else {
                cityFeDtos.stream()
                        .filter(c -> c.getName().equalsIgnoreCase(k))
                        .forEach(c -> {
                            c.setSynopticMeasurement(v);
                            cityFeDtos.add(c);
                        });
            }
            });

            return new LinkedList<>(cityFeDtos);
        }
    }
