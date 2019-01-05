package pl.testaarosa.airmeasurements.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.testaarosa.airmeasurements.domain.dtoFe.OnlineMeasurementDto;
import pl.testaarosa.airmeasurements.repositories.MockOnlineRepository;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnlineServiceTestSuit {
    private final MockOnlineRepository mockOnlineRepository = new MockOnlineRepository();
    @InjectMocks
    private OnlineMeasurementServiceImpl service;

    @Mock
    private OnlineMeasurementProcessor msProcessor;

    @Before
    public void init() throws ExecutionException, InterruptedException {
        when(msProcessor.fillMeasuringStationListStructure())
                .thenReturn(mockOnlineRepository.measuringStationOnLineList());
    }

    @Test
    public void testGetAllMeasuringStations() {
        int result = service.getAllMeasuringStations().size();
        int expect = 4;
        assertEquals(expect, result);
        assertEquals(mockOnlineRepository.measuringStationOnLineList(),service.getAllMeasuringStations());
    }

    @Test
    public void getGivenCityMeasuringStationsWithSynopticData() {
        int result = service.getGivenCityMeasuringStationsWithSynopticData("Warsz").size();
        int expect = 2;
        assertEquals(expect, result);
    }

    @Test
    public void testgetHotestOnlineStation() throws ExecutionException, InterruptedException {
        OnlineMeasurementDto result = service.getHottestOnlineStation();
        OnlineMeasurementDto expect = mockOnlineRepository.measuringStationOnLineList().get(1);
        assertEquals(expect, result);
    }

    @Test
    public void testgetHotestOnlineStation1() throws ExecutionException, InterruptedException {
        OnlineMeasurementDto result = service.getHottestOnlineStation();
        OnlineMeasurementDto expect = mockOnlineRepository.measuringStationOnLineList().get(1);
        assertTrue(result.getStationName().contains(expect.getStationName()));
    }

    @Test
    public void testgetColdestOnlineStation() throws ExecutionException, InterruptedException {
        OnlineMeasurementDto result = service.getColdestOnlineStation();
        OnlineMeasurementDto expect = mockOnlineRepository.measuringStationOnLineList().get(0);
        assertEquals(expect, result);
    }
}
