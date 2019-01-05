package pl.testaarosa.airmeasurements.domain;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockAirRepository;

public class AirMeasurementTestSuit {

    private final MockAirRepository mockAirRepository = new MockAirRepository();

    @Test
    public void testAirmeasurementsDto(){
        AirMeasurement airMeasurement = mockAirRepository.airMeasurement();
        AirMeasurement airMeasurement1 = mockAirRepository.airMeasurement1();
        new EqualsTester().addEqualityGroup(airMeasurement1, airMeasurement).testEquals();
    }
}
