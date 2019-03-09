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
import org.springframework.web.client.RestClientException;
import pl.testaarosa.airmeasurements.domain.City;
import pl.testaarosa.airmeasurements.model.CityFeDto;
import pl.testaarosa.airmeasurements.model.OnlineMeasurementDto;
import pl.testaarosa.airmeasurements.repositories.Converter;
import pl.testaarosa.airmeasurements.repositories.MockCityFeDtoRepository;
import pl.testaarosa.airmeasurements.repositories.MockCityRepository;
import pl.testaarosa.airmeasurements.repositories.MockOnlineMeasurementRepository;
import pl.testaarosa.airmeasurements.services.OnlineMeasurementService;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class OnlineMeasurementsControllerTestSuit {

    private final Converter converter = new Converter();
    private MockOnlineMeasurementRepository mockOnlineMeasurementRepository;
    private MockCityFeDtoRepository mockCityFeDtoRepository;
    private final static String MAPPING = "/online";

    @InjectMocks
    private OnlineMeasurementsController onlineMeasurementsController;

    @Mock
    private OnlineMeasurementService measuringOnlineServices;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockOnlineMeasurementRepository = new MockOnlineMeasurementRepository();
        mockMvc = MockMvcBuilders.standaloneSetup(onlineMeasurementsController).build();
        mockCityFeDtoRepository = new MockCityFeDtoRepository();
    }

    @Test
    public void shouldFindAllMeasuringStations() throws Exception {
        //givem
        List<OnlineMeasurementDto> stationOnLineList = mockOnlineMeasurementRepository.measuringStationOnLineList();
        //when
        when(measuringOnlineServices.getAllMeasuringStations()).thenReturn(stationOnLineList);
        mockMvc.perform(get(MAPPING + "/allSt"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(converter.jsonInString(stationOnLineList)));
        //then
        verify(measuringOnlineServices, times(1)).getAllMeasuringStations();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldNotFindAllMeasuringStationsAndThrowNoSuchElementExxception() throws Exception {
        //given
        //when
        when(measuringOnlineServices.getAllMeasuringStations())
                .thenThrow(new NoSuchElementException("Find all onLine measuring stations, throws NoSuchElementException, return status 400"));
        mockMvc.perform(get(MAPPING + "/allSt"))
                .andExpect(status().is(400))
                .andReturn();
        //then
        verify(measuringOnlineServices, times(1)).getAllMeasuringStations();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldNotFindAllMeasuringStationsWrongURL() throws Exception {
        //given
        //when
        mockMvc.perform(get(MAPPING + "/allSt/404"))
                .andExpect(status().is(404))
                .andReturn();
        //then
        verify(measuringOnlineServices, times(0)).getAllMeasuringStations();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldNotFindAllMeasuringStationsAndThrowsRestClientException() throws Exception {
        //given
        //when
        when(measuringOnlineServices.getAllMeasuringStations())
                .thenThrow(new RestClientException("Find all onLine measuring stations, throws throws RestClientException, return status 500"));
        mockMvc.perform(get(MAPPING +"/allSt"))
                .andExpect(status().is(500))
                .andReturn();
        //then
        verify(measuringOnlineServices, times(1)).getAllMeasuringStations();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetGivenCityMeasuringStations() throws Exception {
        //given
        List<OnlineMeasurementDto> stationOnLines = mockOnlineMeasurementRepository.measuringStationOnLineList();
        //when
        when(measuringOnlineServices.getGivenCityMeasuringStationsWithSynopticData("wawa"))
                .thenReturn(stationOnLines);
        mockMvc.perform(get(MAPPING +"/citySt")
                .param("city", "wawa"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(converter.jsonInString(stationOnLines)));
        //then
        verify(measuringOnlineServices, times(1)).getGivenCityMeasuringStationsWithSynopticData("wawa");
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetGivenCityMeasuringStationsAndThrowNoSuchElementException() throws Exception {
        //given
        //when
        when(measuringOnlineServices.getGivenCityMeasuringStationsWithSynopticData("wawa"))
                .thenThrow(new NoSuchElementException("Get measuring station for given city, throws NoSuchElementException, then return status 400"));
        mockMvc.perform(get(MAPPING +"/citySt")
                .param("city", "wawa"))
                .andExpect(status().is(400))
                .andReturn();
        //then
        verify(measuringOnlineServices, times(1)).getGivenCityMeasuringStationsWithSynopticData("wawa");
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetGivenCityMeasuringStationsAndWrongURL() throws Exception {
        //given
        //when
        mockMvc.perform(get(MAPPING +"/citySt404")
                .param("city", "wawa"))
                .andExpect(status().is(404))
                .andReturn();
        //then
        verify(measuringOnlineServices, times(0)).getGivenCityMeasuringStationsWithSynopticData("wawa");
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetGivenCityMeasuringStationsAndThrowsIllegalArgumentException() throws Exception {
        //given
        //when
        when(measuringOnlineServices.getGivenCityMeasuringStationsWithSynopticData(""))
                .thenThrow(new IllegalArgumentException("Get measuring station for given city, throw InterruptedException, then return status 406"));
        mockMvc.perform(get(MAPPING +"/citySt")
                .param("city", ""))
                .andExpect(status().is(406));
        //then
        verify(measuringOnlineServices, times(1)).getGivenCityMeasuringStationsWithSynopticData("");
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetGivenCityMeasuringStationsAndThrowsRestClientException() throws Exception {
        //given
        //when
        when(measuringOnlineServices.getGivenCityMeasuringStationsWithSynopticData("wawa"))
                .thenThrow(new RestClientException("Get measuring station for given city, throw RestClientException, then return status 500"));
        mockMvc.perform(get(MAPPING +"/citySt")
                .param("city", "wawa"))
                .andExpect(status().is(500))
                .andReturn();
        //then
        verify(measuringOnlineServices, times(1)).getGivenCityMeasuringStationsWithSynopticData("wawa");
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetHotestOnlineMeasurement() throws Exception {
        //given
        OnlineMeasurementDto stationOnLine = mockOnlineMeasurementRepository.measuringStationOnLineList().get(0);
        //when
        when(measuringOnlineServices.getHottestOnlineStation()).thenReturn(stationOnLine);
        mockMvc.perform(get(MAPPING +"/hottest"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(converter.jsonInString(stationOnLine)));
        //then
        verify(measuringOnlineServices, times(1)).getHottestOnlineStation();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetHotestOnlineMeasurementAndThrowsNoSuchElementException() throws Exception {
        //given
        //when
        when(measuringOnlineServices.getHottestOnlineStation())
                .thenThrow(new NoSuchElementException("Get hottest online measurements, throws NoSuchElementException and return status 400"));
        mockMvc.perform(get(MAPPING + "/hottest"))
                .andExpect(status().is(400));
        //then
        verify(measuringOnlineServices, times(1)).getHottestOnlineStation();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetHotestOnlineMeasurementAndWrongURL() throws Exception {
        //given
        //when
        mockMvc.perform(get(MAPPING +"/hottest404"))
                .andExpect(status().is(404))
                .andReturn();
        //then
        verify(measuringOnlineServices, times(0)).getHottestOnlineStation();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetHotestOnlineMeasurementAndThrowsRestClientException() throws Exception {
        //given
        //when
        when(measuringOnlineServices.getHottestOnlineStation())
                .thenThrow(new RestClientException("Get Hottest online measurements, throw RestClientException and return 500 status"));
        mockMvc.perform(get(MAPPING +"/hottest"))
                .andExpect(status().is(500))
                .andReturn();
        //then
        verify(measuringOnlineServices, times(1)).getHottestOnlineStation();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetColdestOnlineMeasurement() throws Exception {
        //given
        OnlineMeasurementDto stationOnLine = mockOnlineMeasurementRepository.measuringStationOnLineList().get(0);
        //when
        when(measuringOnlineServices.getColdestOnlineStation()).thenReturn(stationOnLine);
        mockMvc.perform(get(MAPPING +"/coldest"))
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.content().json(converter.jsonInString(stationOnLine)));
        //then
        verify(measuringOnlineServices, times(1)).getColdestOnlineStation();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetColdestOnlineMeasurementAndThrowsNoSuchElementException() throws Exception {
        //given
        //when
        when(measuringOnlineServices.getColdestOnlineStation())
                .thenThrow(new NoSuchElementException("Get coldest online measurements, throws NoSuchElementException and return status 400"));
        mockMvc.perform(get(MAPPING +"/coldest"))
                .andExpect(status().is(400));
        //then
        verify(measuringOnlineServices, times(1)).getColdestOnlineStation();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetColdestOnlineMeasurementAndWrongURLTest() throws Exception {
        //given
        //when
        mockMvc.perform(get(MAPPING +"/coldest404"))
                .andExpect(status().is(404))
                .andReturn();
        //then
        verify(measuringOnlineServices, times(0)).getColdestOnlineStation();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetColdestOnlineMeasurementAndThrowsRestClientException() throws Exception {
        //given
        //when
        when(measuringOnlineServices.getColdestOnlineStation())
                .thenThrow(new RestClientException("Get Coldest online measurements, throw RestClientException and return 500 status"));
        mockMvc.perform(get(MAPPING +"/coldest"))
                .andExpect(status().is(500));
        //then
        verify(measuringOnlineServices, times(1)).getColdestOnlineStation();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetAllCites() throws Exception {
        //given
        List<CityFeDto> cityFeDtoList = mockCityFeDtoRepository.cityFeDtoList();
        //when
        when(measuringOnlineServices.onlineMeasurementsForCities()).thenReturn(cityFeDtoList);
        mockMvc.perform(get(MAPPING+"/allCities"))
                .andExpect(status().isOk());
        //when
        verify(measuringOnlineServices, times(1)).onlineMeasurementsForCities();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shouldGetAllCitesAndThrowsNoSuchElementException() throws Exception {
        //when
        when(measuringOnlineServices.onlineMeasurementsForCities())
                .thenThrow(new NoSuchElementException("Get all cities online measurements, throws NoSuchElementException and return status 400"));
        mockMvc.perform(get(MAPPING+"/allCities"))
                .andExpect(status().is(400));
        //when
        verify(measuringOnlineServices, times(1)).onlineMeasurementsForCities();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shuldGetAllCitesAndThrowsRestClientException() throws Exception {
        //when
        when(measuringOnlineServices.onlineMeasurementsForCities())
                .thenThrow(new RestClientException("Get all cites online measurements, throw RestClientException and return 500 status"));
        mockMvc.perform(get(MAPPING+"/allCities"))
                .andExpect(status().is(500));
        verify(measuringOnlineServices, times(1)).onlineMeasurementsForCities();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

    @Test
    public void shuldGetAllCitesAndWrongURLTest() throws Exception {
        //given
        //when
        mockMvc.perform(get(MAPPING+"/wrongURL"));
        verify(measuringOnlineServices, times(0)).onlineMeasurementsForCities();
        verifyNoMoreInteractions(measuringOnlineServices);
    }

}