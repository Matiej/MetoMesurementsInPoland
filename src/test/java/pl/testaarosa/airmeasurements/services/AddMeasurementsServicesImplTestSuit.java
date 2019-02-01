package pl.testaarosa.airmeasurements.services;

import org.assertj.core.api.AssertionsForClassTypes;
import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.City;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddMeasurementsServicesImplTestSuit {

    private MockMeasuringStationRepository mockMeasuringStationRepository;
    private MockAirMeasurementRepository mockAirMeasurementRepository;
    private MockSynopticMeasurementRepository mockSynopticMeasurementRepository;
    private MockCityRepository mockCityRepository;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private AddMeasurementsServiceImpl addMeasurementsService;

    @Mock
    private ApiSupplierRetriever apiSupplierRetriever;
    @Mock
    private MeasuringStationRepository measuringStationRepository;
    @Mock
    private SynopticMeasurementRepository synopticRepository;
    @Mock
    private AirMeasurementRepository airRepository;
    @Mock
    private EmailNotifierService emailNotifierService;
    @Mock
    private AddMeasurementRaportGenerator addMeasurementRaportGenerator;
    @Mock
    private CityRepository cityRepository;

    @Before
    public void init() {
        mockMeasuringStationRepository = new MockMeasuringStationRepository();
        mockAirMeasurementRepository = new MockAirMeasurementRepository();
        mockSynopticMeasurementRepository = new MockSynopticMeasurementRepository();
        mockCityRepository = new MockCityRepository();
    }

    @Test
    public void shouldAddAllStationMeasurements() {
        List<City> cities = mockCityRepository.cityList();
        List<MeasuringStation> stationsRepo = mockMeasuringStationRepository.stations();
        stationsRepo.remove(0);

        Map<String, SynopticMeasurement> synopticRepoMap = mockSynopticMeasurementRepository.measurementMap();
        given(apiSupplierRetriever.synopticMeasurementProcessor()).willReturn(synopticRepoMap);
        Map<MeasuringStation, AirMeasurement> measurementMap = mockMeasuringStationRepository.measurementMap();
        given(apiSupplierRetriever.airMeasurementsAndStProcessor()).willReturn(measurementMap);


        //when
        for (Map.Entry<MeasuringStation, AirMeasurement> msEntry : measurementMap.entrySet()) {
            MeasuringStation key = msEntry.getKey();
            when(measuringStationRepository.save(key))
                    .thenReturn(stationsRepo.stream().filter(t -> t.equals(key)).findAny().get());
            String city = key.getCity();
            when(cityRepository.findOneByCityName(city))
                    .thenReturn(cities.stream().filter(t -> t.getCityName().equals(city)).findAny().get());
        }

        List<MeasuringStation> resultList = addMeasurementsService.addMeasurementsAllStations();
        //then
        assertNotNull(resultList);
        assertEquals(stationsRepo, resultList);
        AssertionsForClassTypes.assertThat(stationsRepo.equals(resultList)).isTrue();
        stationsRepo.remove(0);
        assertNotSame(stationsRepo, resultList);
    }

    @Test
    public void shouldAddAllStationMeasurementsAndThrowsRestClientExceptionAir() {
        //given
        given(apiSupplierRetriever.airMeasurementsAndStProcessor()).willThrow(ResourceAccessException.class);
        //when
        exception.expect(RestClientException.class);
        //then
        addMeasurementsService.addMeasurementsAllStations();
    }

    @Test
    public void shouldAddAllStationMeasurementsAndThrowsRestClientExceptionAJ() {
        //given
        given(apiSupplierRetriever.airMeasurementsAndStProcessor()).willThrow(ResourceAccessException.class);
        //when
        //then
        assertThatThrownBy(() -> addMeasurementsService.addMeasurementsAllStations())
                .isInstanceOf(RestClientException.class)
                .hasNoCause();
    }

    @Test
    public void shouldAddAllStationMeasurementsAndThrowsRestClientExceptionSyn() {
        //given
        given(apiSupplierRetriever.synopticMeasurementProcessor()).willThrow(ResourceAccessException.class);
        //when
        exception.expect(RestClientException.class);
        //then
        addMeasurementsService.addMeasurementsAllStations();
    }

    @Test
    public void shouldAddAllStationMeasurementsAndThrowsHibernateExceptionStRepo() {
        //given
        List<MeasuringStation> stationsRepo = mockMeasuringStationRepository.stations();
        stationsRepo.remove(0);
        Map<MeasuringStation, AirMeasurement> measurementMap = mockMeasuringStationRepository.measurementMap();
        given(apiSupplierRetriever.airMeasurementsAndStProcessor()).willReturn(measurementMap);
        String expectMessage = "Can't save station because of data base error";
        //when
        for (Map.Entry<MeasuringStation, AirMeasurement> msEntry : measurementMap.entrySet()) {
            MeasuringStation key = msEntry.getKey();
            when(measuringStationRepository.save(key))
                    .thenThrow(HibernateException.class);
        }
        exception.expect(RuntimeException.class);
        exception.expectMessage(expectMessage);
        //then
        addMeasurementsService.addMeasurementsAllStations();
    }

    @Test
    public void shouldAddAllStationMeasurementsAndThrowsHibernateExceptionCityRepo() {
        //given
        List<MeasuringStation> stationsRepo = mockMeasuringStationRepository.stations();
        stationsRepo.remove(0);
        Map<MeasuringStation, AirMeasurement> measurementMap = mockMeasuringStationRepository.measurementMap();
        given(apiSupplierRetriever.airMeasurementsAndStProcessor()).willReturn(measurementMap);
        String expectMessage = "Can't save station because of data base error";
        //when
        when(cityRepository.existsAllByCityName("Warszawa")).thenReturn(true);
        for (Map.Entry<MeasuringStation, AirMeasurement> msEntry : measurementMap.entrySet()) {
            MeasuringStation key = msEntry.getKey();
            when(measuringStationRepository.save(key))
                    .thenReturn(stationsRepo.stream().filter(t -> t.equals(key)).findAny().get());
            String city = key.getCity();
            when(cityRepository.findOneByCityName(city))
                    .thenThrow(HibernateException.class);
        }
        exception.expect(RuntimeException.class);
        exception.expectMessage(expectMessage);
        //then
        addMeasurementsService.addMeasurementsAllStations();
    }

    @Test
    public void shouldAddAllStationMeasurementsAndThrowsHibernateExceptionAirRepo() {
        //given
        List<MeasuringStation> stationsRepo = mockMeasuringStationRepository.stations();
        stationsRepo.remove(0);
        Map<MeasuringStation, AirMeasurement> measurementMap = mockMeasuringStationRepository.measurementMap();
        given(apiSupplierRetriever.airMeasurementsAndStProcessor()).willReturn(measurementMap);
        String expectMessage = "Can't save station because of data base error";
        //when
        when(airRepository.save(measurementMap.get(stationsRepo.get(0)))).thenThrow(HibernateException.class);
        exception.expect(RuntimeException.class);
        exception.expectMessage(expectMessage);
        //then
        addMeasurementsService.addMeasurementsAllStations();
    }

    @Test
    public void shouldAddAllStationMeasurementsAndThrowsHibernateExceptionSynopticRepo() {
        //given
        List<City> cities = mockCityRepository.cityList();
        List<MeasuringStation> stationsRepo = mockMeasuringStationRepository.stations();
        stationsRepo.remove(0);
        Map<String, SynopticMeasurement> synopticRepoMap = mockSynopticMeasurementRepository.measurementMap();
        given(apiSupplierRetriever.synopticMeasurementProcessor()).willReturn(synopticRepoMap);
        Map<MeasuringStation, AirMeasurement> measurementMap = mockMeasuringStationRepository.measurementMap();
        given(apiSupplierRetriever.airMeasurementsAndStProcessor()).willReturn(measurementMap);
        String expectMessage = "Can't save synoptic measurements because of data base error";
        //when
        when(cityRepository.existsAllByCityName("Warszawa")).thenReturn(true);
        for (Map.Entry<MeasuringStation, AirMeasurement> msEntry : measurementMap.entrySet()) {
            MeasuringStation key = msEntry.getKey();
            when(measuringStationRepository.save(key))
                    .thenReturn(stationsRepo.stream().filter(t -> t.equals(key)).findAny().get());
            String city = key.getCity();
            when(cityRepository.findOneByCityName(city))
                    .thenReturn(cities.stream().filter(t -> t.getCityName().equals(city)).findAny().get());
        }
        when(synopticRepository.save(synopticRepoMap.get("Warszawa"))).thenThrow(HibernateException.class);
        exception.expect(RuntimeException.class);
        exception.expectMessage(expectMessage);
        //then
        addMeasurementsService.addMeasurementsAllStations();
    }

    @Test
    public void shouldAddOneStationMeasurement() {
        List<City> cities = mockCityRepository.cityList();
        List<MeasuringStation> stationsRepo = mockMeasuringStationRepository.stations();
        stationsRepo.remove(0);
        MeasuringStation station = stationsRepo.get(0);
        Integer stationId = station.getStationId();

        Map<String, SynopticMeasurement> synopticRepoMap = mockSynopticMeasurementRepository.measurementMap();
        given(apiSupplierRetriever.synopticMeasurementProcessor()).willReturn(synopticRepoMap);
        Map<MeasuringStation, AirMeasurement> measurementMap = mockMeasuringStationRepository.measurementMap();
        measurementMap.remove(stationsRepo.get(1));
        measurementMap.remove(stationsRepo.get(2));
        given(apiSupplierRetriever.airMeasurementsAndStProcessor(stationId)).willReturn(measurementMap);
        //when
        for (Map.Entry<MeasuringStation, AirMeasurement> msEntry : measurementMap.entrySet()) {
            MeasuringStation key = msEntry.getKey();
            when(measuringStationRepository.save(key))
                    .thenReturn(stationsRepo.get(0));
            String city = key.getCity();
            when(cityRepository.findOneByCityName(city))
                    .thenReturn(cities.get(0));
        }
        MeasuringStation result = addMeasurementsService.addOneStationMeasurement(stationId);
        //then
        assertNotNull(result);
        assertEquals(station, result);
        assertNotEquals(stationsRepo.get(1), result);
        assertNotSame(stationsRepo.get(2), result);
    }

    @Test
    public void shouldAddOneStationMeasurementAndThrowsNumberFormatException() {
        //given
        String expectedMessage = "StationID -> null is empty or format is incorrect!";
        Integer stationId = null;
        //when
        exception.expect(NumberFormatException.class);
        exception.expectMessage(expectedMessage);
        //then
        addMeasurementsService.addOneStationMeasurement(stationId);
    }

    @Test
    public void shouldAddOneStationMeasurementAndThrowsHibernateException() {
        List<MeasuringStation> stationsRepo = mockMeasuringStationRepository.stations();
        stationsRepo.remove(0);
        MeasuringStation station = stationsRepo.get(0);
        Integer stationId = station.getStationId();
        String expectedMessage = "Can't save station because of data base error";
        Map<MeasuringStation, AirMeasurement> measurementMap = mockMeasuringStationRepository.measurementMap();
        measurementMap.remove(stationsRepo.get(1));
        measurementMap.remove(stationsRepo.get(2));
        given(apiSupplierRetriever.airMeasurementsAndStProcessor(stationId)).willReturn(measurementMap);
        //when
        for (Map.Entry<MeasuringStation, AirMeasurement> msEntry : measurementMap.entrySet()) {
            MeasuringStation key = msEntry.getKey();
            when(measuringStationRepository.save(key))
                    .thenThrow(HibernateException.class);
        }
        exception.expect(RuntimeException.class);
        exception.expectMessage(expectedMessage);
        //then
        addMeasurementsService.addOneStationMeasurement(stationId);
    }

    @Test
    public void shouldAddOneStationMeasurementAndThrowsNoSuchElementException() {
        List<MeasuringStation> stationsRepo = mockMeasuringStationRepository.stations();
        MeasuringStation station = stationsRepo.get(0);
        Integer stationId = station.getStationId();
        given(apiSupplierRetriever.airMeasurementsAndStProcessor(stationId)).willThrow(NoSuchElementException.class);
        //when
        exception.expect(NoSuchElementException.class);
        //then
        addMeasurementsService.addOneStationMeasurement(stationId);
    }
}
