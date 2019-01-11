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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.AirMeasurementLevel;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.repositories.Converter;
import pl.testaarosa.airmeasurements.repositories.MockAirRepository;
import pl.testaarosa.airmeasurements.repositories.MockStationRepository;
import pl.testaarosa.airmeasurements.repositories.MockSynopticRepository;
import pl.testaarosa.airmeasurements.services.StoredMeasurementsService;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class StoredMeasurementsControllerTestSuit {
    private final MockStationRepository mockStationRepository = new MockStationRepository();
    private final MockAirRepository mockAirRepository = new MockAirRepository();
    private final MockSynopticRepository mockSynopticRepository = new MockSynopticRepository();
    private final Converter converter = new Converter();
    private final static String MAPPING = "/stored";

    @InjectMocks
    private StoredMeasurementsController controller;

    @Mock
    private StoredMeasurementsService service;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void shouldFindAllStationsWithAllMeasurements() throws Exception {
        //given
        List<MeasuringStation> stations = mockStationRepository.stations();
        //when
        Mockito.when(service.findAll()).thenReturn(stations);
        String jsonContent = converter.jsonInString(stations);
        mockMvc.perform(get(MAPPING + "/allMeasurements"))
                .andExpect(status().is(200))
                .andExpect(content().json(jsonContent));
        //then
        verify(service, times(1)).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindAllStationsWithAllMeasurementsAndThrowsNoSuchElementException() throws Exception {
        //given
        List<MeasuringStation> stations = mockStationRepository.stations();
        //when
        Mockito.when(service.findAll())
                .thenThrow(new NoSuchElementException("Find all stations with all measurements, throws NoSuchElementException and return status 400"));
        mockMvc.perform(get(MAPPING +"/allMeasurements"))
                .andExpect(status().is(400))
                .andReturn();
        //then
        verify(service, times(1)).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindAllStationsWithAllMeasurementsAndWrongURL() throws Exception {
        //given
        //when
        mockMvc.perform(get(MAPPING + "/allMeasurements404"))
                .andExpect(status().is(404))
                .andReturn();
        //then
        verify(service, times(0)).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindAllStationsWithAllMeasurementsAndThrowsDataIntegrityViolationException() throws Exception {
        //given
        //when
        Mockito.when(service.findAll())
                .thenThrow(new DataIntegrityViolationException("Find all stations with all measurements, throws DataIntegrityViolationException and return status 503"));
        mockMvc.perform(get(MAPPING + "/allMeasurements"))
                .andExpect(status().is(503))
                .andReturn();
        //then
        verify(service, times(1)).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindAllSynopticMeasurementsByDate() throws Exception {
        //given
        List<SynopticMeasurement> synopticMeasurements = mockSynopticRepository.synopticMeasurementsOrderHottest();
        //when
        Mockito.when((service.getSynopticMeasuremets("2018-05-11"))).thenReturn(synopticMeasurements);
        mockMvc.perform(get(MAPPING +"/synopticMeasurementsDate")
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
        mockMvc.perform(get(MAPPING +"/synopticMeasurementsDate")
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
        mockMvc.perform(get(MAPPING +"/synopticMeasurementsDate404")
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
        mockMvc.perform(get(MAPPING +"/synopticMeasurementsDate")
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
        mockMvc.perform(get(MAPPING +"/synopticMeasurementsDate")
                .param("date", "2018-05-11"))
                .andExpect(status().is(503))
                .andReturn();
        //then
        verify(service, times(1)).getSynopticMeasuremets("2018-05-11");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindPlaceByAirQuality() throws Exception {
        //given
        List<AirMeasurement> airMeasurementList = mockAirRepository.airMeasurements1();
        //when
        Mockito.when(service.getAirMeasurementsByLevel(AirMeasurementLevel.BAD)).thenReturn(airMeasurementList);
        mockMvc.perform(get(MAPPING + "/airMeasurementsQy")
                .param("airLevel", "BAD"))
                .andExpect(status().isOk())
                .andExpect(content().json(converter.jsonInString(airMeasurementList)));
        //then
        verify(service, times(1)).getAirMeasurementsByLevel(AirMeasurementLevel.BAD);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindPlaceByAirQualityAndThrowsNoSuchElementException() throws Exception {
        //givem
        //when
        Mockito.when(service.getAirMeasurementsByLevel(AirMeasurementLevel.BAD))
                .thenThrow(new NoSuchElementException("Find all air measurements by given air quality level, throws NoSuchElementException and return status 400"));
        mockMvc.perform(get(MAPPING +"/airMeasurementsQy")
                .param("airLevel", "BAD"))
                .andExpect(status().is(400))
                .andReturn();
        //then
        verify(service, times(1)).getAirMeasurementsByLevel(AirMeasurementLevel.BAD);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindPlaceByAirQualityWrongURL() throws Exception {
        //given
        //when
        mockMvc.perform(get(MAPPING + "/airMeasurementsQy404")
                .param("airLevel", "BAD"))
                .andExpect(status().is(404))
                .andReturn();
        //then
        verify(service, times(0)).getAirMeasurementsByLevel(AirMeasurementLevel.BAD);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindPlaceByAirQualityAndThrowsIllegalArgumentException() throws Exception {
        //given
        //when
        Mockito.when(service.getAirMeasurementsByLevel(AirMeasurementLevel.BAD))
                .thenThrow(new IllegalArgumentException("Find all air measurements by given air quality level, throws NoSuchElementException and return status 406"));
        mockMvc.perform(get(MAPPING +"/airMeasurementsQy")
                .param("airLevel", "BAD"))
                .andExpect(status().is(406))
                .andReturn();
        //then
        verify(service, times(1)).getAirMeasurementsByLevel(AirMeasurementLevel.BAD);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindPlaceByAirQualityAndThrowsDataIntegrityViolationException() throws Exception {
        //given
        //when
        Mockito.when(service.getAirMeasurementsByLevel(AirMeasurementLevel.BAD))
                .thenThrow(new DataIntegrityViolationException("Find all air measurements by given air quality level, throws DataIntegrityViolationException and return status 503"));
        mockMvc.perform(get(MAPPING + "/airMeasurementsQy")
                .param("airLevel", "BAD"))
                .andExpect(status().is(503))
                .andReturn();
        //then
        verify(service, times(1)).getAirMeasurementsByLevel(AirMeasurementLevel.BAD);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindAllAirMeasurementsByDate() throws Exception {
        //given
        List<AirMeasurement> airMeasurementList = mockAirRepository.airMeasurements1();
        //when
        Mockito.when((service.getAirMeasurementsByDate("2018-05-05"))).thenReturn(airMeasurementList);
        String jsonContent = converter.jsonInString(airMeasurementList);
        mockMvc.perform(get(MAPPING +"/airMeasurementsDate")
                .param("date", "2018-05-05"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(jsonContent));
        //then
        verify(service, times(1)).getAirMeasurementsByDate("2018-05-05");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindAllAirMeasurementsByDateAndThrowsNoSuchElementException() throws Exception {
        //given
        //when
        Mockito.when((service.getAirMeasurementsByDate("2018-05-05"))).
                thenThrow(new NoSuchElementException("Find all air measurements by given date, throws NoSuchElementException and return status 400"));
        mockMvc.perform(get(MAPPING +"/airMeasurementsDate")
                .param("date", "2018-05-05"))
                .andExpect(status().is(400))
                .andReturn();
        //then
        verify(service, times(1)).getAirMeasurementsByDate("2018-05-05");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindAllAirMeasurementsByDateWrongURL() throws Exception {
        //given
        //when
        mockMvc.perform(get(MAPPING +"/airMeasurementsDate404")
                .param("date", "2018-05-05"))
                .andExpect(status().is(404))
                .andReturn();
        //then
        verify(service, times(0)).getAirMeasurementsByDate("2018-05-05");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindAllAirMeasurementsByDateAndThrowsDateTimeException() throws Exception {
        //given
        //when
        Mockito.when((service.getAirMeasurementsByDate("2018-05-05"))).
                thenThrow(new DateTimeException("Find all air measurements by given date, throws NoSuchElementException and return status 406"));
        mockMvc.perform(get(MAPPING +"/airMeasurementsDate")
                .param("date", "2018-05-05"))
                .andExpect(status().is(406))
                .andReturn();
        //then
        verify(service, times(1)).getAirMeasurementsByDate("2018-05-05");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindAllAirMeasurementsByDateAndThrowsDataIntegrityViolationException() throws Exception {
        //given
        //when
        Mockito.when((service.getAirMeasurementsByDate("2018-05-05"))).
                thenThrow(new DataIntegrityViolationException("Find all air measurements by given date, throws DataIntegrityViolationException and return status 406"));
        mockMvc.perform(get(MAPPING +"/airMeasurementsDate")
                .param("date", "2018-05-05"))
                .andExpect(status().is(503))
                .andReturn();
        //then
        verify(service, times(1)).getAirMeasurementsByDate("2018-05-05");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindTopTenHottestPlaces() throws Exception {
        //given
        List<SynopticMeasurement> measurementsList = mockSynopticRepository.synopticMeasurementsOrderHottest();
        //when
        Mockito.when(service.getHottestPlaces()).thenReturn(measurementsList);
        mockMvc.perform(get(MAPPING +"/hottestTop"))
                .andExpect(status().is(200))
                .andExpect(content().json(converter.jsonInString(measurementsList)));
        //then
        verify(service, times(1)).getHottestPlaces();
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindTopTenHottestPlacesAndThrowsNoSuchElementException() throws Exception {
        //given
        //when
        Mockito.when(service.getHottestPlaces())
                .thenThrow(new NoSuchElementException("Find top ten hottest measurements by given date, throws NoSuchElementException and return status 400"));
        mockMvc.perform(get(MAPPING +"/hottestTop"))
                .andExpect(status().is(400))
                .andReturn();
        //then
        verify(service, times(1)).getHottestPlaces();
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
        verify(service, times(0)).getHottestPlaces();
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindTopTenHottestPlacesAndThrowsDataIntegrityViolationException() throws Exception {
        //given
        //when
        Mockito.when(service.getHottestPlaces())
                .thenThrow(new DataIntegrityViolationException("Find top ten hottest measurements by given date, throws DataIntegrityViolationException and return status 400"));
        mockMvc.perform(get(MAPPING + "/hottestTop"))
                .andExpect(status().is(503))
                .andReturn();
        //then
        verify(service, times(1)).getHottestPlaces();
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindTopTenColdestPlaces() throws Exception {
        //given
        List<SynopticMeasurement> measurementsList = mockSynopticRepository.synopticMeasurementsOrderHottest();
        //when
        when(service.getColdestPlaces()).thenReturn(measurementsList);
        mockMvc.perform(get(MAPPING +"/coldestTop"))
                .andExpect(status().isOk())
                .andExpect(content().json(converter.jsonInString(measurementsList)));
        //then
        verify(service, times(1)).getColdestPlaces();
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindTopTenColdestPlacesAndThrowsNoSuchElementException() throws Exception {
        //given
        //when
        when(service.getColdestPlaces())
                .thenThrow(new NoSuchElementException("Find top ten coldest measurements by given date, throws NoSuchElementException and return status 400"));
        mockMvc.perform(get(MAPPING +"/coldestTop"))
                .andExpect(status().is(400))
                .andReturn();
        //then
        verify(service, times(1)).getColdestPlaces();
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
        verify(service, times(0)).getColdestPlaces();
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindTopTenColdestPlacesAndThrowsDataIntegrityViolationException() throws Exception {
        //given
        //when
        when(service.getColdestPlaces())
                .thenThrow(new DataIntegrityViolationException("Find top ten coldest measurements by given date, throws DataIntegrityViolationException and return status 503"));
        mockMvc.perform(get(MAPPING + "/coldestTop"))
                .andExpect(status().is(503))
                .andReturn();
        //then
        verify(service, times(1)).getColdestPlaces();
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindAHottestPlaceByDate() throws Exception {
        //given
        SynopticMeasurement synopticMeasurement = mockSynopticRepository.synopticMeasurementsOrderColdest().get(0);
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
        SynopticMeasurement synopticMeasurement = mockSynopticRepository.synopticMeasurementsOrderHottest().get(0);
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