package pl.testaarosa.airmeasurements.domain.dtoApi;

import com.google.common.testing.EqualsTester;
import org.junit.Before;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationDtoRepository;

import static org.junit.jupiter.api.Assertions.*;

public class MeasuringStationDtoTestSuit {

    private MockMeasuringStationDtoRepository mockMStDtoRepository;

    @Before
    public void init() {
        mockMStDtoRepository = new MockMeasuringStationDtoRepository();
    }

    @Test
    public void testStationDto(){
        //given
        MeasuringStationDto measuringStationDto1 = mockMStDtoRepository.measuringStationDtoList().get(0);
        MeasuringStationDto measuringStationDto2 = mockMStDtoRepository.measuringStationDtoList().get(1);
        MeasuringStationDto measuringStationDto3 = mockMStDtoRepository.measuringStationDtoList().get(2);
        //then
        //when
        assertNotNull(measuringStationDto1);
        new EqualsTester().addEqualityGroup(measuringStationDto1,measuringStationDto2).testEquals();
        assertEquals(measuringStationDto1,measuringStationDto2);
        assertNotEquals(measuringStationDto1,measuringStationDto3);
    }
}
