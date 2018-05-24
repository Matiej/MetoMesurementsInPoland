package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.measurementsdto.CityDto;

import java.util.ArrayList;
import java.util.List;

public class MockCityDtoRepository {
    private final MockCityRegionDtoRepository mockCityRegionDtoRepository = new MockCityRegionDtoRepository();

    public List<CityDto> cityDtos() {
        List<CityDto> cityDtos = new ArrayList<>();
        CityDto cityDto1 = new CityDto();
        cityDto1.setCityName("Warszawa");
        cityDto1.setId(1);
        cityDto1.setCityRegionDto(mockCityRegionDtoRepository.cityRegionDtos().get(1));

        CityDto cityDto2 = new CityDto();
        cityDto2.setCityName("Warszawa");
        cityDto2.setId(1);
        cityDto2.setCityRegionDto(mockCityRegionDtoRepository.cityRegionDtos().get(1));
        cityDtos.add(cityDto1);
        cityDtos.add(cityDto2);
        return cityDtos;
    }
}
