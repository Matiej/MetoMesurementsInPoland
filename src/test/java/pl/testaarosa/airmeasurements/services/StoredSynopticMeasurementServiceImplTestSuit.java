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
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.repositories.MockAirMeasurementRepository;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationRepository;
import pl.testaarosa.airmeasurements.repositories.MockSynopticMeasurementRepository;
import pl.testaarosa.airmeasurements.repositories.SynopticMeasurementRepository;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StoredSynopticMeasurementServiceImplTestSuit {

    private MockMeasuringStationRepository mockMeasuringStationRepository;
    private MockAirMeasurementRepository mockAirMeasurementRepository;
    private MockSynopticMeasurementRepository mockSynopticMeasurementRepository;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private StoredSynopticMeasurementServiceImpl service;

    @Mock
    private SynopticMeasurementRepository synopticRepository;

    @Before
    public void init() {
        mockMeasuringStationRepository = new MockMeasuringStationRepository();
        mockAirMeasurementRepository = new MockAirMeasurementRepository();
        mockSynopticMeasurementRepository = new MockSynopticMeasurementRepository();
    }

    @Test
    public void shouldFindAllSynopticMeasurementsGivenDate() {
        //given
        List<SynopticMeasurement> expect = mockSynopticMeasurementRepository.synopticMeasurementsOrderHottest();
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
    public void shouldFindHottestTop10Places() {
        //given jUnit vintage ver
        List<SynopticMeasurement> expected = mockSynopticMeasurementRepository.synopticMeasurementsOrderHottest();
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
        String noOfResults = "10";
        given(synopticRepository.findAll()).willThrow(DataIntegrityViolationException.class);
        //when
        exception.expect(RuntimeException.class);
        exception.expectMessage("There is some db problem: null");
        //then
        service.getHottestPlaces(noOfResults);
    }

    @Test
    public void shouldFindHottestTop10PlacesAndThrowsNoSuchElementException() {
        //given
        String noOfResults = "10";
        //when
        exception.expect(NoSuchElementException.class);
        exception.expectMessage("Can't find hottest 10 measurements");
        //then
        service.getHottestPlaces(noOfResults);
    }

    @Test
    public void shouldFindHottestTop10PlacesAndThrowsNoSuchElementExceptionJU5() {
        //given jUnit 5 jupiter version
        String noOfResults = "10";
        String expectedMessage = "Can't find hottest 10 measurements";
        //when
        Exception exception = assertThrows(NoSuchElementException.class, () -> service.getHottestPlaces(noOfResults));
        //then
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldFindColdestTop10Places() {
        //given
        String noOfResults = "5";
        List<SynopticMeasurement> expect = mockSynopticMeasurementRepository.synopticMeasurementsOrderColdest();
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
        String noOfResults = "5";
        given(synopticRepository.findAll()).willThrow(DataIntegrityViolationException.class);
        //when
        exception.expect(RuntimeException.class);
        exception.expectMessage("There is some db problem: null");
        //then
        service.getHottestPlaces(noOfResults);
        List<SynopticMeasurement> result = service.getHottestPlaces(noOfResults);
        Assertions.assertNull(result);
    }

    @Test
    public void shouldFindColdestTop10PlacesAndThrowsDataIntegrityViolationExceptionJU5() {
        //given
        String expectMessage = "There is some db problem: null";
        given(synopticRepository.findAll()).willThrow(DataIntegrityViolationException.class);
        //when
        RuntimeException resultMessage = assertThrows(RuntimeException.class, () -> service.getColdestPlaces("10"));
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
        List<SynopticMeasurement> result = service.getColdestPlaces("5");
        assertNull(result);
    }

    @Test
    public void shouldFindColdestTop10PlacesAndThrowsNoSuchElementExceptionVintageJU() {
        //given
        String expectedMessage = "Can't find coldest 10 measurements";
        //when
        //then
        try {
            service.getColdestPlaces("5");
        } catch (NoSuchElementException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void shouldFindHottestPlaceGivenDate() {
        //given
        List<SynopticMeasurement> givenRepo = mockSynopticMeasurementRepository.synopticMeasurementsOrderHottest();
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
        assertNotEquals(givenRepo.get(3), result);
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
        List<SynopticMeasurement> givenRepo = mockSynopticMeasurementRepository.synopticMeasurementsOrderColdest();
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

