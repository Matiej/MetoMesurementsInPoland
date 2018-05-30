package pl.testaarosa.airmeasurements.domain.measurementsdto;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationDtoRepository;

public class MeasuringStationDtoTestSuit {

    private final MockMeasuringStationDtoRepository mockMStDtoRepository = new MockMeasuringStationDtoRepository();

    @Test
    public void testStationDto(){
        MeasuringStationDto measuringStationDto1 = mockMStDtoRepository.measuringStationDtoList().get(2);
        MeasuringStationDto measuringStationDto2 = mockMStDtoRepository.measuringStationDtoList().get(3);
        new EqualsTester().addEqualityGroup(measuringStationDto1,measuringStationDto2).testEquals();
    }
}
