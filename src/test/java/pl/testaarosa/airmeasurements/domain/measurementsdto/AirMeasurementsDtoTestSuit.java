package pl.testaarosa.airmeasurements.domain.measurementsdto;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockAirDtoRepository;

public class AirMeasurementsDtoTestSuit {
    private final MockAirDtoRepository mockAirDtoRepository = new MockAirDtoRepository();

    @Test
    public void testAirmeasurementsDto(){
        AirMeasurementsDto airMeasurementsDto = mockAirDtoRepository.airMeasurementsDto();
        AirMeasurementsDto airMeasurementsDto1 = mockAirDtoRepository.airMeasurementsDto1();
        new EqualsTester().addEqualityGroup(airMeasurementsDto, airMeasurementsDto1).testEquals();
    }
}
