package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.measurementsdto.CityRegionDto;

import java.util.ArrayList;
import java.util.List;

public class MockCityRegionDtoRepository {

    public List<CityRegionDto> cityRegionDtos() {
        List<CityRegionDto> cityRegionDtoList = new ArrayList<>();
        CityRegionDto cityRegionDto1 = new CityRegionDto();
        cityRegionDto1.setCommuneName("Commune1");
        cityRegionDto1.setDistrictName("Dictrict1");
        cityRegionDto1.setVoivodeship("voivodeship1");

        CityRegionDto cityRegionDto2 = new CityRegionDto();
        cityRegionDto2.setCommuneName("Commune1");
        cityRegionDto2.setDistrictName("Dictrict1");
        cityRegionDto2.setVoivodeship("voivodeship1");

        cityRegionDtoList.add(cityRegionDto1);
        cityRegionDtoList.add(cityRegionDto2);

        return cityRegionDtoList;
    }
}
