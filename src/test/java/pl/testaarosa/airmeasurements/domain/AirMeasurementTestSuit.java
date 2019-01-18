package pl.testaarosa.airmeasurements.domain;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockAirRepository;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotSame;

public class AirMeasurementTestSuit {

    private final MockAirRepository mockAirRepository = new MockAirRepository();

    @Test
    public void testAirmeasurementsDto(){
        AirMeasurement airMeasurement1 = mockAirRepository.airMeasurements1().get(0);
        AirMeasurement airMeasurement2 = mockAirRepository.airMeasurements1().get(0);
        AirMeasurement airMeasurement3 = mockAirRepository.airMeasurements1().get(1);
        new EqualsTester().addEqualityGroup(airMeasurement1, airMeasurement2).testEquals();
        assertEquals(airMeasurement1,airMeasurement2);
        assertNotSame(airMeasurement1,airMeasurement3);
    }
}
