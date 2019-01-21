package pl.testaarosa.airmeasurements.domain;

import com.google.common.testing.EqualsTester;
import org.junit.Before;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockSynopticMeasurementRepository;

import static org.junit.jupiter.api.Assertions.*;

public class SynopticMeasurementTestSuit {
    private MockSynopticMeasurementRepository mockSynopticMeasurementRepository;

    @Before
    public void init() {
        mockSynopticMeasurementRepository = new MockSynopticMeasurementRepository();
    }

    @Test
    public void testAirmeasurementsDto() {
        //given
        SynopticMeasurement synopticMeasurement1 = mockSynopticMeasurementRepository.synopticMeasurementsOrderHottest().get(0);
        SynopticMeasurement synopticMeasurement2 = mockSynopticMeasurementRepository.synopticMeasurementsOrderHottest().get(1);
        SynopticMeasurement synopticMeasurement3 = mockSynopticMeasurementRepository.synopticMeasurementsOrderHottest().get(3);
        //when
        //then
        assertNotNull(synopticMeasurement1);
        new EqualsTester().addEqualityGroup(synopticMeasurement1, synopticMeasurement2).testEquals();
        assertEquals(synopticMeasurement1,synopticMeasurement2);
        assertNotEquals(synopticMeasurement1,synopticMeasurement3);
    }
}
