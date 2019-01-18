package pl.testaarosa.airmeasurements.services;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.AirMeasurementLevel;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.repositories.*;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StoredMeasurementsServiceImplTestSuit {
    private MockStationRepository mockStationRepository;
    private MockAirRepository mockAirRepository;
    private MockSynopticRepository mockSynopticRepository;

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

    @Before
    public void init() {
        mockStationRepository = new MockStationRepository();
        mockAirRepository = new MockAirRepository();
        mockSynopticRepository = new MockSynopticRepository();
    }

    @Test
    public void shouldFindAllStoredMeasurements() {
        //given
        List<MeasuringStation> expect = mockStationRepository.stations();
        //when
        when(stationRepository.findAll()).thenReturn(expect);
        //then
        List<MeasuringStation> resultList = service.findAll();
        assertEquals(expect, resultList);
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
    public void shouldFindAllStoredMeasurementsAndThrowskNoSuchElementException() {
        //given
        //when
        when(service.findAll()).thenThrow(NoSuchElementException.class);
        //then
        service.findAll();
    }

    @Test
    public void shouldFinaAllStoredMeasurementsAndThrowsDataIntegrityViolationException() {
        //given
        given(stationRepository.findAll()).willThrow(DataIntegrityViolationException.class);
        //when
        exception.expect(RuntimeException.class);
        exception.expectMessage("There is some db problem: null");
        //then
        service.findAll();
    }

    @Test
    public void shouldFindAllSynopticMeasurementsGivenDate() {
        //given
        List<SynopticMeasurement> expect = mockSynopticRepository.synopticMeasurementsOrderHottest();
        //when
        when(synopticRepository.findAll()).thenReturn(expect);
        //then
        List<SynopticMeasurement> result = new ArrayList<>();
        try {
            result = service.getSynopticMeasuremets("2018-05-11");
        } catch (NoSuchElementException | DateTimeException | DataIntegrityViolationException e) {
            assertEquals("Not importat message", e.getMessage());
        }
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(expect, result);
    }

    @Test
    public void shouldFindAllSynopticMeasurementsGivenDateAndThrowsDataIntegrityViolationException() {
        //given
        given(synopticRepository.findAll()).willThrow(DataIntegrityViolationException.class);
        String date = "2019-01-01";
        String expectMessage = "There is some db connection problem: null";
        //when
        exception.expect(RuntimeException.class);
        exception.expectMessage(expectMessage);
        //then
        service.getSynopticMeasuremets(date);
    }

    @Test
    public void shouldFindAllSynopticMeasurementsGivenWrongDateAndThrowsDateTimeException() {
        //given
        String wrongDate = "2018-13-01";
        //when
        exception.expect(DateTimeException.class);
        exception.expectMessage("Wrong date format!");
        //then
        service.getSynopticMeasuremets(wrongDate);
    }

    @Test
    public void shouldFindAllSynopticMeasurementsGivenWrongDateFormatAndThrowsDateTimeException() {
        //given
        String wrongDateFormat = "2018/01/01";
        //when
        exception.expect(DateTimeException.class);
        exception.expectMessage("Wrong date format!");
        //then
        service.getSynopticMeasuremets(wrongDateFormat);
    }

    @Test
    public void shouldFindAllSynopticMeasurementsGivenDateAndThrowsNoSuchElementException() {
        //given
        String date = "2018-11-01";
        //when
        exception.expect(NoSuchElementException.class);
        exception.expectMessage("Cant't find any synoptic measurements for date: " + date);
        //then
        service.getSynopticMeasuremets(date);
    }

    @Test
    public void shouldFindMeasurementsByAirQuality() {
        //given
        List<AirMeasurement> measurementsList = mockAirRepository.airMeasurements1();
        //when
        when(airRepository.findAllByAirQuality(AirMeasurementLevel.BAD)).thenReturn(measurementsList);
        //then
        assertEquals(measurementsList, service.getAirMeasurementsByLevel(AirMeasurementLevel.BAD));
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
        assertThatThrownBy(() -> service.getAirMeasurementsByLevel(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageEndingWith("No enum constant null");
    }

    @Test
    public void shouldFindMeasurementsByAirQualityAndThrowsDataIntegrityViolationExceptionVintageJU() {
        //given
        given(airRepository.findAllByAirQuality(AirMeasurementLevel.BAD)).willThrow(DataIntegrityViolationException.class);
        //when then
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
        given(airRepository.findAllByAirQuality(AirMeasurementLevel.BAD)).willThrow(DataIntegrityViolationException.class);
        //when
        exception.expect(RuntimeException.class);
        exception.expectMessage("There is some db problem: null");
        //then
        service.getAirMeasurementsByLevel(AirMeasurementLevel.BAD);
    }

    @Test
    public void shouldFindMeasurementsByAirQualityAndTDataIntegrityViolationExceptionJU5jupiter() {
        //given
        given(airRepository.findAllByAirQuality(AirMeasurementLevel.BAD)).willThrow(DataIntegrityViolationException.class);
        //when
        Exception exception = assertThrows(RuntimeException.class, () -> service.getAirMeasurementsByLevel(AirMeasurementLevel.BAD));
        //then
        assertEquals("There is some db problem: null", exception.getMessage());
    }

    @Test
    public void shouldFindAllAirMeasurementsGivenDate() {
        //given
        List<AirMeasurement> expected = mockAirRepository.airMeasurements1();
        //when
        when(airRepository.findAll()).thenReturn(expected);
        //then
        List<AirMeasurement> result = new ArrayList<>();
        try {
            result = service.getAirMeasurementsByDate("2018-05-05");
        } catch (NoSuchElementException | DateTimeException | DataIntegrityViolationException e) {
            assertEquals("Ignored exceptoion message", e.getMessage());
        }
        assertEquals(expected, result);
    }

    @Test
    public void shouldFindAllAirMeasurementsGivenDateAndThrowsDataIntegrityViolationException() {
        //given
        given(airRepository.findAll()).willThrow(DataIntegrityViolationException.class);
        //when
        exception.expect(RuntimeException.class);
        exception.expectMessage("There is some db connection problem: null");
        //then
        service.getAirMeasurementsByDate("2018-11-01");
    }

    @Test
    public void shouldFindAllAirMeasurementsGivenWrongDateAndThrowsDateTimeException() {
        //given
        String wrongDate = "2018-13-01";
        //when
        exception.expect(DateTimeException.class);
        exception.expectMessage("Wrong date format!");
        //then
        service.getAirMeasurementsByDate(wrongDate);
    }

    @Test
    public void shouldFindAllAirMeasurementsGivenWrongFormatDateAndThrowsDateTimeException() {
        //given
        String wrongDateFormat = "2018-01-01a";
        //when
        exception.expect(DateTimeException.class);
        exception.expectMessage("Wrong date format!");
        //then
        service.getAirMeasurementsByDate(wrongDateFormat);
    }

    @Test
    public void shouldFindAllAirMeasurementsGivenWrongFormatDateAndThrowsDateTimeExceptionCatch() {
        //given
        String wrongDate = "we2e2";
        String except = "Wrong date format!";
        //then
        try {
            service.getAirMeasurementsByDate(wrongDate);
        } catch (DateTimeException e) {
            assertEquals(except, e.getMessage());
        }
    }

    @Test
    public void shouldFindAllAirMeasurementsGivenDateAndThrowsNoSuchElementException() {
        //given
        String date = "2018-01-01";
        //when
        exception.expect(NoSuchElementException.class);
        exception.expectMessage("Cant't find any air measurements for date: " + date);
        //then
        service.getAirMeasurementsByDate(date);
    }

    @Test
    public void shouldFindHottestTop10Places() {
        //given jUnit vintage ver
        List<SynopticMeasurement> expected = mockSynopticRepository.synopticMeasurementsOrderHottest();
        //when
        when(synopticRepository.findAll()).thenReturn(expected);
        //then
        List<SynopticMeasurement> hottestPlaces = new ArrayList<>();
        try {
            hottestPlaces = service.getHottestPlaces();
        } catch (DataIntegrityViolationException | NoSuchElementException e) {
            assertEquals("Some ignored message", e.getMessage());
        } finally {
            assertNotNull(hottestPlaces);
            assertEquals(4, hottestPlaces.size());
            assertEquals(expected, hottestPlaces);
        }
    }

    @Test
    public void shouldFindHottestTop10PlacesAndThrowsDataIntegrityViolationException() {
        //given
        given(synopticRepository.findAll()).willThrow(DataIntegrityViolationException.class);
        //when
        exception.expect(RuntimeException.class);
        exception.expectMessage("There is some db problem: null");
        //then
        service.getHottestPlaces();
        service.getHottestPlaces();
    }

    @Test
    public void shouldFindHottestTop10PlacesAndThrowsNoSuchElementException() {
        //given
        //when
        exception.expect(NoSuchElementException.class);
        exception.expectMessage("Can't find hottest 10 measurements");
        //then
        service.getHottestPlaces();
    }

    @Test
    public void shouldFindHottestTop10PlacesAndThrowsNoSuchElementExceptionJU5() {
        //given jUnit 5 jupiter version
        String expectedMessage = "Can't find hottest 10 measurements";
        //when
        Exception exception = assertThrows(NoSuchElementException.class, () -> service.getHottestPlaces());
        //then
        assertEquals(expectedMessage, exception.getMessage());

    }

    @Test
    public void shouldFindColdestTop10Places() {
        //given
        List<SynopticMeasurement> expect = mockSynopticRepository.synopticMeasurementsOrderColdest();
        //when
        when(synopticRepository.findAll()).thenReturn(expect);
        //then
        List<SynopticMeasurement> result = new ArrayList<>();
        try {
            result = service.getColdestPlaces();
        } catch (DataIntegrityViolationException | NoSuchElementException e) {
            assertEquals("Some ignored message", e.getMessage());
        }
        //then
        assertNotNull(result);
        assertEquals(expect, result);
        assertEquals(4, result.size());
    }

    @Test
    public void shouldFindColdestTop10PlacesAndThrowsDataIntegrityViolationException() {
        //given
        given(synopticRepository.findAll()).willThrow(DataIntegrityViolationException.class);
        //when
        exception.expect(RuntimeException.class);
        exception.expectMessage("There is some db problem: null");
        //then
        service.getHottestPlaces();
        List<SynopticMeasurement> result = service.getHottestPlaces();
        Assertions.assertNull(result);
    }

    @Test
    public void shouldFindColdestTop10PlacesAndThrowsDataIntegrityViolationExceptionJU5() {
        //given
        String expectMessage = "There is some db problem: null";
        given(synopticRepository.findAll()).willThrow(DataIntegrityViolationException.class);
        //when
        RuntimeException resultMessage = assertThrows(RuntimeException.class, () -> service.getColdestPlaces());
        //then
        assertEquals(expectMessage, resultMessage.getMessage());
    }

    @Test
    public void shouldFindColdestTop10PlacesAndThrowsNoSuchElementException() {
        //given
        String expectedMessage = "Can't find coldest 10 measurements";
        //when
        exception.expect(NoSuchElementException.class);
        exception.expectMessage(expectedMessage);
        //then
        List<SynopticMeasurement> result = service.getColdestPlaces();
        assertNull(result);
    }

    @Test
    public void shouldFindColdestTop10PlacesAndThrowsNoSuchElementExceptionVintageJU() {
        //given
        String expectedMessage = "Can't find coldest 10 measurements";
        //when
        //then
        try {
            service.getColdestPlaces();
        } catch (NoSuchElementException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void shouldFindHottestPlaceGivenDate() {
        //given
        List<SynopticMeasurement> givenRepo = mockSynopticRepository.synopticMeasurementsOrderHottest();
        SynopticMeasurement expected = givenRepo.get(0);
        //when
        when(synopticRepository.findAll()).thenReturn(givenRepo);
        //then
        SynopticMeasurement result = new SynopticMeasurement();
        try {
            result = service.getHottestPlaceGivenDate("2018-05-11");
        } catch (DateTimeException | NoSuchElementException | DataIntegrityViolationException e) {
            assertEquals("some wrong message", e.getMessage());
        }
        assertNotNull(result);
        assertEquals(expected, result);
        Assertions.assertNotSame(givenRepo.get(2), result);
        assertNotEquals(givenRepo.get(1), result);
        assertThat(givenRepo, hasItems(result));
    }

    @Test
    public void shouldFindHottestPlaceGivenDateAndThrowsDataIntegrityViolationException() {
        //given
        given(synopticRepository.findAll()).willThrow(DataIntegrityViolationException.class);
        //when
        exception.expect(RuntimeException.class);
        exception.expectMessage("There is some db problem: null");
        //then
        SynopticMeasurement result = service.getHottestPlaceGivenDate("2018-05-11");
        assertNull(result);
    }

    @Test
    public void shouldFindHottestPlaceGivenDateAndThrowsDataIntegrityViolationExceptionJU5() {
        //given
        String expectMessage = "There is some db problem: null";
        given(synopticRepository.findAll()).willThrow(DataIntegrityViolationException.class);
        //when
        RuntimeException resultMessage = assertThrows(RuntimeException.class, () -> service.getHottestPlaceGivenDate("2019-01-01"));
        //then
        assertEquals(expectMessage, resultMessage.getMessage());
    }

    @Test
    public void shouldFindHottestPlaceGivenWrongDateAndThrowsDateTimeException() {
        //given
        String wrongDate = "2019-01-44";
        //when
        exception.expect(DateTimeException.class);
        exception.expectMessage("Wrong date format!");
        //then
        service.getHottestPlaceGivenDate(wrongDate);
    }

    @Test
    public void shouldFindHottestPlaceGivenWrongFormatDateAndThrowsDateTimeException() {
        //given
        String wrongDate = "2019-0144";
        //when
        exception.expect(DateTimeException.class);
        exception.expectMessage("Wrong date format!");
        //then
        service.getHottestPlaceGivenDate(wrongDate);
    }

    @Test
    public void shouldFindHottestPlaceGivenWrongFormatDateAndThrowsNoSuchElementException() {
        //given
        String date = "2018-11-01";
        //when
        exception.expect(NoSuchElementException.class);
        exception.expectMessage("Cant't find any synoptic measurements for date: " + date);
        //then
        service.getHottestPlaceGivenDate(date);
    }

    @Test
    public void shouldFindHottestPlaceGivenWrongFormatDateAndThrowsNoSuchElementExceptionJUVintage() {
        //given
        String date = "2018-11-01";
        String expectMessage = "Cant't find any synoptic measurements for date: " + date;
        //when
        //then
        try {
            service.getHottestPlaceGivenDate(date);
        } catch (NoSuchElementException e) {
            assertEquals(expectMessage, e.getMessage());
        }
    }

    @Test
    public void shouldFindColdestPlaceGivenDate() {
        //given
        List<SynopticMeasurement> givenRepo = mockSynopticRepository.synopticMeasurementsOrderColdest();
        SynopticMeasurement expected = givenRepo.get(0);
        String date = "2018-05-05";
        //when
        when(synopticRepository.findAll()).thenReturn(givenRepo);
        //then
        SynopticMeasurement result = new SynopticMeasurement();
        try {
            result = service.getColdestPlaceGivenDate(date);
        } catch (DateTimeException | NoSuchElementException | DataIntegrityViolationException e) {
            assertEquals("some wrong message", e.getMessage());
        }
        assertNotNull(result);
        assertEquals(expected, result);
        Assertions.assertNotSame(givenRepo.get(2), result);
        assertNotEquals(givenRepo.get(2), result);
        assertThat(givenRepo, hasItems(result));
    }

    @Test
    public void shouldFindColdestPlaceGivenDateAndThrowsDataIntegrityViolationException() {
        //given
        given(synopticRepository.findAll()).willThrow(DataIntegrityViolationException.class);
        String date = "2018-05-05";
        //when
        exception.expect(RuntimeException.class);
        exception.expectMessage("There is some db problem: null");
        //then
        SynopticMeasurement result = service.getColdestPlaceGivenDate(date);
        assertNull(result);
    }

    @Test
    public void shouldFindColdestPlaceGivenDateAndThrowsDataIntegrityViolationExceptionJU5() {
        //give
        String expectMessage = "There is some db problem: null";
        given(synopticRepository.findAll()).willThrow(DataIntegrityViolationException.class);
        String date = "2018-05-05";
        //when
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.getColdestPlaceGivenDate(date));
        //then
        assertEquals(expectMessage, exception.getMessage());
    }

    @Test
    public void shouldFindColdestPlaceGivenWrongDateAndThrowsDateTimeException() {
        //given
        String expectMessage = "Wrong date format!";
        String wrongDate = "2018-44-05";
        //when
        exception.expect(DateTimeException.class);
        exception.expectMessage(expectMessage);
        //then
        service.getColdestPlaceGivenDate(wrongDate);
    }

    @Test
    public void shouldFindColdestPlaceGivenWrongDateFormatAndThrowsDateTimeException() {
        //given
        String expectMessage = "Wrong date format!";
        String wrongDate = "2018/04/05";
        //when
        exception.expect(DateTimeException.class);
        exception.expectMessage(expectMessage);
        //then
        service.getColdestPlaceGivenDate(wrongDate);
    }

    @Test
    public void shouldFindColdestPlaceGivenDateAndThrowsNoSuchElementException() {
        //given
        String date = "2018-11-01";
        String expectMessage = "Cant't find any synoptic measurements for date: " + date;
        //when
        exception.expect(NoSuchElementException.class);
        exception.expectMessage(expectMessage);
        //then
        service.getColdestPlaceGivenDate(date);
    }

    @Test
    public void shouldFindColdestPlaceGivenDateAndThrowsNoSuchElementExceptionJUvintage() {
        //given
        String date = "2018-11-01";
        String expectMessage = "Cant't find any synoptic measurements for date: " + date;
        //when
        SynopticMeasurement result = new SynopticMeasurement();
        //then
        try {
            result = service.getColdestPlaceGivenDate(date);
        } catch (NoSuchElementException e) {
            assertEquals(expectMessage, e.getMessage());
        }
        assertNull(result.getId());
    }

}