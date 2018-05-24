package pl.testaarosa.airmeasurements.mapper;

import org.junit.Test;
import pl.testaarosa.airmeasurements.domain.AirMeasurements;
import pl.testaarosa.airmeasurements.domain.MeasuringStationDetails;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationDetailsRepository;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationDtoRepository;

import static org.junit.Assert.assertEquals;

public class MeasuringStationDetailsMapperTestSuit {
    private final MockMeasuringStationDtoRepository mockMStationDtoRepo = new MockMeasuringStationDtoRepository();
    private final MockMeasuringStationDetailsRepository mockDetails = new MockMeasuringStationDetailsRepository();
    private final MeasuringStationDetailsMapper mapper = new MeasuringStationDetailsMapper();

    @Test
    public void testMapToAirMeasurements(){
        MeasuringStationDetails expect = mockDetails.details1();
        MeasuringStationDetails result = mapper.mapToStationDetails(mockMStationDtoRepo.measuringStationDto1());
        assertEquals(expect,result);
    }
}
