package pl.testaarosa.airmeasurements.domain.dtoApi;

import com.google.common.testing.EqualsTester;
import org.junit.Before;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockAirMeasurementDtoRepository;

import static org.junit.jupiter.api.Assertions.*;

public class AirMeasurementDtoTestSuit {
    private MockAirMeasurementDtoRepository mockAirDtoRepository;

    @Before
    public void init() {
        mockAirDtoRepository = new MockAirMeasurementDtoRepository();
    }

    @Test
    public void testAirmeasurementsDto(){
        //given
        AirMeasurementDto airMeasurementDto = mockAirDtoRepository.airMeasurementsDtos().get(0);
        AirMeasurementDto airMeasurementDto1 = mockAirDtoRepository.airMeasurementsDtos().get(1);
        AirMeasurementDto airMeasurementDto2 = mockAirDtoRepository.airMeasurementsDtos().get(2);
        //when
        //then
        assertNotNull(airMeasurementDto);
        new EqualsTester().addEqualityGroup(airMeasurementDto, airMeasurementDto1).testEquals();
        assertEquals(airMeasurementDto,airMeasurementDto1);
        assertNotEquals(airMeasurementDto,airMeasurementDto2);
    }
}
