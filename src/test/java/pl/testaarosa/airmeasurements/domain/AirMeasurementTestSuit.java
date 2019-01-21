package pl.testaarosa.airmeasurements.domain;

import com.google.common.testing.EqualsTester;
import org.junit.Before;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockAirMeasurementRepository;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotSame;

public class AirMeasurementTestSuit {

    private MockAirMeasurementRepository mockAirMeasurementRepository ;

    @Before
    public void init() {
        mockAirMeasurementRepository = new MockAirMeasurementRepository();
    }

    @Test
    public void testAirmeasurementsDto(){
        //given
        AirMeasurement airMeasurement1 = mockAirMeasurementRepository.airMeasurements1().get(0);
        AirMeasurement airMeasurement2 = mockAirMeasurementRepository.airMeasurements1().get(0);
        AirMeasurement airMeasurement3 = mockAirMeasurementRepository.airMeasurements1().get(1);
        //when
        //then
        new EqualsTester().addEqualityGroup(airMeasurement1, airMeasurement2).testEquals();
        assertEquals(airMeasurement1,airMeasurement2);
        assertNotSame(airMeasurement1,airMeasurement3);
    }
}
