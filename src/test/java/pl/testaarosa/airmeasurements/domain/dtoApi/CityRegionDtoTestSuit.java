package pl.testaarosa.airmeasurements.domain.dtoApi;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockCityRegionDtoRepository;

public class CityRegionDtoTestSuit {

    private final MockCityRegionDtoRepository mockCityRegionDtoRepository = new MockCityRegionDtoRepository();

    @Test
    public void testCityRegionDto(){
        CityRegionDto cityRegionDto1 = mockCityRegionDtoRepository.cityRegionDtos().get(0);
        CityRegionDto cityRegionDto2 = mockCityRegionDtoRepository.cityRegionDtos().get(1);
        new EqualsTester().addEqualityGroup(cityRegionDto2,cityRegionDto1).testEquals();
    }
}
