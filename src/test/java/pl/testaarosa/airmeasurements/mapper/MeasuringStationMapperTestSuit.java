package pl.testaarosa.airmeasurements.mapper;

import org.junit.Test;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.dtoApi.MeasuringStationDto;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationDtoRepository;
import pl.testaarosa.airmeasurements.repositories.MockStationRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MeasuringStationMapperTestSuit {
    private final MockMeasuringStationDtoRepository mockMStationDtoRepo = new MockMeasuringStationDtoRepository();
    private final MeasuringStationMapper mapper = new MeasuringStationMapper();
    private final MockStationRepository mockStationRepository = new MockStationRepository();

    @Test
    public void shouldMapToAirMeasurements() throws IllegalAccessException {
        //given
        MeasuringStation expect = mockStationRepository.stations().get(0);
        Field[] fields = MeasuringStation.class.getDeclaredFields();
        fields[0].setAccessible(true);
        fields[0].set(expect, null);
        fields[8].setAccessible(true);
        fields[8].set(expect, new ArrayList<>());
        fields[9].setAccessible(true);
        fields[9].set(expect, new ArrayList<>());
        MeasuringStationDto stationDto = mockMStationDtoRepo.measuringStationDtoList().get(0);
        //when
        MeasuringStation result = mapper.mapToMeasuringSt(stationDto);
        //then
        assertEquals(expect, result);
    }

    @Test
    public void shouldNotMapToAirMeasurements() throws IllegalAccessException {
        //given
        MeasuringStation expect = mockStationRepository.stations().get(0);
        Field[] fields = MeasuringStation.class.getDeclaredFields();
        MeasuringStationDto stationDto = mockMStationDtoRepo.measuringStationDtoList().get(0);
        //when
        MeasuringStation result = mapper.mapToMeasuringSt(stationDto);
        //then
        assertNotEquals(expect, result);
    }
}
