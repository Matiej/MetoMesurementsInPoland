package pl.testaarosa.airmeasurements.domain.measurementsdto;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockAirDtoRepository;

public class AirMeasurementsDtoTestSuit {
    private final MockAirDtoRepository mockAirDtoRepository = new MockAirDtoRepository();

    @Test
    public void testAirmeasurementsDto(){
        AirMeasurementsDto airMeasurementsDto = mockAirDtoRepository.airMeasurementsDtos().get(0);
        AirMeasurementsDto airMeasurementsDto1 = mockAirDtoRepository.airMeasurementsDtos().get(1);
        new EqualsTester().addEqualityGroup(airMeasurementsDto, airMeasurementsDto1).testEquals();
    }
}
