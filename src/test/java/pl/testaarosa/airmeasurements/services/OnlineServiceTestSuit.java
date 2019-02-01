package pl.testaarosa.airmeasurements.services;

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
import pl.testaarosa.airmeasurements.model.OnlineMeasurementDto;
import pl.testaarosa.airmeasurements.repositories.MockOnlineMeasurementRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnlineServiceTestSuit {

    private MockOnlineMeasurementRepository mockOnlineMeasurementRepository;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private OnlineMeasurementServiceImpl service;

    @Mock
    private OnlineMeasurementProcessor msProcessor;

    @Before
    public void init() {
        mockOnlineMeasurementRepository = new MockOnlineMeasurementRepository();
    }

    @Test
    public void shouldGetAllMeasuringStations() {
        //given
        List<OnlineMeasurementDto> expect = mockOnlineMeasurementRepository.measuringStationOnLineList();
        int expectsize = 4;
        //when
        when(msProcessor.fillMeasuringStationListStructure())
                .thenReturn(mockOnlineMeasurementRepository.measuringStationOnLineList());
        int resultsize = service.getAllMeasuringStations().size();
        List<OnlineMeasurementDto> result = service.getAllMeasuringStations();
        //then
        assertEquals(expect, result);
        assertEquals(expectsize, resultsize);
    }

    @Test
    public void shouldGetAllMeasuringStationsAndThrowsNoSuchElementException() {
        //given
        //when
        exception.expect(NoSuchElementException.class);
        exception.expectMessage("Can't find any online measuring stations");
        //then
        service.getAllMeasuringStations();
    }

    @Test
    public void shouldGetAllMeasuringStationsAndThrowsNoSuchElementExceptionJUvintage() {
        //given
        String expectedMessage = "Can't find any online measuring stations";
        //when
        List<OnlineMeasurementDto> result = null;
        try {
            result = service.getAllMeasuringStations();
        } catch (NoSuchElementException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
        //then
        assertNull(result);
    }

    @Test
    public void shouldGetAllMeasuringStationsAndThrowsRestClientException() {
        //given
        given(msProcessor.fillMeasuringStationListStructure()).willThrow(RestClientResponseException.class);
        //when
        exception.expect(RestClientException.class);
        //then
        service.getAllMeasuringStations();
    }

    @Test
    public void shouldFindWarszawaCityMeasuringStationsWithSynopticData() throws ExecutionException, InterruptedException {
        //given
        List<OnlineMeasurementDto> expect = mockOnlineMeasurementRepository.measuringStationOnLineList();
        expect.remove(3);
        expect.remove(2);
        int expectSize = 2;
        //when
        when(msProcessor.fillMeasuringStationListStructure()).thenReturn(expect);
        List<OnlineMeasurementDto> result = service.getGivenCityMeasuringStationsWithSynopticData("Warsz");
        int resultSize = result.size();
        //then
        assertNotNull(result);
        assertEquals(expect, result);
        assertEquals(expectSize, resultSize);
    }

    @Test
    public void shouldFindKrakowCityMeasuringStationsWithSynopticData() throws ExecutionException, InterruptedException {
        //given
        List<OnlineMeasurementDto> expect = mockOnlineMeasurementRepository.measuringStationOnLineList();
        expect.remove(0);
        expect.remove(0);
        expect.remove(0);
        int expectSize = 1;
        //when
        when(msProcessor.fillMeasuringStationListStructure()).thenReturn(expect);
        List<OnlineMeasurementDto> result = service.getGivenCityMeasuringStationsWithSynopticData("krA");
        int resultSize = result.size();
        //then
        assertNotNull(result);
        assertEquals(expect, result);
        assertEquals(expectSize, resultSize);
    }

    @Test
    public void shouldFindGivenCityMeasuringStationsWithSynopticDataAndThrowsIllegalArgumentException() {
        //given
        String expectedMessage = "City name can't be empty!";
        //when
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(expectedMessage);
        //then
        service.getGivenCityMeasuringStationsWithSynopticData("");
    }

    @Test
    public void shouldFindGivenCityMeasuringStationsWithSynopticDataAndThrowsNoSuchElementException() {
        //given
        String stationCity = "Radom";
        String expectedMessage = "Cant't find any stations for city: " + stationCity;
        //when
        exception.expect(NoSuchElementException.class);
        exception.expectMessage(expectedMessage);
        //then
        service.getGivenCityMeasuringStationsWithSynopticData(stationCity);
    }

    @Test
    public void shouldFindGivenCityMeasuringStationsWithSynopticDataAndThrowsNoSuchElementExceptionJUVintage() throws ExecutionException, InterruptedException {
        //given
        List<OnlineMeasurementDto> expect = mockOnlineMeasurementRepository.measuringStationOnLineList();
        String stationCity = "Radom";
        String expectedMessage = "Cant't find any stations for city: " + stationCity;
        //when
        when(msProcessor.fillMeasuringStationListStructure()).thenReturn(expect);
        //then
        List<OnlineMeasurementDto> result = null;
        try {
            result = service.getGivenCityMeasuringStationsWithSynopticData(stationCity);
        } catch (NoSuchElementException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
        assertNull(result);
    }

    @Test
    public void shouldFindGivenCityMeasuringStationsWithSynopticDataAndThrowsRestClientException() throws ExecutionException, InterruptedException {
        //given
        given(msProcessor.fillMeasuringStationListStructure()).willThrow(RestClientResponseException.class);
        String expectedMessage = "External REST API server error! Can't get online measurements for all stations.-> null";
        String stationCity = "Warsz";
        //when
        exception.expect(RestClientException.class);
        exception.expectMessage(expectedMessage);
        //then
        service.getGivenCityMeasuringStationsWithSynopticData(stationCity);
    }


    @Test
    public void shouldFindHotestOnlineStation() {
        //given
        List<OnlineMeasurementDto> repository = mockOnlineMeasurementRepository.measuringStationOnLineList();
        OnlineMeasurementDto expect = repository.get(2);
        //when
        when(msProcessor.fillMeasuringStationListStructure()).thenReturn(repository);
        //then
        OnlineMeasurementDto result = null;
        try {
            result = service.getHottestOnlineStation();
        } catch (RestClientException | NoSuchElementException e) {
            assertEquals("some not important message", e.getMessage());
        }
        assertNotNull(result);
        assertEquals(expect, result);
    }

    @Test
    public void shouldFindHotestOnlineStationAndThrowsNoSuchElementException() {
        //given
        String expectedMessage = "Can't find hottest measurement online";
        //when
        exception.expect(NoSuchElementException.class);
        exception.expectMessage(expectedMessage);
        //then
        service.getHottestOnlineStation();
    }

    @Test
    public void shouldFindHotestOnlineStationAndThrowsNoSuchElementExceptionJUVintage() {
        //given
        String expectedMessage = "Can't find hottest measurement online";
        //when
        when(msProcessor.fillMeasuringStationListStructure()).thenReturn(new ArrayList<>());
        //then
        try {
            service.getHottestOnlineStation();
        } catch (NoSuchElementException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void shouldFindHotestOnlineStationAndThrowsRestClientException() {
        //given
        given(msProcessor.fillMeasuringStationListStructure()).willThrow(RestClientResponseException.class);
        String expectedMessage = "External REST API server error! Can't get online measurements for all stations.-> null";
        //when
        exception.expect(RestClientException.class);
        exception.expectMessage(expectedMessage);
        //then
        service.getHottestOnlineStation();
    }

    @Test
    public void shouldfindColdestOnlineStation() {
        //given
        List<OnlineMeasurementDto> repository = mockOnlineMeasurementRepository.measuringStationOnLineList();
        OnlineMeasurementDto expect = repository.get(0);
        //when
        when(msProcessor.fillMeasuringStationListStructure()).thenReturn(repository);
        //then
        OnlineMeasurementDto result = null;
        try {
            result = service.getColdestOnlineStation();
        } catch (RestClientException | NoSuchElementException e) {
            assertEquals("some not important message", e.getMessage());
        }
        assertNotNull(result);
        assertEquals(expect, result);
    }

    @Test
    public void shouldfindColdestOnlineStationAndThrowsNoSuchElementException() {
        //given
        String expectedMessage = "Can't find coldest measurement online";
        //when
        exception.expect(NoSuchElementException.class);
        exception.expectMessage(expectedMessage);
        //then
        service.getColdestOnlineStation();
    }

    @Test
    public void shouldfindColdestOnlineStationAndThrowsNoSuchElementExceptionJUvintage() {
        //given
        String expectedMessage = "Can't find coldest measurement online";
        given(msProcessor.fillMeasuringStationListStructure()).willReturn(new ArrayList<>());
        //when
        OnlineMeasurementDto coldestOnlineStation = null;
        try {
            coldestOnlineStation = service.getColdestOnlineStation();
        } catch (NoSuchElementException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
        assertNull(coldestOnlineStation);
    }

    @Test
    public void shouldfindColdestOnlineStationAndThrowsRestClientException() {
        //given
        given(msProcessor.fillMeasuringStationListStructure()).willThrow(RestClientResponseException.class);
        String expectedMessage = "External REST API server error! Can't get coldest measurement online for station";
        //when
        exception.expect(RestClientException.class);
        exception.expectMessage(expectedMessage);
        //then
        service.getColdestOnlineStation();
    }
}
