package pl.testaarosa.airmeasurements.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.dtoApi.AirMeasurementDto;
import pl.testaarosa.airmeasurements.repositories.MockAirDtoRepository;
import pl.testaarosa.airmeasurements.repositories.MockAirRepository;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class AirMeasurementMapperTestSuit {
    private final MockAirRepository mockAirRepository = new MockAirRepository();
    private final MockAirDtoRepository mockAirDtoRepository = new MockAirDtoRepository();
    private final AirMeasurementMapper airMeasurementMapper = new AirMeasurementMapper();

    @Test
    public void shouldMapToAirMeasurements() throws NoSuchFieldException, IllegalAccessException {
        //given
        AirMeasurement expect = mockAirRepository.airMeasurements1().get(0);
        AirMeasurementDto expectDto = mockAirDtoRepository.airMeasurementsDtos().get(0);
        AirMeasurement result = airMeasurementMapper.mapToAirMeasurements(expectDto);
        //when
        Field saveDate = AirMeasurement.class.getDeclaredField("saveDate");
        saveDate.setAccessible(true);
        saveDate.set(result, LocalDateTime.of(2018,05,05,12,01,05));
        //then
        assertEquals(expect,result);
    }

    @Test
    public void shouldNotMapToAirMeasurements() throws NoSuchFieldException, IllegalAccessException {
        //given
        AirMeasurement expect = mockAirRepository.airMeasurements1().get(0);
        AirMeasurementDto expectDto = mockAirDtoRepository.airMeasurementsDtos().get(1);
        //when
        AirMeasurement result = airMeasurementMapper.mapToAirMeasurements(expectDto);
        //then
        assertNotSame(expect,result);
    }
}
