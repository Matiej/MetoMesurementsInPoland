package pl.testaarosa.airmeasurements.mapper;

import org.springframework.stereotype.Component;
import pl.testaarosa.airmeasurements.domain.MeasuringStationDetails;
import pl.testaarosa.airmeasurements.domain.measurementsdto.CityRegionDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.MeasuringStationDto;

@Component
public class MeasuringStationDetailsMapper {

    public MeasuringStationDetails mapToStationDetails(MeasuringStationDto stationDto) {
        CityRegionDto cityRegionDto = stationDto.getCityDto().getCityRegionDto();
        return new MeasuringStationDetails.MeasuringStationDetailsBuilder().city(stationDto.getCityDto().getCityName())
                                                                           .commune(cityRegionDto.getCommuneName())
                                                                           .district(cityRegionDto.getDistrictName())
                                                                           .voivodeship(cityRegionDto.getVoivodeship())
                                                                           .build();
    }
}
