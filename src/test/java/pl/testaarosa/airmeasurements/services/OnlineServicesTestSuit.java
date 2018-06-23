package pl.testaarosa.airmeasurements.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.testaarosa.airmeasurements.domain.MeasuringStationOnLine;
import pl.testaarosa.airmeasurements.repositories.MockOnlineRepository;
import pl.testaarosa.airmeasurements.services.MeasurementStationProcessor;
import pl.testaarosa.airmeasurements.services.MeasuringOnlineServicesImpl;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnlineServicesTestSuit {
    private final MockOnlineRepository mockOnlineRepository = new MockOnlineRepository();
    @InjectMocks
    private MeasuringOnlineServicesImpl service;

    @Mock
    private MeasurementStationProcessor msProcessor;

    @Before
    public void init() throws ExecutionException, InterruptedException {
        when(msProcessor.fillMeasuringStationListStructure()).thenReturn(mockOnlineRepository.measuringStationOnLineList());
    }

    @Test
    public void testGetAllMeasuringStations() throws ExecutionException, InterruptedException {
        int result = service.getAllMeasuringStations().size();
        int expect = 4;
        assertEquals(expect, result);
        assertEquals(mockOnlineRepository.measuringStationOnLineList(),service.getAllMeasuringStations());
    }

    @Test
    public void getGivenCityMeasuringStationsWithSynopticData() throws ExecutionException, InterruptedException {
        int result = service.getGivenCityMeasuringStationsWithSynopticData("Warsz").size();
        int expect = 2;
        assertEquals(expect, result);
    }

    @Test
    public void testgetHotestOnlineStation() throws ExecutionException, InterruptedException {
        MeasuringStationOnLine result = service.getHottestOnlineStation();
        MeasuringStationOnLine expect = mockOnlineRepository.measuringStationOnLineList().get(1);
        assertEquals(expect, result);
    }

    @Test
    public void testgetHotestOnlineStation1() throws ExecutionException, InterruptedException {
        MeasuringStationOnLine result = service.getHottestOnlineStation();
        MeasuringStationOnLine expect = mockOnlineRepository.measuringStationOnLineList().get(1);
        assertTrue(result.getStationName().contains(expect.getStationName()));
    }

    @Test
    public void testgetColdestOnlineStation() throws ExecutionException, InterruptedException {
        MeasuringStationOnLine result = service.getColdestOnlineStation();
        MeasuringStationOnLine expect = mockOnlineRepository.measuringStationOnLineList().get(0);
        assertEquals(expect, result);
    }
}
