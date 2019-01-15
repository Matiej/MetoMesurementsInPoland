package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.dtoApi.CityRegionDto;

import java.util.ArrayList;
import java.util.List;

public class MockCityRegionDtoRepository {

    public List<CityRegionDto> cityRegionDtos() {
        List<CityRegionDto> cityRegionDtoList = new ArrayList<>();
        CityRegionDto cityRegionDto1 = new CityRegionDto();
        cityRegionDto1.setCommuneName("Commune1");
        cityRegionDto1.setDistrictName("Dictrict1");
        cityRegionDto1.setVoivodeship("mazowieckie");

        CityRegionDto cityRegionDto2 = new CityRegionDto();
        cityRegionDto2.setCommuneName("Commune1");
        cityRegionDto2.setDistrictName("Dictrict1");
        cityRegionDto2.setVoivodeship("mazowieckie");

        CityRegionDto cityRegionDto3 = new CityRegionDto();
        cityRegionDto2.setCommuneName("Commune1");
        cityRegionDto2.setDistrictName("Dictrict1");
        cityRegionDto2.setVoivodeship("wielkopolskie");

        CityRegionDto cityRegionDto4 = new CityRegionDto();
        cityRegionDto2.setCommuneName("Commune1");
        cityRegionDto2.setDistrictName("Dictrict1");
        cityRegionDto2.setVoivodeship("wielkopolskie");

        cityRegionDtoList.add(cityRegionDto1);
        cityRegionDtoList.add(cityRegionDto2);
        cityRegionDtoList.add(cityRegionDto3);
        cityRegionDtoList.add(cityRegionDto4);

        return cityRegionDtoList;
    }
}
