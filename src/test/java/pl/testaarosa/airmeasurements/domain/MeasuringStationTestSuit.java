package pl.testaarosa.airmeasurements.domain;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockStationRepository;

public class MeasuringStationTestSuit {

    private final MockStationRepository mockStationRepository = new MockStationRepository();

    @Test
    public void testAirmeasurementsDto(){
        MeasuringStation station = mockStationRepository.stations().get(2);
        MeasuringStation station1 = mockStationRepository.stations().get(3);
        new EqualsTester().addEqualityGroup(station1, station).testEquals();
    }
}
