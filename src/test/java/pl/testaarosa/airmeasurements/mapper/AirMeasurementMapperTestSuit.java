package pl.testaarosa.airmeasurements.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import pl.testaarosa.airmeasurements.domain.AirMeasurements;
import pl.testaarosa.airmeasurements.repositories.MockAirRepository;
import pl.testaarosa.airmeasurements.repositories.MockAirDtoRepository;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AirMeasurementMapperTestSuit {
    private final MockAirRepository mockAirRepository = new MockAirRepository();
    private final MockAirDtoRepository mockAirDtoRepository = new MockAirDtoRepository();
    private final AirMeasurementMapper airMeasurementMapper = new AirMeasurementMapper();

    @Test
    public void testMapToAirMeasurements(){
        AirMeasurements expect = mockAirRepository.airMeasurement();
        AirMeasurements result = airMeasurementMapper.mapToAirMeasurements(mockAirDtoRepository.airMeasurementsDto());
        assertEquals(expect,result);
    }
}
