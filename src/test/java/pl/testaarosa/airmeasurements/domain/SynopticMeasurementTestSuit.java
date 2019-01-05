package pl.testaarosa.airmeasurements.domain;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockSynopticRepository;

public class SynopticMeasurementTestSuit {
private final MockSynopticRepository mockSynopticRepository = new MockSynopticRepository();

    @Test
    public void testAirmeasurementsDto(){
        SynopticMeasurement synopticMeasurement1 = mockSynopticRepository.synopticMeasurements2().get(3);
        SynopticMeasurement synopticMeasurement2 = mockSynopticRepository.synopticMeasurements2().get(2);
        new EqualsTester().addEqualityGroup(synopticMeasurement1, synopticMeasurement2).testEquals();
    }
}
