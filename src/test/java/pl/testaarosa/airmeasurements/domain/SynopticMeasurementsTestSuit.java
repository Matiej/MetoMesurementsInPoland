package pl.testaarosa.airmeasurements.domain;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockSynopticRepository;

public class SynopticMeasurementsTestSuit {
private final MockSynopticRepository mockSynopticRepository = new MockSynopticRepository();

    @Test
    public void testAirmeasurementsDto(){
        SynopticMeasurements synopticMeasurements1 = mockSynopticRepository.synopticMeasurements2().get(3);
        SynopticMeasurements synopticMeasurements2 = mockSynopticRepository.synopticMeasurements2().get(2);
        new EqualsTester().addEqualityGroup(synopticMeasurements1, synopticMeasurements2).testEquals();
    }
}
