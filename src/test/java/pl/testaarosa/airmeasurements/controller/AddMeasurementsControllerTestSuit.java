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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AddMeasurementsControllerTestSuit {

    private MockMvc mockMvc;
    private MockStationRepository mockStationRepository;
    private List<MeasuringStation> measuringStationList;
    private Converter converter;
    private final static String RESULT = "Test result";
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
    public void addMeasurements() throws Exception {
        MeasuringStation measuringStation = measuringStationList.get(0);
        Mockito.when(measurementsService.addOne(2)).thenReturn(measuringStation);
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING+"/station")
                .param("id",String.valueOf(2)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(converter.jsonInString(measuringStation)));
        verify(measurementsService, times(1)).addOne(2);
        verifyNoMoreInteractions(measurementsService);
    }

    @Test
    public void allMeasurements() throws Exception {
        Mockito.when(measurementsService.addMeasurementsAllStations()).thenReturn(measuringStationList);
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING+"/station/all"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(converter.jsonInString(measuringStationList)));
        verify(measurementsService, times(1)).addMeasurementsAllStations();
        verifyNoMoreInteractions(measurementsService);
    }


}