package pl.testaarosa.airmeasurements.services;

import org.hibernate.HibernateException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.AirMeasurementLevel;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.repositories.*;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StoredMeasurementsServiceImplTestSuit {
    private final MockStationRepository mockStationRepository = new MockStationRepository();
    private final MockAirRepository mockAirRepository = new MockAirRepository();
    private final MockSynopticRepository mockSynopticRepository = new MockSynopticRepository();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private StoredMeasurementsServiceImpl service;

    @Mock
    private MeasuringStationRepository stationRepository;

    @Mock
    private AirMeasurementRepository airRepository;

    @Mock
    private SynopticMeasurementRepository synopticRepository;

    @Test
    public void shouldFindAllStoredMeasurements() {
        //given
        List<MeasuringStation> expect = mockStationRepository.stations();
        //when
        when(stationRepository.findAll()).thenReturn(expect);
        //then
        List<MeasuringStation> resultList = service.findAll();
        assertEquals(expect,resultList);
    }

    @Test
    public void shouldFindAllStoredMeasurementsAndThrowsNoSuchElementException() {
        //given
        //when
        exception.expect(NoSuchElementException.class);
        exception.expectMessage("Can't find any measurements in data base");
        //then
        service.findAll();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldFindAllStoredMeasurementsAndThrowsMockNoSuchElementException() {
        //given
        //when
        when(service.findAll()).thenThrow(NoSuchElementException.class);
        //then
        service.findAll();
    }

    @Test
    public void shouldFindMeasurementsByAirQuality() {
        //given
        List<AirMeasurement> measurementsList = mockAirRepository.airMeasurements1();
        //when
        when(airRepository.findAllByAirQuality(AirMeasurementLevel.BAD)).thenReturn(measurementsList);
        //then
        assertEquals(measurementsList,service.getAirMeasurementsByLevel(AirMeasurementLevel.BAD));
    }

    @Test
    public void shouldFindMeasurementsByAirQualityAndThrowsNoSuchElementException() {
        //given
        //when
        exception.expect(NoSuchElementException.class);
        exception.expectMessage("There are no measurements for given air level: ");
        //then
        service.getAirMeasurementsByLevel(AirMeasurementLevel.BAD);
    }

    @Test
    public void shouldFindMeasurementsByAirQualityAndThrowsIllegalArgumentException() {
        //given
        //when
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("No enum constant null");
        //then
        service.getAirMeasurementsByLevel(null);
    }

    @Test
    public void shouldFindMeasurementsByAirQualityAndThrowsIllegalArgumentExceptionAssertJ() {
        //given
        //when
        //then
        assertThatThrownBy(()-> service.getAirMeasurementsByLevel(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageEndingWith("No enum constant null");

    }

    @Test
    public void shouldFindMeasurementsByAirQualityAndThrowsDataIntegrityViolationExceptionVintageJU() {
        //given
        BDDMockito.given(airRepository.findAllByAirQuality(AirMeasurementLevel.BAD)).willThrow(DataIntegrityViolationException.class);
        try {
            service.getAirMeasurementsByLevel(AirMeasurementLevel.BAD);
        } catch (RuntimeException e) {
            assertEquals("There is some db problem: null", e.getMessage());
        }
    }

    @Test(expected = RuntimeException.class)
    public void shouldFindMeasurementsByAirQualityAndTDataIntegrityViolationExceptionJU4() {
        service.getAirMeasurementsByLevel(AirMeasurementLevel.BAD);
    }

    @Test
    public void shouldFindMeasurementsByAirQualityAndTDataIntegrityViolationExceptionRULEJunit() {
        //given
        BDDMockito.given(airRepository.findAllByAirQuality(AirMeasurementLevel.BAD)).willThrow(DataIntegrityViolationException.class);
        //when
        exception.expect(RuntimeException.class);
        exception.expectMessage("There is some db problem: null");
        //then
        service.getAirMeasurementsByLevel(AirMeasurementLevel.BAD);
    }

    @Test
    public void shouldFindMeasurementsByAirQualityAndTDataIntegrityViolationExceptionJU5() {
        //given
        BDDMockito.given(airRepository.findAllByAirQuality(AirMeasurementLevel.BAD)).willThrow(DataIntegrityViolationException.class);
        //when
       Exception exception = assertThrows(RuntimeException.class,()-> service.getAirMeasurementsByLevel(AirMeasurementLevel.BAD));
       //then
       assertEquals("There is some db problem: null", exception.getMessage());
    }

    @Test
    public void shouldFindAllAirMeasurementsGivenDate() {
        //given
        List<AirMeasurement> airMeasurements = mockAirRepository.airMeasurements1();
        //when
        when(airRepository.findAll()).thenReturn(airMeasurements);
        //then
        List<AirMeasurement> result = service.getAirMeasurementsByDate("2018-05-05");
        assertEquals(mockAirRepository.airMeasurements1(), result);
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