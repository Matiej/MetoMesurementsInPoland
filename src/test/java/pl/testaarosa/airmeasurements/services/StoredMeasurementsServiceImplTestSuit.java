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
    private MockMeasuringStationRepository mockMeasuringStationRepository;
    private MockAirMeasurementRepository mockAirMeasurementRepository;
    private MockSynopticMeasurementRepository mockSynopticMeasurementRepository;

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
        mockMeasuringStationRepository = new MockMeasuringStationRepository();
        mockAirMeasurementRepository = new MockAirMeasurementRepository();
        mockSynopticMeasurementRepository = new MockSynopticMeasurementRepository();
    }

    @Test
    public void shouldFindAllStoredMeasurements() {
        //given
        List<MeasuringStation> expect = mockMeasuringStationRepository.stations();
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

}