package pl.testaarosa.airmeasurements.mapper;

import org.junit.Test;
import pl.testaarosa.airmeasurements.domain.dtoFe.OnlineMeasurementDto;
import pl.testaarosa.airmeasurements.domain.dtoApi.AirMeasurementDto;
import pl.testaarosa.airmeasurements.domain.dtoApi.MeasuringStationDto;
import pl.testaarosa.airmeasurements.domain.dtoApi.SynopticMeasurementDto;
import pl.testaarosa.airmeasurements.repositories.MockAirDtoRepository;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationDtoRepository;
import pl.testaarosa.airmeasurements.repositories.MockOnlineRepository;
import pl.testaarosa.airmeasurements.repositories.MockSynopticDtoRepository;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class OnlineMeasurementDtoMapperTestSuit {
private final MockOnlineRepository mockOnlineRepo = new MockOnlineRepository();
private final MockMeasuringStationDtoRepository stationDtoRepo = new MockMeasuringStationDtoRepository();
private final MockAirDtoRepository airDtoRepos = new MockAirDtoRepository();
private final MockSynopticDtoRepository synopticDtoRepos = new MockSynopticDtoRepository();
private final OnlineMeasurementMapper mapper = new OnlineMeasurementMapper();

    @Test
    public void testMapToMeasuringStation() {
        AirMeasurementDto airMeasurementDto = airDtoRepos.airMeasurementsDtos().get(0);
        SynopticMeasurementDto synopticMeasurementDto = synopticDtoRepos.mockSynopticDtoRepositories().get(1);
        MeasuringStationDto measuringStationDto = stationDtoRepo.measuringStationDtoList().get(2);
        OnlineMeasurementDto expect = mockOnlineRepo.measuringStationOnLineList().get(2);
        OnlineMeasurementDto result = mapper.mapToMeasuringStation(measuringStationDto, airMeasurementDto, synopticMeasurementDto);
        assertEquals(expect, result);

    }
    @Test
    public void mapToMeasuringStationList(){
        List<MeasuringStationDto> measuringStationDtos = stationDtoRepo.measuringStationDtoList();
        Map<Integer, AirMeasurementDto> airMap = airDtoRepos.measurementsDtoMap();
        Map<String, SynopticMeasurementDto> synMap = synopticDtoRepos.mockSynopticMeasurementsMap();
        List<OnlineMeasurementDto> resutl = mapper.mapToMeasuringStationList(measuringStationDtos, airMap, synMap);
        List<OnlineMeasurementDto> expect = mockOnlineRepo.measuringStationOnLineList();
        assertEquals(expect.size(),resutl.size());
        assertEquals(expect,resutl);
    }
}
