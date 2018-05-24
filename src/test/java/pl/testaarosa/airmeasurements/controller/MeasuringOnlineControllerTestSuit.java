package pl.testaarosa.airmeasurements.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.testaarosa.airmeasurements.controller.MeasuringOnlineController;
import pl.testaarosa.airmeasurements.domain.MeasuringStationOnLine;
import pl.testaarosa.airmeasurements.repositories.Converter;
import pl.testaarosa.airmeasurements.repositories.MockOnlineRepository;
import pl.testaarosa.airmeasurements.services.MeasuringOnlineServices;

import java.util.List;

import static org.mockito.Mockito.when;

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
    public void getAllMeasuringStationsWithSynopticDataControllerTest() throws Exception {
        List<MeasuringStationOnLine> stationOnLineList = mockOnlineRepository.resultForOnlineController();
        when(measuringOnlineServices.getAllMeasuringStations()).thenReturn(stationOnLineList);
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING+"/stations/all"))
                .andExpect(MockMvcResultMatchers.content().json(converter.jsonInString(stationOnLineList)));
    }

    @Test
    public void getGivenCityMeasuringStationsWithSynopticDataControllerTest() throws Exception {
        List<MeasuringStationOnLine> stationOnLines = mockOnlineRepository.resultForOnlineController();
        when(measuringOnlineServices.getGivenCityMeasuringStationsWithSynopticData("wawa"))
                .thenReturn(stationOnLines);
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING+"/stations/select").param("city", "wawa"))
                .andExpect(MockMvcResultMatchers.content().json(converter.jsonInString(stationOnLines)));
    }

    @Test
    public void getHotestOnlineStationTest() throws Exception {
        MeasuringStationOnLine stationOnLine = mockOnlineRepository.resultForOnlineController().get(0);
        when(measuringOnlineServices.getHottestOnlineStation()).thenReturn(stationOnLine);
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING+"/stations/hottest"))
                .andExpect(MockMvcResultMatchers.content().json(converter.jsonInString(
                        stationOnLine)));
    }

    @Test
    public void getColdestOnlineStationTest() throws Exception {
        MeasuringStationOnLine stationOnLine = mockOnlineRepository.resultForOnlineController().get(0);
        when(measuringOnlineServices.getColdestOnlineStation()).
                thenReturn(stationOnLine);
        mockMvc.perform(MockMvcRequestBuilders.get(MAPPING+"/stations/coldest"))
                .andExpect(MockMvcResultMatchers.content().json(converter.jsonInString(
                        stationOnLine)));
    }

}