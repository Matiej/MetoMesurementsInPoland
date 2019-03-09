package pl.testaarosa.airmeasurements.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.mapper.OnlineMeasurementMapper;
import pl.testaarosa.airmeasurements.model.CityFeDto;
import pl.testaarosa.airmeasurements.model.OnlineMeasurementDto;
import pl.testaarosa.airmeasurements.repositories.MockCityFeDtoRepository;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationRepository;
import pl.testaarosa.airmeasurements.repositories.MockOnlineMeasurementRepository;
import pl.testaarosa.airmeasurements.repositories.MockSynopticMeasurementRepository;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnlineMeasurementProcessorTestSuit {

    private MockOnlineMeasurementRepository onlineMeasurementRepository;
    private MockCityFeDtoRepository mockCityFeDtoRepository;
    private Map<String, SynopticMeasurement> synopticMeasurementMap;
    private Map<MeasuringStation, AirMeasurement> measurementMap;

    @InjectMocks
    private OnlineMeasurementProcessor onlineMeasurementProcessorl;

    @Mock
    private ApiSupplierRetrieverImpl apiSupplierRetriever;
    @Mock
    private OnlineMeasurementMapper measuringStationMapper;

    @Before
    public void init() {
        MockMeasuringStationRepository mockMeasuringStationRepository = new MockMeasuringStationRepository();
        MockSynopticMeasurementRepository mockSynopticMeasurementRepository = new MockSynopticMeasurementRepository();
        onlineMeasurementRepository = new MockOnlineMeasurementRepository();
        mockCityFeDtoRepository = new MockCityFeDtoRepository();
         synopticMeasurementMap = mockSynopticMeasurementRepository.measurementMap();
         measurementMap = mockMeasuringStationRepository.measurementMap();
        given(apiSupplierRetriever.synopticMeasurementProcessor()).willReturn(synopticMeasurementMap);
        given(apiSupplierRetriever.airMeasurementsAndStProcessor()).willReturn(measurementMap);
    }

    @Test
    public void shouldFillMeasuringStationsList() {
        //given
        List<OnlineMeasurementDto> expected = onlineMeasurementRepository.measuringStationOnLineList();
        //when
        when(measuringStationMapper.mapToOnlneMsDtoList(measurementMap,synopticMeasurementMap)).thenReturn(expected);
        List<OnlineMeasurementDto> result = onlineMeasurementProcessorl.fillMeasuringStationListStructure();
        //then
        assertNotNull(result);
        assertEquals(expected,result);

    }

    @Test
    public void shouldFillCityFeDtoStructure() {
        //given
        List<CityFeDto> expected = mockCityFeDtoRepository.cityFeDtoList();
        //when
        when(measuringStationMapper.mapToCityFeDto(measurementMap,synopticMeasurementMap)).thenReturn(expected);
        List<CityFeDto> result = onlineMeasurementProcessorl.fillCityFeDtoStructure();
        //then
        assertNotNull(result);
        assertEquals(expected,result);
    }

}
