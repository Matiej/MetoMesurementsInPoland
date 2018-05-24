package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.measurementsdto.CityDto;

public class MockCityDtoRepository {
    MockCityRegionDtoRepository mockCityRegionDtoRepository = new MockCityRegionDtoRepository();
    public CityDto cityDto1() {
        CityDto cityDto1 = new CityDto();
        cityDto1.setCityName("City");
        cityDto1.setId(1);
        cityDto1.setCityRegionDto(mockCityRegionDtoRepository.cityRegionDto1());
        return cityDto1;
    }

    public CityDto cityDto2() {
        CityDto cityDto2 = new CityDto();
        cityDto2.setCityName("City");
        cityDto2.setId(1);
        cityDto2.setCityRegionDto(mockCityRegionDtoRepository.cityRegionDto1());
        return cityDto2;
    }
}
