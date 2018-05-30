package pl.testaarosa.airmeasurements.mapper;

import org.junit.Test;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.measurementsdto.MeasuringStationDto;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationDtoRepository;
import pl.testaarosa.airmeasurements.repositories.MockStationRepository;

import static org.junit.Assert.assertEquals;

public class MeasuringStationMapperTestSuit {
    private final MockMeasuringStationDtoRepository mockMStationDtoRepo = new MockMeasuringStationDtoRepository();
    private final MeasuringStationMapper mapper = new MeasuringStationMapper();
    private final MockStationRepository mockStationRepository = new MockStationRepository();
    @Test
    public void testMapToAirMeasurements() {
        MeasuringStation expect = mockStationRepository.stationForMapperTest();
        MeasuringStationDto stationDto = mockMStationDtoRepo.measuringStationDtoList().get(2);
        MeasuringStation result = mapper.mapToMeasuringSt(stationDto);
        assertEquals(expect, result);
    }
}
