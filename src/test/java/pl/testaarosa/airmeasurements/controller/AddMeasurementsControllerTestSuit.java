package pl.testaarosa.airmeasurements.controller;

import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestClientException;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.repositories.Converter;
import pl.testaarosa.airmeasurements.repositories.MockStationRepository;
import pl.testaarosa.airmeasurements.services.AddMeasurementsService;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AddMeasurementsControllerTestSuit {

    private MockMvc mockMvc;
    private MockStationRepository mockStationRepository;
    private List<MeasuringStation> measuringStationList;
    private Converter converter;
    private final static String MAPPING = "/add";
    @Mock
    private AddMeasurementsService measurementsService;

    @InjectMocks
    private AddMeasurementsController controller;

    @Before
    public void init() {
        converter = new Converter();
        mockStationRepository = new MockStationRepository();
        measuringStationList = mockStationRepository.stations();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void shouldAddOneMeasurement() throws Exception {
        //given
        MeasuringStation measuringStation = measuringStationList.get(0);
        //when
        Mockito.when(measurementsService.addOne(2)).thenReturn(measuringStation);
        mockMvc.perform(get(MAPPING+"/station")
                .param("stationId",String.valueOf(2)))
                .andExpect(status().is(201))
                .andExpect(MockMvcResultMatchers.content().json(converter.jsonInString(measuringStation)));
        //then
        verify(measurementsService, times(1)).addOne(2);
        verifyNoMoreInteractions(measurementsService);
    }

    @Test
    public void shouldAddOneMeasurementAndThrowsNoSuchElementException() throws Exception {
        //given
        //when
        Mockito.when(measurementsService.addOne(2))
                .thenThrow(new NoSuchElementException("Add measurements to given station, throws NoSuchElementException, return status 400"));
        mockMvc.perform(get(MAPPING+"/station")
                .param("stationId",String.valueOf(2)))
                .andExpect(status().is(400))
                .andReturn();
        //then
        verify(measurementsService, times(1)).addOne(2);
        verifyNoMoreInteractions(measurementsService);
    }

    @Test
    public void shouldAddOneMeasurementWrongURL() throws Exception {
        //given
        //when
        mockMvc.perform(get(MAPPING+"/station/worngURL")
                .param("id",String.valueOf(2)))
                .andExpect(status().is(404))
                .andReturn();
        //then
        verify(measurementsService,times(0)).addOne(2);
        verifyNoMoreInteractions(measurementsService);
    }

    @Test
    public void shouldAddOneMeasurementAndThrowsRestClientException() throws Exception {
        //given
        //when
        Mockito.when(measurementsService.addOne(2))
                .thenThrow(new RestClientException("Add measurements to given station, throws RestClientException, return status 500"));
        mockMvc.perform(get(MAPPING+"/station")
                .param("stationId",String.valueOf(2)))
                .andExpect(status().is(500))
                .andReturn();
        //then
        verify(measurementsService, times(1)).addOne(2);
        verifyNoMoreInteractions(measurementsService);
    }

    @Test
    public void shouldAddOneMeasurementAndThrowsHibernateException() throws Exception {
        //given
        //when
        Mockito.when(measurementsService.addOne(2))
                .thenThrow(new HibernateException("Add measurements to given station, throws HibernateException, return status 500"));
        mockMvc.perform(get(MAPPING+"/station")
                .param("stationId",String.valueOf(2)))
                .andExpect(status().is(503))
                .andReturn();
        //then
        verify(measurementsService, times(1)).addOne(2);
        verifyNoMoreInteractions(measurementsService);
    }

    @Test
    public void shouldAddAllMeasurementsToDb() throws Exception {
        //given
        //when
        Mockito.when(measurementsService.addMeasurementsAllStations()).thenReturn(measuringStationList);
        mockMvc.perform(get(MAPPING+"/station/all"))
                .andExpect(status().is(201))
                .andExpect(MockMvcResultMatchers.content().json(converter.jsonInString(measuringStationList)));
        //then
        verify(measurementsService, times(1)).addMeasurementsAllStations();
        verifyNoMoreInteractions(measurementsService);
    }

    @Test
    public void shouldAddAllMeasurementsAndWrongUrl() throws Exception {
        //given
        //when
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING+"/station/all/status404"))
                .andExpect(status().is(404))
                .andReturn();
        //then
        verify(measurementsService,times(0)).addMeasurementsAllStations();
        verifyNoMoreInteractions(measurementsService);
    }

    @Test
    public void shouldAddAllMeasurementsAndThrowsRestClientException() throws Exception {
        //given
        //when
        Mockito.when(measurementsService.addMeasurementsAllStations())
                .thenThrow(new RestClientException("Add measurements for all stations, throws RestClientException, return status 500"));
        mockMvc.perform(get(MAPPING+"/station/all"))
                .andExpect(status().is(500));
        //then
        verify(measurementsService, times(1)).addMeasurementsAllStations();
        verifyNoMoreInteractions(measurementsService);
    }

    @Test
    public void shouldAddAllMeasurementsAndThrowsHibernateException() throws Exception {
        //given
        //when
        Mockito.when(measurementsService.addMeasurementsAllStations())
                .thenThrow(new HibernateException("Add measurements for all stations, throws HibernateException, return status 503"));
        mockMvc.perform(get(MAPPING+"/station/all"))
                .andExpect(status().is(503))
                .andReturn();
        //then
        verify(measurementsService, times(1)).addMeasurementsAllStations();
        verifyNoMoreInteractions(measurementsService);
    }
}