package pl.testaarosa.airmeasurements.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.testaarosa.airmeasurements.domain.MeasuringStationOnLine;
import pl.testaarosa.airmeasurements.repositories.Converter;
import pl.testaarosa.airmeasurements.repositories.MockOnlineRepository;
import pl.testaarosa.airmeasurements.services.MeasuringOnlineServices;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class MeasuringOnlineControllerTestSuit {

    private final MockOnlineRepository mockOnlineRepository = new MockOnlineRepository();
    private final Converter converter = new Converter();
    private final static String MAPPING = "/online";

    @InjectMocks
    private MeasuringOnlineController measuringOnlineController;

    @Mock
    private MeasuringOnlineServices measuringOnlineServices;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(measuringOnlineController).build();
    }

    @Test
    public void shouldFindAllMeasuringStationsStatus200() throws Exception {
        List<MeasuringStationOnLine> stationOnLineList = mockOnlineRepository.measuringStationOnLineList();
        when(measuringOnlineServices.getAllMeasuringStations()).thenReturn(stationOnLineList);
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING + "/stations/all"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(converter.jsonInString(stationOnLineList)));
        verify(measuringOnlineServices, times(1)).getAllMeasuringStations();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldNotFindAllMeasuringStationsStatus400ThrowNoSuchElementExxception() throws Exception {
        List<MeasuringStationOnLine> stationOnLineList = mockOnlineRepository.measuringStationOnLineList();
        when(measuringOnlineServices.getAllMeasuringStations()).thenThrow(new NoSuchElementException("OnLine measuring stations NoSuchElementexception, return status 400"));
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING + "/stations/all"))
                .andExpect(status().is(400));
        verify(measuringOnlineServices, times(1)).getAllMeasuringStations();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldNotFindAllMeasuringStationsStatus404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING + "/stations/allStatus404"))
                .andExpect(status().is(404));
        verify(measuringOnlineServices, times(0)).getAllMeasuringStations();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldNotFindAllMeasuringStationsStatus503ThrowInterruptedException() throws Exception {
        when(measuringOnlineServices.getAllMeasuringStations()).thenThrow(new InterruptedException("OnLine measuring stations throw InterruptedException, return status 503"));
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING + "/stations/all"))
                .andExpect(status().is(503));
    }

    @Test
    public void shouldNotFindAllMeasuringStationsStatus503ThrowExecutionException() throws Exception {
        when(measuringOnlineServices.getAllMeasuringStations()).thenThrow(new ExecutionException(
                new Throwable("Find all measuring stations, throw ExecutionException and return 503 status")));
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING + "/stations/all"))
                .andExpect(status().is(503));
    }

    @Test
    public void shouldGetGivenCityMeasuringStationsStatus200() throws Exception {
        List<MeasuringStationOnLine> stationOnLines = mockOnlineRepository.measuringStationOnLineList();
        when(measuringOnlineServices.getGivenCityMeasuringStationsWithSynopticData("wawa"))
                .thenReturn(stationOnLines);
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING + "/stations/select")
                .param("city", "wawa"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(converter.jsonInString(stationOnLines)));
        verify(measuringOnlineServices, times(1)).getGivenCityMeasuringStationsWithSynopticData("wawa");
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetGivenCityMeasuringStationsAndThrowExecutionException() throws Exception {
        when(measuringOnlineServices.getGivenCityMeasuringStationsWithSynopticData("wawa"))
                .thenThrow(new ExecutionException(new Throwable("Get  online measurements for given city, throw ExecutionException and return 503 status")));
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING + "/stations/select")
                .param("city", "wawa"))
                .andExpect(status().is(503));
    }

    @Test
    public void shouldGetGivenCityMeasuringStationsAndThrowsInterruptedException() throws Exception {
        when(measuringOnlineServices.getGivenCityMeasuringStationsWithSynopticData("wawa"))
                .thenThrow(new InterruptedException
                        ("Get measuring station for given city, throw InterruptedException, then return status 503"));
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING + "/stations/select")
                .param("city", "wawa"))
                .andExpect(status().is(503));
    }

    @Test
    public void shouldGetGivenCityMeasuringStationsAndThrowNoSuchElementException() throws Exception {
        when(measuringOnlineServices.getGivenCityMeasuringStationsWithSynopticData("wawa"))
                .thenThrow(new NoSuchElementException("Get measuring station for given city, throws NoSuchElementException, then return status 400"));
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING + "/stations/select")
                .param("city", "wawa"))
                .andExpect(status().is(400));
        verify(measuringOnlineServices, times(1)).getGivenCityMeasuringStationsWithSynopticData("wawa");
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetGivenCityMeasuringStationsAndThrowsIllegalArgumentException() throws Exception {
        when(measuringOnlineServices.getGivenCityMeasuringStationsWithSynopticData(""))
                .thenThrow(new IllegalArgumentException("Get measuring station for given city, throw InterruptedException, then return status 406"));
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING + "/stations/select")
                .param("city", ""))
                .andExpect(status().is(406));
        verify(measuringOnlineServices, times(1)).getGivenCityMeasuringStationsWithSynopticData("");
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetGivenCityMeasuringStationsAndWrongURL() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING + "/stations/select404")
                .param("city", "wawa"))
                .andExpect(status().is(404));
        verify(measuringOnlineServices, times(0)).getGivenCityMeasuringStationsWithSynopticData("wawa");
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetHotestOnlineMeasurement() throws Exception {
        MeasuringStationOnLine stationOnLine = mockOnlineRepository.measuringStationOnLineList().get(0);
        when(measuringOnlineServices.getHottestOnlineStation()).thenReturn(stationOnLine);
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING + "/stations/hottest"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(converter.jsonInString(stationOnLine)));
        verify(measuringOnlineServices, times(1)).getHottestOnlineStation();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetHotestOnlineMeasurementAndThrowsExecutionException() throws Exception {
        when(measuringOnlineServices.getHottestOnlineStation())
                .thenThrow(new ExecutionException(new Throwable("Get Hottest online measurements, throw ExecutionException and return 503 status")));
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING + "/stations/hottest"))
                .andExpect(status().is(503));
    }

    @Test
    public void shouldGetHotestOnlineMeasurementAndThrowsInterruptedException() throws Exception {
        when(measuringOnlineServices.getHottestOnlineStation())
                .thenThrow(new InterruptedException("Get hottest online measurements, throw InterruptedException and return 503 status"));
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING + "/stations/hottest"))
                .andExpect(status().is(503));
    }

    @Test
    public void shouldGetHotestOnlineMeasurementAndThrowsNoSuchElementException() throws Exception {
        when(measuringOnlineServices.getHottestOnlineStation())
                .thenThrow(new NoSuchElementException("Get hottest online measurements, throws NoSuchElementException and return status 400"));
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING + "/stations/hottest"))
                .andExpect(status().is(400));
        verify(measuringOnlineServices, times(1)).getHottestOnlineStation();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetHotestOnlineMeasurementAndWrongURL() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING + "/stations/hottest404"))
                .andExpect(status().is(404));
        verify(measuringOnlineServices, times(0)).getHottestOnlineStation();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetColdestOnlineMeasurement() throws Exception {
        MeasuringStationOnLine stationOnLine = mockOnlineRepository.measuringStationOnLineList().get(0);
        when(measuringOnlineServices.getColdestOnlineStation()).thenReturn(stationOnLine);
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING + "/stations/coldest"))
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.content().json(converter.jsonInString(stationOnLine)));
        verify(measuringOnlineServices, times(1)).getColdestOnlineStation();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetColdestOnlineMeasurementAndThrowsExecutionException() throws Exception {
        when(measuringOnlineServices.getColdestOnlineStation())
                .thenThrow(new ExecutionException(new Throwable("Get Coldest online measurements, throw ExecutionException and return 503 status")));
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING + "/stations/coldest"))
                .andExpect(status().is(503));
        verify(measuringOnlineServices, times(1)).getColdestOnlineStation();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetColdestOnlineMeasurementAndThrowsInterruptedException() throws Exception {
        when(measuringOnlineServices.getColdestOnlineStation())
                .thenThrow(new InterruptedException("Get Coldest online measurements, throw InterruptedException and return 503 status"));
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING + "/stations/coldest"))
                .andExpect(status().is(503));
        verify(measuringOnlineServices, times(1)).getColdestOnlineStation();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetColdestOnlineMeasurementAndThrowsNoSuchElementException() throws Exception {
        when(measuringOnlineServices.getColdestOnlineStation())
                .thenThrow(new NoSuchElementException("Get coldest online measurements, throws NoSuchElementException and return status 400"));
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING + "/stations/coldest"))
                .andExpect(status().is(400));
        verify(measuringOnlineServices, times(1)).getColdestOnlineStation();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetColdestOnlineMeasurementAndWrontURLTest() throws Exception {
        MeasuringStationOnLine stationOnLine = mockOnlineRepository.measuringStationOnLineList().get(0);
        when(measuringOnlineServices.getColdestOnlineStation()).thenReturn(stationOnLine);
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING + "/stations/coldest404"))
                .andExpect(status().is(404));
        verify(measuringOnlineServices, times(0)).getColdestOnlineStation();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

}