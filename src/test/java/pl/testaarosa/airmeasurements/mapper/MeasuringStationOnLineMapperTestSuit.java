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

import static org.junit.Assert.assertEquals;

public class MeasuringStationOnLineMapperTestSuit {
private final MockOnlineRepository mockOnlineRepository = new MockOnlineRepository();
private final MockMeasuringStationDtoRepository stationDtoRepository = new MockMeasuringStationDtoRepository();
private final MockAirDtoRepository mockAirDtoRepository = new MockAirDtoRepository();
private final MockSynopticDtoRepository synopticDtoRepository = new MockSynopticDtoRepository();
private final MeasuringStationOnLineMapper mapper = new MeasuringStationOnLineMapper();

    @Test
    public void testMapToMeasuringStation() {
        MeasuringStationOnLine expect = mockOnlineRepository.measuringStationOnLineList().get(2);
        MeasuringStationDto measuringStationDto = stationDtoRepository.measuringStationDtoList().get(0);
        AirMeasurementsDto airMeasurementsDto = mockAirDtoRepository.airMeasurementsDtos().get(0);
        SynopticMeasurementDto synopticMeasurementDto = synopticDtoRepository.mockSynopticDtoRepositories().get(1);
        MeasuringStationOnLine result = mapper.mapToMeasuringStation(measuringStationDto, airMeasurementsDto, synopticMeasurementDto);
        assertEquals(expect, result);

    }
}
