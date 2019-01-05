package pl.testaarosa.airmeasurements.domain;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import pl.testaarosa.airmeasurements.domain.dtoFe.OnlineMeasurementDto;
import pl.testaarosa.airmeasurements.repositories.MockOnlineRepository;

public class OnlineMeasurementDtoTestSuit {
    private final MockOnlineRepository mockOnlineRepository = new MockOnlineRepository();

    @Test
    public void testAirmeasurementsDto(){
        OnlineMeasurementDto onlineMeasurementDto1 = mockOnlineRepository.measuringStationOnLineList().get(3);
        OnlineMeasurementDto onlineMeasurementDto2 = mockOnlineRepository.measuringStationOnLineList().get(2);
        new EqualsTester().addEqualityGroup(onlineMeasurementDto1, onlineMeasurementDto2).testEquals();
    }
}
