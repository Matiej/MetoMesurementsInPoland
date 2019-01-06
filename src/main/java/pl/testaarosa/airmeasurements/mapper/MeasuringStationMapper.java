package pl.testaarosa.airmeasurements.mapper;

import org.springframework.stereotype.Component;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.MeasuringStationDetails;
import pl.testaarosa.airmeasurements.domain.dtoApi.CityRegionDto;
import pl.testaarosa.airmeasurements.domain.dtoApi.MeasuringStationDto;

import java.util.Optional;

@Component
public class MeasuringStationMapper {

    public MeasuringStation mapToMeasuringSt(MeasuringStationDto stationDto) {
        CityRegionDto cityRegionDto = stationDto.getCityDto().getCityRegionDto();
        String cityName = stationDto.getCityDto().getCityName();
        MeasuringStationDetails mdetail = new MeasuringStationDetails.MeasuringStationDetailsBuilder()
                .city(cityName)
                .commune(stationDto.getCityDto().getCityRegionDto().getCommuneName())
                .district(stationDto.getCityDto().getCityRegionDto().getDistrictName())
                .voivodeship(cityRegionDto.getVoivodeship())
                .build();

        MeasuringStation measuringStation = new MeasuringStation();
        measuringStation.setStationId(stationDto.getId());
        measuringStation.setStationName(stationDto.getStationName());
        measuringStation.setLatitude(stationDto.getGegrLat());
        measuringStation.setLongitude(stationDto.getGegrLon());
        measuringStation.setStreet(stationDto.getAddressStreet());
        measuringStation.setCity(cityName);
        measuringStation.setStationDetails(mdetail);
        return measuringStation;
    }
}
