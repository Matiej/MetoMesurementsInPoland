package pl.testaarosa.airmeasurements.domain;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockStationRepository;

public class MeasuringStationTestSuit {

    private final MockStationRepository mockStationRepository = new MockStationRepository();

    @Test
    public void testAirmeasurementsDto(){
        MeasuringStation station = mockStationRepository.station();
        MeasuringStation station1 = mockStationRepository.station1();
        new EqualsTester().addEqualityGroup(station1, station).testEquals();
    }
}
