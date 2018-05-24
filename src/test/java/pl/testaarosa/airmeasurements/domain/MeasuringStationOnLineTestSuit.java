package pl.testaarosa.airmeasurements.domain;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockOnlineRepository;

public class MeasuringStationOnLineTestSuit {
    private final MockOnlineRepository mockOnlineRepository = new MockOnlineRepository();

    @Test
    public void testAirmeasurementsDto(){
        MeasuringStationOnLine measuringStationOnLine1 = mockOnlineRepository.measuringStationOnLineList().get(3);
        MeasuringStationOnLine measuringStationOnLine2 = mockOnlineRepository.measuringStationOnLineList().get(2);
        new EqualsTester().addEqualityGroup(measuringStationOnLine1,  measuringStationOnLine2).testEquals();
    }
}
