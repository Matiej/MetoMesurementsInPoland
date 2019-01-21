package pl.testaarosa.airmeasurements.mapper;

import org.junit.Before;
import org.junit.Test;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.dtoApi.AirMeasurementDto;
import pl.testaarosa.airmeasurements.repositories.MockAirMeasurementDtoRepository;
import pl.testaarosa.airmeasurements.repositories.MockAirMeasurementRepository;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class AirMeasurementMapperTestSuit {
    private MockAirMeasurementRepository mockAirMeasurementRepository;
    private MockAirMeasurementDtoRepository mockAirMeasurementDtoRepository;
    private AirMeasurementMapper airMeasurementMapper;

    @Before
    public void init() {
        mockAirMeasurementRepository = new MockAirMeasurementRepository();
        mockAirMeasurementDtoRepository = new MockAirMeasurementDtoRepository();
        airMeasurementMapper = new AirMeasurementMapper();

    }

    @Test
    public void shouldMapToAirMeasurements() throws NoSuchFieldException, IllegalAccessException {
        //given
        AirMeasurement expect = mockAirMeasurementRepository.airMeasurements1().get(0);
        AirMeasurementDto expectDto = mockAirMeasurementDtoRepository.airMeasurementsDtos().get(0);
        AirMeasurement result = airMeasurementMapper.mapToAirMeasurements(expectDto);
        //when
        Field saveDate = AirMeasurement.class.getDeclaredField("saveDate");
        saveDate.setAccessible(true);
        saveDate.set(result, LocalDateTime.of(2018, 05, 05, 12, 01, 05));
        //then
        assertEquals(expect, result);
    }

    @Test
    public void shouldNotMapToAirMeasurements() throws NoSuchFieldException, IllegalAccessException {
        //given
        AirMeasurement expect = mockAirMeasurementRepository.airMeasurements1().get(0);
        AirMeasurementDto expectDto = mockAirMeasurementDtoRepository.airMeasurementsDtos().get(1);
        //when
        AirMeasurement result = airMeasurementMapper.mapToAirMeasurements(expectDto);
        //then
        assertNotSame(expect, result);
    }
}
