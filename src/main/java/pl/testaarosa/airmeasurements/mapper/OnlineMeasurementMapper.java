package pl.testaarosa.airmeasurements.mapper;

import org.springframework.stereotype.Component;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.domain.dtoFe.OnlineMeasurementDto;
import pl.testaarosa.airmeasurements.domain.dtoApi.AirMeasurementDto;
import pl.testaarosa.airmeasurements.domain.dtoApi.MeasuringStationDto;
import pl.testaarosa.airmeasurements.domain.dtoApi.SynopticMeasurementDto;

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

    public List<OnlineMeasurementDto> mapToOnlineMeasurementDtoList(List<MeasuringStation> measuringStationList,
                                                                    Map<Integer, AirMeasurement> airMap,
                                                                    Map<String, SynopticMeasurement> synMap) {
        List<OnlineMeasurementDto> onlineMeasurementDtoList = new ArrayList<>();
        measuringStationList.stream().forEach(m -> {
            onlineMeasurementDtoList.add(mapToOnlineMeasurementDto(m, airMap.get(m.getStationId()), synMap.get(m.getCity())));
        });
        return onlineMeasurementDtoList;
    }

    public OnlineMeasurementDto mapToMeasuringStation(MeasuringStationDto measuringStationDto,
                                                      AirMeasurementDto airMeasurementDto,
                                                      SynopticMeasurementDto synopticMeasurementDto) {

        SynopticMeasurementDto synop = Optional.ofNullable(synopticMeasurementDto).orElse(emptyObj());
        String cityName = measuringStationDto.getCityDto().getCityName();
        return new OnlineMeasurementDto.OnlineMeasurementBuilder()
                .id(measuringStationDto.getId())
                .gegrLatitude(Double.parseDouble(
                        measuringStationDto.getGegrLat()))
                .gegrLongitude(Double.parseDouble(
                        measuringStationDto.getGegrLon()))
                .stationName(measuringStationDto.getStationName())
                .stationStreet(measuringStationDto.getAddressStreet())
                .stationCity(cityName)
                .stationDistrict(measuringStationDto.getCityDto()
                        .getCityRegionDto()
                        .getDistrictName())
                .stationVoivodeship(measuringStationDto.getCityDto()
                        .getCityRegionDto()
                        .getVoivodeship())
                .air(airMeasurementDto)
                .synoptics(synop)
                .build();
    }

    public List<OnlineMeasurementDto> mapToMeasuringStationList(List<MeasuringStationDto> measuringStationDtos,
                                                                Map<Integer, AirMeasurementDto> airMap,
                                                                Map<String, SynopticMeasurementDto> synMap) {
        List<OnlineMeasurementDto> onlineMeasurementDtoList = new LinkedList<>();
        for (MeasuringStationDto measuringStationDto : measuringStationDtos) {
            int stationId = measuringStationDto.getId();
            String stationCity = measuringStationDto.getCityDto().getCityName();
            onlineMeasurementDtoList.add(mapToMeasuringStation(measuringStationDto,
                    airMap.get(stationId),
                    synMap.get(stationCity)));
        }
        return onlineMeasurementDtoList;
    }

    private SynopticMeasurementDto emptyObj() {
        return new SynopticMeasurementDto(9999, "->>no data available<<-", 9999.0, 9999.0, 9999.0, 9999.0);
    }
}
