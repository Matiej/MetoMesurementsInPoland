package pl.testaarosa.airmeasurements.mapper;

import org.springframework.stereotype.Component;
import pl.testaarosa.airmeasurements.domain.MeasuringStationOnLine;
import pl.testaarosa.airmeasurements.domain.measurementsdto.AirMeasurementsDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.MeasuringStationDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.SynopticMeasurementDto;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class MeasuringStationOnLineMapper {

    public MeasuringStationOnLine mapToMeasuringStation(MeasuringStationDto measuringStationDto,
                                                        AirMeasurementsDto airMeasurementsDto,
                                                        SynopticMeasurementDto synopticMeasurementDto) {

        SynopticMeasurementDto synop = Optional.ofNullable(synopticMeasurementDto).orElse(emptyObj());
        String cityName = measuringStationDto.getCityDto().getCityName();
        return new MeasuringStationOnLine.MeasuringStationOnLineBuilder().id(measuringStationDto.getId())
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
                .air(airMeasurementsDto)
                .synoptics(synop)
                .build();
    }

    public List<MeasuringStationOnLine> mapToMeasuringStationList(List<MeasuringStationDto> measuringStationDtos,
                                                                  Map<Integer, AirMeasurementsDto> airMap,
                                                                  Map<String, SynopticMeasurementDto> synMap) {
        List<MeasuringStationOnLine> measuringStationOnLineList = new LinkedList<>();
        for (MeasuringStationDto measuringStationDto : measuringStationDtos) {
            int stationId = measuringStationDto.getId();
            String stationCity = measuringStationDto.getCityDto().getCityName();
            measuringStationOnLineList.add(mapToMeasuringStation(measuringStationDto,
                    airMap.get(stationId),
                    synMap.get(stationCity)));
        }
        return measuringStationOnLineList;
    }

    private SynopticMeasurementDto emptyObj() {
        return new SynopticMeasurementDto(9999, "->>no data available<<-", 9999.0, 9999.0, 9999.0, 9999.0);
    }
}
