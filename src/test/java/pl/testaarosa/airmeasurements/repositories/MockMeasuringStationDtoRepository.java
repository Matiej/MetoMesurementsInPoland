package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.measurementsdto.MeasuringStationDto;

public class MockMeasuringStationDtoRepository {

    private final MockCityDtoRepository mockCityDtoRepository = new MockCityDtoRepository();

    public MeasuringStationDto measuringStationDto1(){
        MeasuringStationDto measuringStationDto = new MeasuringStationDto();
        measuringStationDto.setId(1);
        measuringStationDto.setStationName("name");
        measuringStationDto.setGegrLat("15");
        measuringStationDto.setGegrLon("15");
        measuringStationDto.setAddressStreet("street");
        measuringStationDto.setCityDto(mockCityDtoRepository.cityDto1());
        return measuringStationDto;
    }

    public MeasuringStationDto measuringStationDto2(){
        MeasuringStationDto measuringStationDto = new MeasuringStationDto();
        measuringStationDto.setId(1);
        measuringStationDto.setStationName("name");
        measuringStationDto.setGegrLat("15");
        measuringStationDto.setGegrLon("15");
        measuringStationDto.setAddressStreet("street");
        measuringStationDto.setCityDto(mockCityDtoRepository.cityDto1());
        return measuringStationDto;
    }
}
