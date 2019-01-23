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
import pl.testaarosa.airmeasurements.model.OnlineMeasurementDto;
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

    private MockMeasuringStationRepository mockMeasuringStationRepository;
    private MockSynopticMeasurementRepository mockSynopticMeasurementRepository;
    private MockOnlineMeasurementRepository onlineMeasurementRepository;

    @InjectMocks
    private OnlineMeasurementProcessor onlineMeasurementProcessorl;

    @Mock
    private ApiSupplierRetrieverImpl apiSupplierRetriever;
    @Mock
    private OnlineMeasurementMapper measuringStationMapper;

    @Before
    public void init() {
        mockMeasuringStationRepository = new MockMeasuringStationRepository();
        mockSynopticMeasurementRepository = new MockSynopticMeasurementRepository();
        onlineMeasurementRepository = new MockOnlineMeasurementRepository();
    }

    @Test
    public void shouldFillMeasuringStationsList() {
        //given
        Map<String, SynopticMeasurement> synopticMeasurementMap = mockSynopticMeasurementRepository.measurementMap();
        Map<MeasuringStation, AirMeasurement> measurementMap = mockMeasuringStationRepository.measurementMap();
        List<OnlineMeasurementDto> expected = onlineMeasurementRepository.measuringStationOnLineList();
        given(apiSupplierRetriever.synopticMeasurementProcessor()).willReturn(synopticMeasurementMap);
        given(apiSupplierRetriever.airMeasurementsAndStProcessor()).willReturn(measurementMap);
        //when
        when(measuringStationMapper.mapToOnlneMsDtoList(measurementMap,synopticMeasurementMap)).thenReturn(expected);
        List<OnlineMeasurementDto> result = onlineMeasurementProcessorl.fillMeasuringStationListStructure();
        //given
        assertNotNull(result);
        assertEquals(expected,result);

    }

}
