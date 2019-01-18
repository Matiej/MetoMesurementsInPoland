package pl.testaarosa.airmeasurements.services;

import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddMeasurementsServicesImplTestSuit {

    private MockStationRepository mockStationRepository;
    private MockAirRepository mockAirRepository;
    private MockSynopticRepository mockSynopticRepository;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private AddMeasurementsServiceImpl addMeasurementsService;

    @Mock
    private ApiSupplierRetriever apiSupplierRetriever;
    @Mock
    private MeasuringStationRepository measuringStationRepository;
    @Mock
    private SynopticMeasurementRepository synopticRepository;
    @Mock
    private AirMeasurementRepository airRepository;
    @Mock
    private EmailNotifierService emailNotifierService;
    @Mock
    private AddMeasurementRaportGenerator addMeasurementRaportGenerator;

    @Before
    public void init() {
        mockStationRepository = new MockStationRepository();
        mockAirRepository = new MockAirRepository();
        mockSynopticRepository = new MockSynopticRepository();
        mockStationRepository = new MockStationRepository();
    }

    @Test
    public void shouldAddAllStationMeasurements() {
        Map<String, SynopticMeasurement> synopticRepoMap = mockSynopticRepository.measurementMap();
        given(apiSupplierRetriever.synopticMeasurementProcessor()).willReturn(synopticRepoMap);
        List<MeasuringStation> stationsRepo = mockStationRepository.stations();
        given(measuringStationRepository.findAll()).willReturn(stationsRepo);
        List<MeasuringStation> stationsRepoWrong = new ArrayList<>();
        stationsRepoWrong.addAll(stationsRepo);
        stationsRepoWrong.remove(0);

        AirMeasurement airMeasurement1 = mockAirRepository.airMeasurements1().get(0);
        given(apiSupplierRetriever.airMeasurementProcessorById(stationsRepo.get(0).getStationId())).willReturn(airMeasurement1);
        AirMeasurement airMeasurement2 = mockAirRepository.airMeasurements1().get(1);
        given(apiSupplierRetriever.airMeasurementProcessorById(stationsRepo.get(1).getStationId())).willReturn(airMeasurement2);
        AirMeasurement airMeasurement3 = mockAirRepository.airMeasurements2().get(0);
        given(apiSupplierRetriever.airMeasurementProcessorById(stationsRepo.get(2).getStationId())).willReturn(airMeasurement3);
        AirMeasurement airMeasurement4 = mockAirRepository.airMeasurements2().get(1);
        given(apiSupplierRetriever.airMeasurementProcessorById(stationsRepo.get(3).getStationId())).willReturn(airMeasurement4);

        //when
        when(measuringStationRepository.save(stationsRepo.get(0))).thenReturn(stationsRepo.get(0));
        when(measuringStationRepository.save(stationsRepo.get(1))).thenReturn(stationsRepo.get(1));
        when(measuringStationRepository.save(stationsRepo.get(2))).thenReturn(stationsRepo.get(2));
        when(measuringStationRepository.save(stationsRepo.get(3))).thenReturn(stationsRepo.get(3));
        List<MeasuringStation> measuringStationList = addMeasurementsService.addMeasurementsAllStations();
        //then
        assertNotNull(measuringStationList);
        assertEquals(stationsRepo, measuringStationList);
        assertNotEquals(stationsRepoWrong, measuringStationList);
    }

    @Test
    public void shouldAddAllStationMeasurementsAndThrowsRestClientException() {
        //given
        List<MeasuringStation> stationsRepo = mockStationRepository.stations();
        int stationId = stationsRepo.get(0).getStationId();
        String expectMessage = "Can't find any air measurement for stationID: " + stationId + " because of REST API error-> null";
        given(apiSupplierRetriever.airMeasurementProcessorById(stationId)).willThrow(RestClientResponseException.class);
        //when
        when(measuringStationRepository.findAll()).thenReturn(stationsRepo);
        exception.expect(RestClientException.class);
        exception.expectMessage(expectMessage);
        //then
        addMeasurementsService.addMeasurementsAllStations();
    }

    @Test
    public void shouldAddAllStationMeasurementsAndThrowsRestClientExceptionAJ() {
        //given
        List<MeasuringStation> stationsRepo = mockStationRepository.stations();
        int stationId = stationsRepo.get(0).getStationId();
        String expectMessage = "Can't find any air measurement for stationID: " + stationId + " because of REST API error-> null";
        given(apiSupplierRetriever.airMeasurementProcessorById(stationId)).willThrow(RestClientResponseException.class);
        //when
        when(measuringStationRepository.findAll()).thenReturn(stationsRepo);
        //then
        assertThatThrownBy(() -> addMeasurementsService.addMeasurementsAllStations())
                .isInstanceOf(RestClientException.class)
                .hasMessage(expectMessage);
    }

    @Test
    public void shouldAddAllStationMeasurementsAndThrowsRuntimeException() {
        //given
        Map<String, SynopticMeasurement> synopticRepoMap = mockSynopticRepository.measurementMap();
        List<MeasuringStation> stationsRepo = mockStationRepository.stations();
        int stationId = stationsRepo.get(0).getStationId();
        String expectMessage = "There is some db problem: null";
        AirMeasurement airMeasurement = mockAirRepository.airMeasurement();
        given(measuringStationRepository.save(stationsRepo.get(0))).willThrow(HibernateException.class);
        //when
        when(apiSupplierRetriever.synopticMeasurementProcessor()).thenReturn(synopticRepoMap);
        when(measuringStationRepository.findAll()).thenReturn(stationsRepo);
        when(apiSupplierRetriever.airMeasurementProcessorById(stationId)).thenReturn(airMeasurement);
        exception.expect(RuntimeException.class);
        exception.expectMessage(expectMessage);
        //then
        addMeasurementsService.addMeasurementsAllStations();
    }

    @Test
    public void shouldAddOneStationMeasurement() {
        List<MeasuringStation> stationsRepo = mockStationRepository.stations();
        MeasuringStation measuringStation = stationsRepo.get(0);
        MeasuringStation wrongMeasuringStation = stationsRepo.get(2);
        Integer stationID = measuringStation.getStationId();
        given(measuringStationRepository.findAll()).willReturn(stationsRepo);
        AirMeasurement airMeasurement1 = mockAirRepository.airMeasurements1().get(0);
        given(apiSupplierRetriever.airMeasurementProcessorById(measuringStation.getStationId())).willReturn(airMeasurement1);
        //when
        when(measuringStationRepository.save(measuringStation)).thenReturn(measuringStation);
        MeasuringStation result = addMeasurementsService.addOneStationMeasurement(stationID);
        //then
        assertNotNull(result);
        assertEquals(measuringStation, result);
        assertNotSame(wrongMeasuringStation, result);
    }

    @Test
    public void shouldAddOneStationMeasurementAndThrowsNumberFormatException() {
        //given
        String expectedMessage = "StationID -> null is empty or format is incorrect!";
        Integer stationId = null;
        //when
        exception.expect(NumberFormatException.class);
        exception.expectMessage(expectedMessage);
        //then
        addMeasurementsService.addOneStationMeasurement(stationId);
    }

    @Test
    public void shouldAddOneStationMeasurementAndThrowsNoSuchElementException() {
        //given
        Integer stationId = 1;
        String expectedMessage = "Can't find station id: " + stationId + " in data base!";
        //when
        exception.expect(NoSuchElementException.class);
        exception.expectMessage(expectedMessage);
        //then
        addMeasurementsService.addOneStationMeasurement(stationId);
    }

    @Test
    public void shouldAddOneStationMeasurementAndThrowsRuntimeException() {
        //given
        Map<String, SynopticMeasurement> synopticRepoMap = mockSynopticRepository.measurementMap();
        List<MeasuringStation> stationsRepo = mockStationRepository.stations();
        int stationId = stationsRepo.get(0).getStationId();
        String expectMessage = "There is some db problem: null";
        AirMeasurement airMeasurement = mockAirRepository.airMeasurement();
        given(measuringStationRepository.save(stationsRepo.get(0))).willThrow(HibernateException.class);
        //when
        when(apiSupplierRetriever.synopticMeasurementProcessor()).thenReturn(synopticRepoMap);
        when(measuringStationRepository.findAll()).thenReturn(stationsRepo);
        when(apiSupplierRetriever.airMeasurementProcessorById(stationId)).thenReturn(airMeasurement);
        exception.expect(RuntimeException.class);
        exception.expectMessage(expectMessage);
        //then
        addMeasurementsService.addOneStationMeasurement(stationId);
    }

    @Test
    public void shouldAddOneStationMeasurementAndThrowsRestClientException() {
        //given
        Map<String, SynopticMeasurement> synopticRepoMap = mockSynopticRepository.measurementMap();
        List<MeasuringStation> stationsRepo = mockStationRepository.stations();
        int stationId = stationsRepo.get(0).getStationId();
        String expectMessage = "Can't find any air measurement for stationID: " + stationId + " because of REST API error-> null";
        AirMeasurement airMeasurement = mockAirRepository.airMeasurement();
        given(apiSupplierRetriever.airMeasurementProcessorById(stationId)).willThrow(RestClientResponseException.class);
        //when
        when(apiSupplierRetriever.synopticMeasurementProcessor()).thenReturn(synopticRepoMap);
        when(measuringStationRepository.findAll()).thenReturn(stationsRepo);
        exception.expect(RestClientException.class);
        exception.expectMessage(expectMessage);
        //then
        addMeasurementsService.addOneStationMeasurement(stationId);
    }

}
