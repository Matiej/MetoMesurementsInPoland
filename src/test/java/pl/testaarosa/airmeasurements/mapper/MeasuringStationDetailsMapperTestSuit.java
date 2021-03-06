package pl.testaarosa.airmeasurements.mapper;

import org.junit.Before;
import org.junit.Test;
import pl.testaarosa.airmeasurements.domain.MeasuringStationDetails;
import pl.testaarosa.airmeasurements.domain.dtoApi.MeasuringStationDto;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationDetailsRepository;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationDtoRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MeasuringStationDetailsMapperTestSuit {
    private MockMeasuringStationDtoRepository mockMStationDtoRepo ;
    private MockMeasuringStationDetailsRepository mockDetails;
    private MeasuringStationDetailsMapper mapper ;

    @Before
    public void init() {
        mockMStationDtoRepo = new MockMeasuringStationDtoRepository();
        mockDetails = new MockMeasuringStationDetailsRepository();
        mapper = new MeasuringStationDetailsMapper();
    }

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
