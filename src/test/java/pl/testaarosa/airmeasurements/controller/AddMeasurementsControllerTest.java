package pl.testaarosa.airmeasurements.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.testaarosa.airmeasurements.controller.AddMeasurementsController;
import pl.testaarosa.airmeasurements.services.AddMeasurementsService;

@RunWith(MockitoJUnitRunner.class)
public class AddMeasurementsControllerTest {

    private MockMvc mockMvc;
    private final static String RESULT = "Test result";
    private final static String MAPPING = "/add";
    @Mock
    private AddMeasurementsService measurementsService;

    @InjectMocks
    private AddMeasurementsController controller;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void addMeasurements() throws Exception {

        Mockito.when(measurementsService.addMeasurements(2)).thenReturn(RESULT);
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING+"/station")
                .param("id",String.valueOf(2)))
                .andExpect(MockMvcResultMatchers.content().string(RESULT));
    }

    @Test
    public void allMeasurements() throws Exception {

        Mockito.when(measurementsService.addMeasurementsAllStations()).thenReturn(RESULT);
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING+"/station/all"))
                .andExpect(MockMvcResultMatchers.content().string(RESULT));
    }


}