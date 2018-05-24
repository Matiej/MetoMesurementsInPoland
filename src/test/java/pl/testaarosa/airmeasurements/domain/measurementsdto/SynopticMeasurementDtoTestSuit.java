package pl.testaarosa.airmeasurements.domain.measurementsdto;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockSynopticDtoRepository;

public class SynopticMeasurementDtoTestSuit {
    private final MockSynopticDtoRepository mocksynDto = new MockSynopticDtoRepository();

    @Test
    public void testCityRegionDto(){
        SynopticMeasurementDto synopticMeasurementDto1 = mocksynDto.mockSynopticDtoRepositories().get(0);
        SynopticMeasurementDto synopticMeasurementDto2 = mocksynDto.mockSynopticDtoRepositories().get(1);
        new EqualsTester().addEqualityGroup(synopticMeasurementDto1,synopticMeasurementDto2).testEquals();
    }
}
