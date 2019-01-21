package pl.testaarosa.airmeasurements.domain.dtoApi;

import com.google.common.testing.EqualsTester;
import org.junit.Before;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockSynopticMeasurementDtoRepository;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SynopticMeasurementDtoTestSuit {
    private MockSynopticMeasurementDtoRepository mocksynDto ;

    @Before
    public void init() {
        mocksynDto = new MockSynopticMeasurementDtoRepository();
    }

    @Test
    public void testCityRegionDto(){
        //given
        SynopticMeasurementDto synopticMeasurementDto1 = mocksynDto.mockSynopticDtoRepositories().get(0);
        SynopticMeasurementDto synopticMeasurementDto2 = mocksynDto.mockSynopticDtoRepositories().get(1);
        SynopticMeasurementDto synopticMeasurementDto3 = mocksynDto.mockSynopticDtoRepositories().get(3);
        //when
        //then
        assertNotNull(synopticMeasurementDto1);
        new EqualsTester().addEqualityGroup(synopticMeasurementDto1,synopticMeasurementDto2).testEquals();
        assertEquals(synopticMeasurementDto1,synopticMeasurementDto2);
        assertNotEquals(synopticMeasurementDto1, synopticMeasurementDto3);
    }
}
