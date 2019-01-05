package pl.testaarosa.airmeasurements.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.MeasuringStationDetails;
import pl.testaarosa.airmeasurements.domain.dtoApi.AirMeasurementDto;
import pl.testaarosa.airmeasurements.domain.dtoApi.MeasuringStationDto;
import pl.testaarosa.airmeasurements.mapper.AirMeasurementMapper;
import pl.testaarosa.airmeasurements.mapper.MeasuringStationDetailsMapper;
import pl.testaarosa.airmeasurements.mapper.MeasuringStationMapper;
import pl.testaarosa.airmeasurements.mapper.SynopticMeasurementMapper;
import pl.testaarosa.airmeasurements.repositories.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddMeasurementsServicesImplTestSuit {

    private MockOnlineRepository mockOnlineRepository;
    private MockStationRepository mockStationRepository;
    private MockMeasuringStationDtoRepository mockMeasuringStationDtoRepository;
    private MockMeasuringStationDetailsRepository mockMeasuringStationDetailsRepository;
    private MockSynopticDtoRepository mockSynopticDtoRepository;
    private MockAirRepository mockAirRepository;
    private MockAirDtoRepository mockAirDtoRepository;

    @InjectMocks
    private AddMeasurementsServiceImpl addMeasurementsService;

    @Mock
    private ApiSupplierRetriever apiSupplierRetriever;
    @Mock
    private MeasuringStationRepository measuringStationRepository;
    @Mock
    private AirMeasurementMapper airMapper;
    @Mock
    private SynopticMeasurementMapper synopticMapper;
    @Mock
    private SynopticMeasurementRepository synopticRepository;
    @Mock
    private AirMeasurementRepository airRepository;
    @Mock
    private OnlineMeasurementService measuringOnlineServices;
    @Mock
    private EmailNotifierService emailNotifierService;
    @Mock
    private MeasuringStationMapper stMapper;
    @Mock
    private MeasuringStationDetailsMapper staDetMapper;

    @Before
    public void initi() {
        mockOnlineRepository = new MockOnlineRepository();
        mockStationRepository = new MockStationRepository();
        mockMeasuringStationDtoRepository = new MockMeasuringStationDtoRepository();
        mockMeasuringStationDetailsRepository = new MockMeasuringStationDetailsRepository();
        mockSynopticDtoRepository = new MockSynopticDtoRepository();
        mockAirRepository = new MockAirRepository();
        mockAirDtoRepository = new MockAirDtoRepository();
    }

//    @Test
//    public void shouldAddOneStationMeasurement() throws ExecutionException, InterruptedException {
//        //given
//        List<MeasuringStation> measuringStationList = mockStationRepository.stations();
//        List<MeasuringStationDetails> measuringStationDetails = mockMeasuringStationDetailsRepository.detailsList();
//        CompletableFuture<List<MeasuringStationDto>> measuringStationDtos = mockMeasuringStationDtoRepository.measuringStationDtoListCF();
//        MeasuringStation measuringStation = measuringStationList.get(0);
//        MeasuringStation expect = measuringStationList.get(0);
//        MeasuringStationDetails details = measuringStationDetails.get(0);
//        CompletableFuture<AirMeasurement> airMeasurementsCompletableFuture = mockAirRepository.airMeasurementCF();
//        CompletableFuture<AirMeasurementDto> airMeasurementsDtoCompletableFuture = mockAirDtoRepository.airMeasurementsDtosMapCF();
//        //when
//        when(apiSupplierRetriever.measuringStationApiProcessor()).thenReturn(measuringStationDtos);
//        when(staDetMapper.mapToStationDetails(measuringStationDtos.get().get(0))).thenReturn(details);
//        when(stMapper.mapToMeasuringSt(measuringStationDtos.get().get(0))).thenReturn(measuringStationList.get(0));
//        when(measuringStationRepository.existsAllByStationId(1)).thenReturn(false);
//        when(measuringStationRepository.existsAllByStationId(2)).thenReturn(true);
//        when(measuringStationRepository.existsAllByStationId(3)).thenReturn(true);
//        when(measuringStationRepository.save(measuringStation)).thenReturn(expect);
//        when(measuringStationRepository.findAll()).thenReturn(measuringStationList);
//        when(apiSupplierRetriever.synopticMeasurementProcessor())
//                .thenReturn(mockSynopticDtoRepository.mockSynopticMeasurementsMapCF());
//        when(airMapper.mapToAirMeasurements(airMeasurementsDtoCompletableFuture.get())).thenReturn(airMeasurementsCompletableFuture.get());
//        when(apiSupplierRetriever.airMeasurementProcessorById(1)).thenReturn(airMeasurementsDtoCompletableFuture);
//        when(apiSupplierRetriever.airMeasurementProcessorById(2)).thenReturn(airMeasurementsDtoCompletableFuture);
//        when(apiSupplierRetriever.airMeasurementProcessorById(3)).thenReturn(airMeasurementsDtoCompletableFuture);
//        //then
//        MeasuringStation result = addMeasurementsService.addOneStationMeasurement(2);
//        assertEquals(expect,result);
//    }
}
