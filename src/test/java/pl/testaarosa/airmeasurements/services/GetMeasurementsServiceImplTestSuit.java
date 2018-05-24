package pl.testaarosa.airmeasurements.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.testaarosa.airmeasurements.domain.AirMeasurements;
import pl.testaarosa.airmeasurements.domain.MeasurementsAirLevel;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.repositories.*;
import pl.testaarosa.airmeasurements.repositories.AirMeasurementRepository;
import pl.testaarosa.airmeasurements.repositories.MeasuringStationRepository;
import pl.testaarosa.airmeasurements.repositories.SynopticMeasurementRepository;
import pl.testaarosa.airmeasurements.services.GetMeasurementsServiceImpl;

import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetMeasurementsServiceImplTestSuit {
    private final MockStationRepository mockStationRepository = new MockStationRepository();
    private final MockAirRepository mockAirRepository = new MockAirRepository();
    private final MockSynopticRepository mockSynopticRepository = new MockSynopticRepository();

    @InjectMocks
    private GetMeasurementsServiceImpl service;

    @Mock
    private MeasuringStationRepository stationRepository;

    @Mock
    private AirMeasurementRepository airRepository;

    @Mock
    private SynopticMeasurementRepository synopticRepository;

    @Test
    public void findAll() {
        when(stationRepository.findAll()).thenReturn(mockStationRepository.stations());
        List<MeasuringStation> resultList = service.findAll();
        assertEquals(mockStationRepository.stations(),resultList);
    }

    @Test
    public void getAllAirMeasurementesGivenDate() {
        when(airRepository.findAll()).thenReturn(mockAirRepository.airMeasurements1());
        assertEquals(mockAirRepository.airMeasurements1(),service.getAirMeasurements("2018-05-05"));
    }

    @Test
    public void getAllSynopticMeasurementesGivenDate() {
        when(synopticRepository.findAll()).thenReturn(mockSynopticRepository.synopticMeasurements2());
        assertEquals(mockSynopticRepository.synopticMeasurements2(),service.getSynopticMeasuremets("2018-05-11"));
    }

    @Test
    public void getHottestPlaceGivenDate() {
        when(synopticRepository.findAll()).thenReturn(mockSynopticRepository.synopticMeasurements2());
        assertEquals(mockSynopticRepository.synopticMeasurements2().get(0),service.getHottestPlaceGivenDate("2018-05-11"));
    }

    @Test
    public void getHottestPlaceGivenDate1() {
        when(synopticRepository.findAll()).thenReturn(mockSynopticRepository.synopticMeasurements2());
        assertThat(mockSynopticRepository.synopticMeasurements2(),hasItem(service.getHottestPlaceGivenDate("2018-05-11")));
    }

    @Test
    public void getColdestPlaceGivenDate() {
        when(synopticRepository.findAll()).thenReturn(mockSynopticRepository.synopticMeasurements1());
        assertEquals(mockSynopticRepository.synopticMeasurements1().get(0),service.getColdestPlaceGivenDate("2018-05-05"));
    }

    @Test
    public void getColdestPlaceGivenDate1() {
        when(synopticRepository.findAll()).thenReturn(mockSynopticRepository.synopticMeasurements1());
        assertThat(mockSynopticRepository.synopticMeasurements1(),hasItem(service.getColdestPlaceGivenDate("2018-05-05")));
    }

    @Test
    public void getMeasurementsByAirQuality() {
        List<AirMeasurements> measurementsList = mockAirRepository.airMeasurements1();
        when(airRepository.findAllByAirQuality(MeasurementsAirLevel.BAD)).thenReturn(measurementsList);
        assertEquals(measurementsList,service.getAirMeasurements(MeasurementsAirLevel.BAD));
    }

    @Test
    public void getcoldest10Places() {
        when(synopticRepository.findAll()).thenReturn(mockSynopticRepository.synopticMeasurements1());
        assertEquals(mockSynopticRepository.synopticMeasurements1(), service.getColdestPlaces());
    }

    @Test
    public void getHottest10Places(){
        when(synopticRepository.findAll()).thenReturn(mockSynopticRepository.synopticMeasurements2());
        assertEquals(mockSynopticRepository.synopticMeasurements2(), service.getHottestPlaces());
    }
}