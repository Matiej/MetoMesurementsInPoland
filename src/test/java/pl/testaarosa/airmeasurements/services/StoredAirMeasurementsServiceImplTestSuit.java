package pl.testaarosa.airmeasurements.services;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.AirMeasurementLevel;
import pl.testaarosa.airmeasurements.repositories.*;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StoredAirMeasurementsServiceImplTestSuit {
    private MockMeasuringStationRepository mockMeasuringStationRepository;
    private MockAirMeasurementRepository mockAirMeasurementRepository;
    private MockSynopticMeasurementRepository mockSynopticMeasurementRepository;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private StoredAirMeasurementsServiceImpl service;

    @Mock
    private MeasuringStationRepository stationRepository;

    @Mock
    private AirMeasurementRepository airRepository;


    @Before
    public void init() {
        mockMeasuringStationRepository = new MockMeasuringStationRepository();
        mockAirMeasurementRepository = new MockAirMeasurementRepository();
        mockSynopticMeasurementRepository = new MockSynopticMeasurementRepository();
    }

    @Test
    public void shouldFindMeasurementsByAirQuality() {
        //given
        List<AirMeasurement> measurementsList = mockAirMeasurementRepository.airMeasurements1();
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
        List<AirMeasurement> expected = mockAirMeasurementRepository.airMeasurements1();
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
}
