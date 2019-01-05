package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.dtoApi.CityDto;

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

        CityDto cityDto3 = new CityDto();
        cityDto3.setCityName("Poznan");
        cityDto3.setId(2);
        cityDto3.setCityRegionDto(mockCityRegionDtoRepository.cityRegionDtos().get(1));

        CityDto cityDto4 = new CityDto();
        cityDto4.setCityName("Krakow");
        cityDto4.setId(3);
        cityDto4.setCityRegionDto(mockCityRegionDtoRepository.cityRegionDtos().get(1));

        cityDtos.add(cityDto1);
        cityDtos.add(cityDto2);
        cityDtos.add(cityDto3);
        cityDtos.add(cityDto4);
        return cityDtos;
    }
}
