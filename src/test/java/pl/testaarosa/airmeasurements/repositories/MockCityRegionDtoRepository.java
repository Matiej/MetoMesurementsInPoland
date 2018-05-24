package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.measurementsdto.CityRegionDto;

public class MockCityRegionDtoRepository {

    public CityRegionDto cityRegionDto1(){
        CityRegionDto cityRegionDto1 = new CityRegionDto();
        cityRegionDto1.setCommuneName("Commune1");
        cityRegionDto1.setDistrictName("Dictrict1");
        cityRegionDto1.setVoivodeship("voivodeship1");

        return cityRegionDto1;
    }

    public CityRegionDto cityRegionDto2(){
        CityRegionDto cityRegionDto2 = new CityRegionDto();
        cityRegionDto2.setCommuneName("Commune1");
        cityRegionDto2.setDistrictName("Dictrict1");
        cityRegionDto2.setVoivodeship("voivodeship1");

        return cityRegionDto2;
    }


}
