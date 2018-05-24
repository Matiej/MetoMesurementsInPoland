package pl.testaarosa.airmeasurements.domain.measurementsdto;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockCityDtoRepository;

public class CityDtoTestSuit {
    private final MockCityDtoRepository cityDtoRepository = new MockCityDtoRepository();

    @Test
    public void testCityDto(){
        CityDto cityDto1 = cityDtoRepository.cityDto1();
        CityDto cityDto2 = cityDtoRepository.cityDto2();
        new EqualsTester().addEqualityGroup(cityDto1,cityDto2).testEquals();
    }
}
