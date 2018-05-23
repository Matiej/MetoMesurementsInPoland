package pl.testaarosa.airmeasurements.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import pl.testaarosa.airmeasurements.domain.AirMeasurements;
import pl.testaarosa.airmeasurements.repositories.MockAirRepository;
import pl.testaarosa.airmeasurements.repositories.MockDto;
import pl.testaarosa.airmeasurements.mapper.AirMeasurementMapper;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AirMeasurementMapperTest {
    private final MockAirRepository mockAirRepository = new MockAirRepository();
    private final MockDto mockDto = new MockDto();
    private final AirMeasurementMapper airMeasurementMapper = new AirMeasurementMapper();

    @Test
    public void testMapToAirMeasurements(){
        AirMeasurements expect = mockAirRepository.airMeasurement();
        AirMeasurements result = airMeasurementMapper.mapToAirMeasurements(mockDto.airMeasurementsDto());
        assertEquals(expect,result);
    }
}
