package pl.testaarosa.airmeasurements.domain.dtoApi;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockAirDtoRepository;

public class AirMeasurementDtoTestSuit {
    private final MockAirDtoRepository mockAirDtoRepository = new MockAirDtoRepository();

    @Test
    public void testAirmeasurementsDto(){
        AirMeasurementDto airMeasurementDto = mockAirDtoRepository.airMeasurementsDtos().get(0);
        AirMeasurementDto airMeasurementDto1 = mockAirDtoRepository.airMeasurementsDtos().get(1);
        new EqualsTester().addEqualityGroup(airMeasurementDto, airMeasurementDto1).testEquals();
    }
}
