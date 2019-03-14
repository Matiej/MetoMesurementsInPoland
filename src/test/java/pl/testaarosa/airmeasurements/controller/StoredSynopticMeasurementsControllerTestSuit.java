package pl.testaarosa.airmeasurements.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.repositories.Converter;
import pl.testaarosa.airmeasurements.repositories.MockAirMeasurementRepository;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationRepository;
import pl.testaarosa.airmeasurements.repositories.MockSynopticMeasurementRepository;
import pl.testaarosa.airmeasurements.services.StoredSynopticMeasurementService;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class StoredSynopticMeasurementsControllerTestSuit {

    private final MockSynopticMeasurementRepository mockSynopticMeasurementRepository = new MockSynopticMeasurementRepository();
    private final Converter converter = new Converter();
    private final static String MAPPING = "/storedSynoptic";
    private final static String NOOFRESULTS = "10";

    @InjectMocks
    private StoredSynopticMeasuremetsController controller;

    @Mock
    private StoredSynopticMeasurementService service;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void shouldFindAllSynopticMeasurementsByDate() throws Exception {
        //given
        List<SynopticMeasurement> synopticMeasurements = mockSynopticMeasurementRepository.synopticMeasurementsOrderHottest();
        //when
        Mockito.when((service.getSynopticMeasuremets("2018-05-11"))).thenReturn(synopticMeasurements);
        mockMvc.perform(get(MAPPING +"/byDate")
                .param("date", "2018-05-11"))
                .andExpect(status().is(200))
                .andExpect(content().json(converter.jsonInString(synopticMeasurements)));
        //then
        verify(service, times(1)).getSynopticMeasuremets("2018-05-11");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindAllSynopticMeasurementsByDateAndThrowsNoSuchElementException() throws Exception {
        //given
        List<SynopticMeasurement> synopticMeasurements = new ArrayList<>();
        //when
        Mockito.when((service.getSynopticMeasuremets("2018-05-11")))
                .thenThrow(new NoSuchElementException("Find all synoptic measurements by given date, throws NoSuchElementException return status 400"))
                .thenReturn(synopticMeasurements);
        mockMvc.perform(get(MAPPING +"/byDate")
                .param("date", "2018-05-11"))
                .andExpect(status().is(400));
        //then
        verify(service, times(1)).getSynopticMeasuremets("2018-05-11");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindAllSynopticMeasurementsByDateWrongURL() throws Exception {
        //given
        //when
        mockMvc.perform(get(MAPPING +"/byDate404")
                .param("date", "2018-05-11"))
                .andExpect(status().is(404));
        //then
        verify(service, times(0)).getSynopticMeasuremets("2018-05-11");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindAllSynopticMeasurementsByDateAndThrowsDateTimeException() throws Exception {
        //given
        List<SynopticMeasurement> synopticMeasurements = new ArrayList<>();
        //when
        Mockito.when((service.getSynopticMeasuremets("2018www-05-11")))
                .thenThrow(new DateTimeException("Find all synoptic measurements by given date, throws NDateTimeException return status 406"))
                .thenReturn(synopticMeasurements);
        mockMvc.perform(get(MAPPING +"/byDate")
                .param("date", "2018www-05-11"))
                .andExpect(status().is(406))
                .andReturn();
        //then
        verify(service, times(1)).getSynopticMeasuremets("2018www-05-11");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindAllSynopticMeasurementsByDateAndThrowsDataIntegrityViolationException() throws Exception {
        //given
        List<SynopticMeasurement> synopticMeasurements = new ArrayList<>();
        //when
        Mockito.when((service.getSynopticMeasuremets("2018-05-11")))
                .thenThrow(new DataIntegrityViolationException("Find all synoptic measurements by given date, throws DataIntegrityViolationException return status 503"))
                .thenReturn(synopticMeasurements);
        mockMvc.perform(get(MAPPING +"/byDate")
                .param("date", "2018-05-11"))
                .andExpect(status().is(503))
                .andReturn();
        //then
        verify(service, times(1)).getSynopticMeasuremets("2018-05-11");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindTopTenHottestPlaces() throws Exception {
        //given
        List<SynopticMeasurement> measurementsList = mockSynopticMeasurementRepository.synopticMeasurementsOrderHottest();
        //when
        Mockito.when(service.getHottestPlaces(null)).thenReturn(measurementsList);
        mockMvc.perform(get(MAPPING +"/hottestTop"))
                .andExpect(status().is(200))
                .andExpect(content().json(converter.jsonInString(measurementsList)));
        //then
        verify(service, times(1)).getHottestPlaces(null);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindTopTenHottestPlacesAndThrowsNoSuchElementException() throws Exception {
        //given
        //when
        Mockito.when(service.getHottestPlaces(null))
                .thenThrow(new NoSuchElementException("Find top ten hottest measurements by given date, throws NoSuchElementException and return status 400"));
        mockMvc.perform(get(MAPPING +"/hottestTop"))
                .andExpect(status().is(400))
                .andReturn();
        //then
        verify(service, times(1)).getHottestPlaces(null);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindTopTenHottestPlacesAndWrongURL() throws Exception {
        //given
        //when
        mockMvc.perform(get(MAPPING + "/hottestTop404"))
                .andExpect(status().is(404))
                .andReturn();
        //then
        verify(service, times(0)).getHottestPlaces(NOOFRESULTS);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindTopTenHottestPlacesAndThrowsDataIntegrityViolationException() throws Exception {
        //given
        //when
        Mockito.when(service.getHottestPlaces(null))
                .thenThrow(new DataIntegrityViolationException("Find top ten hottest measurements by given date, throws DataIntegrityViolationException and return status 400"));
        mockMvc.perform(get(MAPPING + "/hottestTop"))
                .andExpect(status().is(503))
                .andReturn();
        //then
        verify(service, times(1)).getHottestPlaces(null);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindTopTenColdestPlaces() throws Exception {
        //given
        List<SynopticMeasurement> measurementsList = mockSynopticMeasurementRepository.synopticMeasurementsOrderHottest();
        //when
        when(service.getColdestPlaces(null)).thenReturn(measurementsList);
        mockMvc.perform(get(MAPPING +"/coldestTop"))
                .andExpect(status().isOk())
                .andExpect(content().json(converter.jsonInString(measurementsList)));
        //then
        verify(service, times(1)).getColdestPlaces(null);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindTopTenColdestPlacesAndThrowsNoSuchElementException() throws Exception {
        //given
        //when
        when(service.getColdestPlaces(null))
                .thenThrow(new NoSuchElementException("Find top ten coldest measurements by given date, throws NoSuchElementException and return status 400"));
        mockMvc.perform(get(MAPPING +"/coldestTop"))
                .andExpect(status().is(400))
                .andReturn();
        //then
        verify(service, times(1)).getColdestPlaces(null);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindTopTenColdestPlacesAndWrongURL() throws Exception {
        //given
        //when
        mockMvc.perform(get(MAPPING + "coldestTop404"))
                .andExpect(status().is(404))
                .andReturn();
        //then
        verify(service, times(0)).getColdestPlaces(NOOFRESULTS);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindTopTenColdestPlacesAndThrowsDataIntegrityViolationException() throws Exception {
        //given
        //when
        when(service.getColdestPlaces(null))
                .thenThrow(new DataIntegrityViolationException("Find top ten coldest measurements by given date, throws DataIntegrityViolationException and return status 503"));
        mockMvc.perform(get(MAPPING + "/coldestTop"))
                .andExpect(status().is(503))
                .andReturn();
        //then
        verify(service, times(1)).getColdestPlaces(null);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindAHottestPlaceByDate() throws Exception {
        //given
        SynopticMeasurement synopticMeasurement = mockSynopticMeasurementRepository.synopticMeasurementsOrderColdest().get(0);
        //when
        Mockito.when(service.getHottestPlaceGivenDate("2018-05-05")).thenReturn(synopticMeasurement);
        mockMvc.perform(get(MAPPING +"/hottestDate")
                .param("date", "2018-05-05"))
                .andExpect(status().is(200))
                .andExpect(content().json(converter.jsonInString(synopticMeasurement)));
        //then
        verify(service, times(1)).getHottestPlaceGivenDate("2018-05-05");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindAHottestPlaceByDateAndTrhowsNoSuchElementException() throws Exception {
        //given
        //when
        Mockito.when(service.getHottestPlaceGivenDate("2018-05-05"))
                .thenThrow(new NoSuchElementException("Find hottest measurements by given date, throws NoSuchElementException and return status 400"));
        mockMvc.perform(get(MAPPING +"/hottestDate")
                .param("date", "2018-05-05"))
                .andExpect(status().is(400))
                .andReturn();
        //then
        verify(service, times(1)).getHottestPlaceGivenDate("2018-05-05");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindAHottestPlaceByDateAndWrongURL() throws Exception {
        //given
        //when
        mockMvc.perform(get(MAPPING +"/hottestDate404")
                .param("date", "2018-05-05"))
                .andExpect(status().is(404))
                .andReturn();
        //then
        verify(service, times(0)).getHottestPlaceGivenDate("2018-05-05");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindAHottestPlaceByDateAndTrhowsDateTimeException() throws Exception {
        //given
        //when
        Mockito.when(service.getHottestPlaceGivenDate("2018www-05-05"))
                .thenThrow(new DateTimeException("Find hottest measurements by given date, throws DateTimeException and return status 406"));
        mockMvc.perform(get(MAPPING +"/hottestDate")
                .param("date", "2018www-05-05"))
                .andExpect(status().is(406))
                .andReturn();
        //then
        verify(service, times(1)).getHottestPlaceGivenDate("2018www-05-05");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindAHottestPlaceByDateAndTrhowsDataIntegrityViolationException() throws Exception {
        //given
        //when
        Mockito.when(service.getHottestPlaceGivenDate("2018-05-05"))
                .thenThrow(new DataIntegrityViolationException("Find hottest measurements by given date, throws DataIntegrityViolationException and return status 503"));
        mockMvc.perform(get(MAPPING + "/hottestDate")
                .param("date", "2018-05-05"))
                .andExpect(status().is(503))
                .andReturn();
        //then
        verify(service, times(1)).getHottestPlaceGivenDate("2018-05-05");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindColdestPlaceByDate() throws Exception {
        //given
        SynopticMeasurement synopticMeasurement = mockSynopticMeasurementRepository.synopticMeasurementsOrderHottest().get(0);
        //when
        Mockito.when(service.getColdestPlaceGivenDate("2018-05-05")).thenReturn(synopticMeasurement);
        mockMvc.perform(get(MAPPING +"/coldestDate")
                .param("date", "2018-05-05"))
                .andExpect(status().isOk())
                .andExpect(content().json(converter.jsonInString(synopticMeasurement)));
        //then
        verify(service, times(1)).getColdestPlaceGivenDate("2018-05-05");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindColdestPlaceByDateAndTrhowsNoSuchElementException() throws Exception {
        //given //when
        Mockito.when(service.getColdestPlaceGivenDate("2018-05-05"))
                .thenThrow(new NoSuchElementException("Find coldest measurements by given date, throws NoSuchElementException and return status 400"));
        mockMvc.perform(get(MAPPING + "/coldestDate")
                .param("date", "2018-05-05"))
                .andExpect(status().is(400))
                .andReturn();
        //then
        verify(service, times(1)).getColdestPlaceGivenDate("2018-05-05");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindColdestPlaceByDateAndWrongURL() throws Exception {
        //givem //when
        mockMvc.perform(get(MAPPING + "/coldestDate404")
                .param("date", "2018-05-05"))
                .andExpect(status().is(404))
                .andReturn();
        //then
        verify(service, times(0)).getColdestPlaceGivenDate("2018-05-05");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindColdestPlaceByDateAndTrhowsDateTimeException() throws Exception {
        //givem //when
        Mockito.when(service.getColdestPlaceGivenDate("2018-05-05"))
                .thenThrow(new DateTimeException("Find coldest measurements by given date, throws DateTimeException and return status 406"));
        mockMvc.perform(get(MAPPING +"/coldestDate")
                .param("date", "2018-05-05"))
                .andExpect(status().is(406))
                .andReturn();
        //then
        verify(service, times(1)).getColdestPlaceGivenDate("2018-05-05");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindColdestPlaceByDateAndTrhowsDataIntegrityViolationException() throws Exception {
        //given //when
        Mockito.when(service.getColdestPlaceGivenDate("2018-05-05"))
                .thenThrow(new DataIntegrityViolationException("Find coldest measurements by given date, throws DataIntegrityViolationException and return status 503"));
        mockMvc.perform(get(MAPPING + "/coldestDate")
                .param("date", "2018-05-05"))
                .andExpect(status().is(503))
                .andReturn();
        //then
        verify(service, times(1)).getColdestPlaceGivenDate("2018-05-05");
        verifyNoMoreInteractions(service);
    }
}
