package pl.testaarosa.airmeasurements.domain.measurementsdto;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationDtoRepository;

public class MeasuringStationDtoTestSuit {

    private final MockMeasuringStationDtoRepository mockMStDtoRepository = new MockMeasuringStationDtoRepository();

    @Test
    public void testCityRegionDto(){
        MeasuringStationDto measuringStationDto1 = mockMStDtoRepository.measuringStationDto1();
        MeasuringStationDto measuringStationDto2 = mockMStDtoRepository.measuringStationDto2();
        new EqualsTester().addEqualityGroup(measuringStationDto1,measuringStationDto2).testEquals();
    }
}
