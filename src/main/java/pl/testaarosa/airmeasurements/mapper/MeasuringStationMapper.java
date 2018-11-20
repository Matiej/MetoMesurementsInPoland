package pl.testaarosa.airmeasurements.mapper;

import org.springframework.stereotype.Component;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.measurementsdto.CityDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.MeasuringStationDto;

@Component
public class MeasuringStationMapper {

    public MeasuringStation mapToMeasuringSt(MeasuringStationDto stationDto) {
        MeasuringStation measuringStation = new MeasuringStation();
        measuringStation.setStationId(stationDto.getId());
        measuringStation.setStationName(stationDto.getStationName());
        measuringStation.setLatitude(stationDto.getGegrLat());
        measuringStation.setLongitude(stationDto.getGegrLon());
        measuringStation.setStreet(stationDto.getAddressStreet());
        measuringStation.setCity(stationDto.getCityDto().getCityName());
        return measuringStation;
    }
}
