package pl.testaarosa.airmeasurements.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import pl.testaarosa.airmeasurements.domain.MeasuringStationDetails;
import pl.testaarosa.airmeasurements.domain.dtoApi.MeasuringStationDto;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationDetailsRepository;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationDtoRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MeasuringStationDetailsMapperTestSuit {
    private final MockMeasuringStationDtoRepository mockMStationDtoRepo = new MockMeasuringStationDtoRepository();
    private final MockMeasuringStationDetailsRepository mockDetails = new MockMeasuringStationDetailsRepository();
    private final MeasuringStationDetailsMapper mapper = new MeasuringStationDetailsMapper();

    @Test
    public void shouldMapToAirMeasurements(){
        //given
        MeasuringStationDetails expect = mockDetails.detailsList().get(0);
        MeasuringStationDto stationDto = mockMStationDtoRepo.measuringStationDtoList().get(0);
        //when
        MeasuringStationDetails result = mapper.mapToStationDetails(stationDto);
        //then
        assertEquals(expect,result);
    }

    @Test
    public void shouldNotMapToAirMeasurements(){
        //given
        MeasuringStationDetails expect = mockDetails.detailsList().get(0);
        MeasuringStationDto stationDto = mockMStationDtoRepo.measuringStationDtoList().get(3);
        //when
        MeasuringStationDetails result = mapper.mapToStationDetails(stationDto);
        //then
        assertNotEquals(expect,result);
    }
}
