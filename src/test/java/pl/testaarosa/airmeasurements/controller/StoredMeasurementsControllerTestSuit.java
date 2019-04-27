package pl.testaarosa.airmeasurements.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.testaarosa.airmeasurements.controller.exceptionHandler.MeteoResponseEntityHandler;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.AirMeasurementLevel;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.repositories.Converter;
import pl.testaarosa.airmeasurements.repositories.MockAirMeasurementRepository;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationRepository;
import pl.testaarosa.airmeasurements.repositories.MockSynopticMeasurementRepository;
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
    private final MockMeasuringStationRepository mockMeasuringStationRepository = new MockMeasuringStationRepository();
    private final MockAirMeasurementRepository mockAirMeasurementRepository = new MockAirMeasurementRepository();
    private final Converter converter = new Converter();
    private final static String MAPPING = "/stored/station";

    @InjectMocks
    private StoredMeasurementsController controller;

    @Mock
    private StoredMeasurementsService service;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new MeteoResponseEntityHandler())
                .build();
    }

    @Test
    public void shouldFindAllStationsWithAllMeasurements() throws Exception {
        //given
        List<MeasuringStation> stations = mockMeasuringStationRepository.stations();
        //when
        Mockito.when(service.findAll()).thenReturn(stations);
        String jsonContent = converter.jsonInString(stations);
        mockMvc.perform(get(MAPPING + "/allMeasurements").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().json(jsonContent));
        //then
        verify(service, times(1)).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindAllStationsWithAllMeasurementsAndThrowsNoSuchElementException() throws Exception {
        //given
        List<MeasuringStation> stations = mockMeasuringStationRepository.stations();
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

}