package pl.testaarosa.airmeasurements.controller;

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
    public void shouldAddOneMeasurementStatus200() throws Exception {
        MeasuringStation measuringStation = measuringStationList.get(0);
        Mockito.when(measurementsService.addOne(2)).thenReturn(measuringStation);
        mockMvc.perform(get(MAPPING+"/station")
                .param("id",String.valueOf(2)))
                .andExpect(status().is(201))
                .andExpect(MockMvcResultMatchers.content().json(converter.jsonInString(measuringStation)));
        verify(measurementsService, times(1)).addOne(2);
        verifyNoMoreInteractions(measurementsService);
    }

    @Test
    public void shouldAddOneMeasurementStatus404() throws Exception {
        MeasuringStation measuringStation = measuringStationList.get(1);
        mockMvc.perform(get(MAPPING+"/station/worngURL")
                .param("id",String.valueOf(2)))
                .andExpect(status().is(404));
        verifyNoMoreInteractions(measurementsService);
    }

    @Test
    public void shouldAddOneMeasurementStatus400() throws Exception {
        MeasuringStation measuringStation = measuringStationList.get(1);
        Mockito.when(measurementsService.addOne(2)).thenThrow(new NoSuchElementException("TestERROR NoSuchElement, wrong IP"));
        mockMvc.perform(get(MAPPING+"/station")
                .param("id",String.valueOf(2)))
                .andExpect(status().is(404));
        verify(measurementsService, times(1)).addOne(2);
        verifyNoMoreInteractions(measurementsService);
    }

    @Test
    public void shouldAddAllMeasurementsStatusIsOk() throws Exception {
        Mockito.when(measurementsService.addMeasurementsAllStations()).thenReturn(measuringStationList);
        mockMvc.perform(get(MAPPING+"/station/all"))
                .andExpect(status().is(201))
                .andExpect(MockMvcResultMatchers.content().json(converter.jsonInString(measuringStationList)));
        verify(measurementsService, times(1)).addMeasurementsAllStations();
        verifyNoMoreInteractions(measurementsService);
    }

    @Test
    public void shouldAddAllMeasurementsStatus404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING+"/station/all/status404"))
                .andExpect(status().is(404));
        verifyNoMoreInteractions(measurementsService);
    }

    @Test
    public void shouldAddAllMeasurementsStatus400() throws Exception {
        Mockito.when(measurementsService.addMeasurementsAllStations()).thenThrow(new InterruptedException());
        mockMvc.perform(get(MAPPING+"/station/all"))
                .andExpect(status().is(400));
        verify(measurementsService, times(1)).addMeasurementsAllStations();
        verifyNoMoreInteractions(measurementsService);
    }
}