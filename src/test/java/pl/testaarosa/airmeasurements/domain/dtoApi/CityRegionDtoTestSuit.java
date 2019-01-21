package pl.testaarosa.airmeasurements.domain.dtoApi;

import com.google.common.testing.EqualsTester;
import org.junit.Before;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockCityRegionDtoRepository;

import static org.junit.jupiter.api.Assertions.*;

public class CityRegionDtoTestSuit {

    private MockCityRegionDtoRepository mockCityRegionDtoRepository;

    @Before
    public void init() {
        mockCityRegionDtoRepository = new MockCityRegionDtoRepository();
    }

    @Test
    public void testCityRegionDto(){
        //given
        CityRegionDto cityRegionDto1 = mockCityRegionDtoRepository.cityRegionDtos().get(0);
        CityRegionDto cityRegionDto2 = mockCityRegionDtoRepository.cityRegionDtos().get(1);
        CityRegionDto cityRegionDto3 = mockCityRegionDtoRepository.cityRegionDtos().get(2);
        //when
        //then
        assertNotNull(cityRegionDto1);
        new EqualsTester().addEqualityGroup(cityRegionDto2,cityRegionDto1).testEquals();
        assertEquals(cityRegionDto1,cityRegionDto2);
        assertNotEquals(cityRegionDto1,cityRegionDto3);
    }
}
