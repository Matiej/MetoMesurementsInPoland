package pl.testaarosa.airmeasurements.domain.measurementsdto;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationDtoRepository;

public class MeasuringStationDtoTestSuit {

    private final MockMeasuringStationDtoRepository mockMStDtoRepository = new MockMeasuringStationDtoRepository();

    @Test
    public void testCityRegionDto(){
        MeasuringStationDto measuringStationDto1 = mockMStDtoRepository.measuringStationDtoList().get(0);
        MeasuringStationDto measuringStationDto2 = mockMStDtoRepository.measuringStationDtoList().get(1);
        new EqualsTester().addEqualityGroup(measuringStationDto1,measuringStationDto2).testEquals();
    }
}
