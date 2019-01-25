package pl.testaarosa.airmeasurements.services;

import org.hamcrest.collection.IsMapContaining;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.domain.dtoApi.AirMeasurementDto;
import pl.testaarosa.airmeasurements.domain.dtoApi.MeasuringStationDto;
import pl.testaarosa.airmeasurements.domain.dtoApi.SynopticMeasurementDto;
import pl.testaarosa.airmeasurements.mapper.AirMeasurementMapper;
import pl.testaarosa.airmeasurements.mapper.MeasuringStationMapper;
import pl.testaarosa.airmeasurements.mapper.SynopticMeasurementMapper;
import pl.testaarosa.airmeasurements.repositories.*;
import pl.testaarosa.airmeasurements.supplier.MeasurementsApiSupplier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApiSupplierRetrieverImplTestSuit {

    private MockMeasuringStationRepository mockMeasuringStationRepository;
    private MockSynopticMeasurementRepository mockSynopticMeasurementRepository;
    private MockOnlineMeasurementRepository onlineMeasurementRepository;
    private MockMeasuringStationDtoRepository mockMeasuringStationDtoRepository;
    private MockSynopticMeasurementDtoRepository mockSynopticMeasurementDtoRepository;
    private MockAirMeasurementDtoRepository mockAirMeasurementDtoRepository;
    private MockAirMeasurementRepository mockAirMeasurementRepository;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private ApiSupplierRetrieverImpl apiSupplierRetriever;

    @Mock
    private MeasurementsApiSupplier msApi;
    @Mock
    private MeasuringStationMapper measuringStationMapper;
    @Mock
    private AirMeasurementMapper airMeasurementMapper;
    @Mock
    private SynopticMeasurementMapper synopticMeasurementMapper;
    @Mock
    private RestTemplate restTemplate;

    @Before
    public void init() {
        mockMeasuringStationRepository = new MockMeasuringStationRepository();
        mockSynopticMeasurementRepository = new MockSynopticMeasurementRepository();
        onlineMeasurementRepository = new MockOnlineMeasurementRepository();
        mockMeasuringStationDtoRepository = new MockMeasuringStationDtoRepository();
        mockSynopticMeasurementDtoRepository = new MockSynopticMeasurementDtoRepository();
        mockAirMeasurementDtoRepository = new MockAirMeasurementDtoRepository();
        mockAirMeasurementRepository = new MockAirMeasurementRepository();
    }

    @Test
    public void shouldGetSynopticMeasurementProcessor() {
        //given
        List<SynopticMeasurement> synopticMeasurementList = mockSynopticMeasurementRepository.synopticMeasurementsOrderColdest();
        List<SynopticMeasurementDto> synopticMeasurementDtoList = mockSynopticMeasurementDtoRepository.mockSynopticDtoRepositories();
        SynopticMeasurementDto[] synopticMeasurementDtosArray = synopticMeasurementDtoList.toArray(new SynopticMeasurementDto[synopticMeasurementDtoList.size()]);
        Map<String, SynopticMeasurement> expect = mockSynopticMeasurementRepository.measurementMap();
        given(restTemplate.getForEntity(msApi.imgwApiSupplierAll(), SynopticMeasurementDto[].class))
                .willReturn(new ResponseEntity(synopticMeasurementDtosArray, HttpStatus.OK));
        //when
        IntStream.range(0, synopticMeasurementDtoList.size())
                .forEach(s-> when(synopticMeasurementMapper.maptToSynopticMeasurement(synopticMeasurementDtoList.get(s))).thenReturn(synopticMeasurementList.get(s)));
        //then
        Map<String, SynopticMeasurement> result = apiSupplierRetriever.synopticMeasurementProcessor();
        assertNotNull(result);
        assertEquals(expect,result);
    }

    @Test
    public void shouldGetSynopticMeasurementProcessorAndThrowsRestClientException() {
        //given
        String expectedMessage = "Can't find any synoptic measurement because of REST API error-> null";
        given(restTemplate.getForEntity(msApi.imgwApiSupplierAll(), SynopticMeasurementDto[].class))
                .willThrow(RestClientResponseException.class);
        //when
        exception.expect(RestClientException.class);
        exception.expectMessage(expectedMessage);
        //then
        apiSupplierRetriever.synopticMeasurementProcessor();
    }

    @Test
    public void shouldGetAirMeasurementsAndStProcessor() {
        //given
        List<AirMeasurement> airMeasurements1 = mockAirMeasurementRepository.airMeasurements1();
        List<MeasuringStation> stations = mockMeasuringStationRepository.stations();
        Map<MeasuringStation, AirMeasurement> expected = new HashMap<>();
        expected.put(stations.get(0), airMeasurements1.get(0));
        expected.put(stations.get(2), airMeasurements1.get(0));
        expected.put(stations.get(3), airMeasurements1.get(0));

        List<MeasuringStationDto> measuringStationDtos = mockMeasuringStationDtoRepository.measuringStationDtoList();
        MeasuringStationDto[] responseEntity = measuringStationDtos.toArray(new MeasuringStationDto[measuringStationDtos.size()]);
        given(restTemplate.getForEntity(msApi.giosApiSupplierAll(), MeasuringStationDto[].class))
                .willReturn(new ResponseEntity(responseEntity, HttpStatus.OK));
        List<AirMeasurementDto> airMeasurementDtos = mockAirMeasurementDtoRepository.airMeasurementsDtos();
        //when
        IntStream.range(0, stations.size()).forEach(s-> {
            when(measuringStationMapper.mapToMeasuringSt(measuringStationDtos.get(s))).thenReturn(stations.get(s));
//            when(restTemplate.getForObject(msApi.giosApiSupplierIndex(100), AirMeasurementDto.class)).thenReturn(airMeasurementDtos.get(s));
            when(restTemplate.getForEntity(msApi.giosApiSupplierIndex(100), AirMeasurementDto.class)).thenReturn(new ResponseEntity(airMeasurementDtos.get(s), HttpStatus.OK));
            when(airMeasurementMapper.mapToAirMeasurements(airMeasurementDtos.get(s))).thenReturn(airMeasurements1.get(0));
        });
        Map<MeasuringStation, AirMeasurement> result = apiSupplierRetriever.airMeasurementsAndStProcessor();
        assertNotNull(expected);
        assertThat(result, is(expected));
        assertEquals(3, result.size());
        assertThat(result, IsMapContaining.hasEntry(stations.get(0), airMeasurements1.get(0)));
        assertThat(result, IsMapContaining.hasKey(stations.get(0)));
        assertThat(result, IsMapContaining.hasKey(stations.get(1)));
        assertThat(result, IsMapContaining.hasKey(stations.get(2)));
    }

    @Test
    public void shouldGetAirMeasurementsAndStProcessorAndThrowsRestClientExceptionForEntity() {
        //given
        String expectedMessage = "Can't find any measurement because of REST API error-> null";
        String expectedMessage2 = "Error Api connection. Http response status-> ";
        given(restTemplate.getForEntity(msApi.giosApiSupplierAll(), MeasuringStationDto[].class)).willThrow(RestClientResponseException.class);
        //when
        exception.expect(RestClientException.class);
        exception.expectMessage(expectedMessage);
        exception.expectMessage(expectedMessage2);
        //then
        apiSupplierRetriever.airMeasurementsAndStProcessor();
    }


}
