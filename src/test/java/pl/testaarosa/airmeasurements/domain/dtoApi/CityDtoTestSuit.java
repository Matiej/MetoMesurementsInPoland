package pl.testaarosa.airmeasurements.domain.dtoApi;

import com.google.common.testing.EqualsTester;
import org.junit.Before;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockCityDtoRepository;

import static org.junit.jupiter.api.Assertions.*;

public class CityDtoTestSuit {
    private MockCityDtoRepository cityDtoRepository;

    @Before
    public void init() {
        cityDtoRepository = new MockCityDtoRepository();
    }

    @Test
    public void testCityDto(){
        //given
        CityDto cityDto1 = cityDtoRepository.cityDtos().get(0);
        CityDto cityDto2 = cityDtoRepository.cityDtos().get(1);
        CityDto cityDto3 = cityDtoRepository.cityDtos().get(3);
        //when
        //then
        assertNotNull(cityDto1);
        new EqualsTester().addEqualityGroup(cityDto1,cityDto2).testEquals();
        assertEquals(cityDto1,cityDto2);
        assertNotEquals(cityDto2,cityDto3);
    }
}
