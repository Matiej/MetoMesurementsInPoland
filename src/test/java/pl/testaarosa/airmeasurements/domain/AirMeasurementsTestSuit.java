package pl.testaarosa.airmeasurements.domain;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockAirRepository;

public class AirMeasurementsTestSuit {

    private final MockAirRepository mockAirRepository = new MockAirRepository();

    @Test
    public void testAirmeasurementsDto(){
        AirMeasurements airMeasurements = mockAirRepository.airMeasurement();
        AirMeasurements airMeasurements1 = mockAirRepository.airMeasurement1();
        new EqualsTester().addEqualityGroup(airMeasurements1, airMeasurements).testEquals();
    }
}
