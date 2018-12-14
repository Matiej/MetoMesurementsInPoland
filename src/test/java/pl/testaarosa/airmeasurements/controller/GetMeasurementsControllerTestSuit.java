package pl.testaarosa.airmeasurements.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.testaarosa.airmeasurements.domain.AirMeasurements;
import pl.testaarosa.airmeasurements.domain.MeasurementsAirLevel;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurements;
import pl.testaarosa.airmeasurements.repositories.Converter;
import pl.testaarosa.airmeasurements.repositories.MockAirRepository;
import pl.testaarosa.airmeasurements.repositories.MockStationRepository;
import pl.testaarosa.airmeasurements.repositories.MockSynopticRepository;
import pl.testaarosa.airmeasurements.services.GetMeasurementsService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class GetMeasurementsControllerTestSuit {
    private final MockStationRepository mockStationRepository = new MockStationRepository();
    private final MockAirRepository mockAirRepository = new MockAirRepository();
    private final MockSynopticRepository mockSynopticRepository = new MockSynopticRepository();
    private final Converter converter = new Converter();
    private final static String MAPPING = "/get";

    @InjectMocks
    private GetMeasurementsController controller;

    @Mock
    private GetMeasurementsService service;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testFindAll() throws Exception {
        List<MeasuringStation> stations = mockStationRepository.stations();
        Mockito.when(service.findAll()).thenReturn(stations);
        String jsonContent = converter.jsonInString(stations);
        mockMvc.perform(get(MAPPING + "/stations/all"))
                .andExpect(status().is(200))
                .andExpect(content().json(jsonContent));
        verify(service, times(1)).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testfindAllAirMeasurementsByDate() throws Exception {
        List<AirMeasurements> airMeasurementsList = mockAirRepository.airMeasurements1();
        Mockito.when((service.getAirMeasurements("2018-05-05"))).thenReturn(airMeasurementsList);
        String jsonContent = converter.jsonInString(airMeasurementsList);
        mockMvc.perform(get(MAPPING + "/measurements/date")
                .param("date", "2018-05-05"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(jsonContent));
        verify(service,times(1)).getAirMeasurements("2018-05-05");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testFindAHottestPlaceByDate() throws Exception {
        SynopticMeasurements synopticMeasurement = mockSynopticRepository.synopticMeasurements1().get(0);
        Mockito.when(service.getHottestPlaceGivenDate("2018-05-05")).thenReturn(synopticMeasurement);
        mockMvc.perform(get(MAPPING + "/measurements/hottest")
                .param("date", "2018-05-05"))
                .andExpect(status().is(200))
                .andExpect(content().json(converter.jsonInString(synopticMeasurement)));
        verify(service,times(1)).getHottestPlaceGivenDate("2018-05-05");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testFindColdestPlaceByDate() throws Exception {
        SynopticMeasurements synopticMeasurements = mockSynopticRepository.synopticMeasurements2().get(0);
        Mockito.when(service.getColdestPlaceGivenDate("2018-05-05")).thenReturn(synopticMeasurements);
        mockMvc.perform(get(MAPPING + "/measurements/coldest")
                .param("date", "2018-05-05"))
                .andExpect(status().isOk())
                .andExpect(content().json(converter.jsonInString(synopticMeasurements)));
        verify(service,times(1)).getColdestPlaceGivenDate("2018-05-05");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testFindPlaceByAirQuality() throws Exception {
        List<AirMeasurements> airMeasurementsList = mockAirRepository.airMeasurements1();
        Mockito.when(service.getAirMeasurements(MeasurementsAirLevel.BAD)).thenReturn(airMeasurementsList);
        mockMvc.perform(get(MAPPING + "/measurements/air")
                .param("airLevel", "BAD"))
                .andExpect(status().isOk())
                .andExpect(content().json(converter.jsonInString(airMeasurementsList)));
        verify(service, times(1)).getAirMeasurements(MeasurementsAirLevel.BAD);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testfindAllSynopticMeasurementsByDate() throws Exception {
        List<SynopticMeasurements> synopticMeasurements = mockSynopticRepository.synopticMeasurements2();
        Mockito.when((service.getSynopticMeasuremets("2018-05-11"))).thenReturn(synopticMeasurements);
        mockMvc.perform(get(MAPPING + "/measurements/synoptic")
                .param("date", "2018-05-11"))
                .andExpect(status().is(200))
                .andExpect(content().json(converter.jsonInString(synopticMeasurements)));
        verify(service,times(1)).getSynopticMeasuremets("2018-05-11");
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testFindColdest10Places() throws Exception {
        List<SynopticMeasurements> measurementsList = mockSynopticRepository.synopticMeasurements2();
        when(service.getColdestPlaces()).thenReturn(measurementsList);
        mockMvc.perform(get(MAPPING + "/measurements/coldestTop"))
                .andExpect(status().isOk())
                .andExpect(content().json(converter.jsonInString(measurementsList)));
        verify(service,times(1)).getColdestPlaces();
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testFindHottest10Places() throws Exception {
        List<SynopticMeasurements> measurementsList = mockSynopticRepository.synopticMeasurements2();
        Mockito.when(service.getHottestPlaces()).thenReturn(measurementsList);
        mockMvc.perform(get(MAPPING + "/measurements/hottestTop"))
                .andExpect(status().is(200))
                .andExpect(content().json(converter.jsonInString(measurementsList)));
        verify(service,times(1)).getHottestPlaces();
        verifyNoMoreInteractions(service);
    }
}