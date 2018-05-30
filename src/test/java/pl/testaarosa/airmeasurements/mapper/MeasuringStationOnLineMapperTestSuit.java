package pl.testaarosa.airmeasurements.mapper;

import org.junit.Test;
import pl.testaarosa.airmeasurements.domain.MeasuringStationOnLine;
import pl.testaarosa.airmeasurements.domain.measurementsdto.AirMeasurementsDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.MeasuringStationDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.SynopticMeasurementDto;
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

public class MeasuringStationOnLineMapperTestSuit {
private final MockOnlineRepository mockOnlineRepo = new MockOnlineRepository();
private final MockMeasuringStationDtoRepository stationDtoRepo = new MockMeasuringStationDtoRepository();
private final MockAirDtoRepository airDtoRepos = new MockAirDtoRepository();
private final MockSynopticDtoRepository synopticDtoRepos = new MockSynopticDtoRepository();
private final MeasuringStationOnLineMapper mapper = new MeasuringStationOnLineMapper();

    @Test
    public void testMapToMeasuringStation() {
        AirMeasurementsDto airMeasurementsDto = airDtoRepos.airMeasurementsDtos().get(0);
        SynopticMeasurementDto synopticMeasurementDto = synopticDtoRepos.mockSynopticDtoRepositories().get(1);
        MeasuringStationDto measuringStationDto = stationDtoRepo.measuringStationDtoList().get(2);
        MeasuringStationOnLine expect = mockOnlineRepo.measuringStationOnLineList().get(2);
        MeasuringStationOnLine result = mapper.mapToMeasuringStation(measuringStationDto, airMeasurementsDto, synopticMeasurementDto);
        assertEquals(expect, result);

    }
    @Test
    public void mapToMeasuringStationList(){
        List<MeasuringStationDto> measuringStationDtos = stationDtoRepo.measuringStationDtoList();
        Map<Integer, AirMeasurementsDto> airMap = airDtoRepos.measurementsDtoMap();
        Map<String, SynopticMeasurementDto> synMap = synopticDtoRepos.measurementDtoMap();
        List<MeasuringStationOnLine> resutl = mapper.mapToMeasuringStationList(measuringStationDtos, airMap, synMap);
        List<MeasuringStationOnLine> expect = mockOnlineRepo.measuringStationOnLineList();
        assertEquals(expect.size(),resutl.size());
        assertEquals(expect,resutl);
    }
}
