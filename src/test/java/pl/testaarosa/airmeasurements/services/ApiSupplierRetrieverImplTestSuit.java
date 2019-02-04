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
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.domain.dtoApi.MeasuringStationDto;
import pl.testaarosa.airmeasurements.domain.dtoApi.SynopticMeasurementDto;
import pl.testaarosa.airmeasurements.mapper.MeasuringStationMapper;
import pl.testaarosa.airmeasurements.mapper.SynopticMeasurementMapper;
import pl.testaarosa.airmeasurements.repositories.*;
import pl.testaarosa.airmeasurements.supplier.MeasurementsApiSupplier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
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
    private MockAirMeasurementDtoRepository mockAirMeasurementDtoRepository;
    private MockAirMeasurementRepository mockAirMeasurementRepository;
    private List<MeasuringStationDto> measuringStationDtoList;
    private MeasuringStationDto[] measuringStRresponseEntity;
    private List<SynopticMeasurementDto> synopticMeasurementDtoList;
    private SynopticMeasurementDto[] synopticMstResponseEntity;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private ApiSupplierRetrieverImpl apiSupplierRetriever;

    @Mock
    private MeasurementsApiSupplier msApi;
    @Mock
    private MeasuringStationMapper measuringStationMapper;
    @Mock
    private SynopticMeasurementMapper synopticMeasurementMapper;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ApiAirAsyncRetriever apiAirAsyncRetriever;

    @Before
    public void init() {
        mockMeasuringStationRepository = new MockMeasuringStationRepository();
        mockSynopticMeasurementRepository = new MockSynopticMeasurementRepository();
        MockMeasuringStationDtoRepository mockMeasuringStationDtoRepository = new MockMeasuringStationDtoRepository();
        MockSynopticMeasurementDtoRepository mockSynopticMeasurementDtoRepository = new MockSynopticMeasurementDtoRepository();
        mockAirMeasurementDtoRepository = new MockAirMeasurementDtoRepository();
        mockAirMeasurementRepository = new MockAirMeasurementRepository();
        measuringStationDtoList = mockMeasuringStationDtoRepository.measuringStationDtoList();
        measuringStRresponseEntity = measuringStationDtoList.toArray(new MeasuringStationDto[measuringStationDtoList.size()]);
        synopticMeasurementDtoList = mockSynopticMeasurementDtoRepository.mockSynopticDtoRepositories();
        synopticMstResponseEntity = synopticMeasurementDtoList.toArray(new SynopticMeasurementDto[synopticMeasurementDtoList.size()]);
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

        given(restTemplate.getForEntity(msApi.giosApiSupplierAll(), MeasuringStationDto[].class))
                .willReturn(new ResponseEntity(measuringStRresponseEntity, HttpStatus.OK));
        //when
        IntStream.range(0, stations.size()).forEach(s -> {
            when(measuringStationMapper.mapToMeasuringSt(measuringStationDtoList.get(s))).thenReturn(stations.get(s));
            when(apiAirAsyncRetriever.airMeasurementProcessorById(s)).thenReturn(CompletableFuture.completedFuture(airMeasurements1.get(0)));
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
        String expectedMessage = "External Api error. Can't find any measuring station or air measurement because of error-> no connection";
        given(restTemplate.getForEntity(msApi.giosApiSupplierAll(), MeasuringStationDto[].class)).willThrow(ResourceAccessException.class);
        //when
        exception.expect(RestClientException.class);
        exception.expectMessage(expectedMessage);
        //then
        apiSupplierRetriever.airMeasurementsAndStProcessor();
    }

    @Test
    public void shouldGetAirMeasurementsAndStProcessorAndThrowsRestClientExceptionForEntityStatus400() {
        //given
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        given(restTemplate.getForEntity(msApi.giosApiSupplierAll(), MeasuringStationDto[].class)).willReturn(new ResponseEntity(measuringStRresponseEntity, badRequest));
        String expectedMessage = "Can't find any measuring station because of external API error. HTTP Status code => " + badRequest.toString();
        //when
        exception.expect(RestClientException.class);
        exception.expectMessage(expectedMessage);
        //then
        apiSupplierRetriever.airMeasurementsAndStProcessor();
    }

    @Test
    public void shouldGetAirMeasurementsAndStProcessorAndThrowsRestClientExceptionForEntityStatusTeaPot() {
        //given
        HttpStatus teaStatus = HttpStatus.I_AM_A_TEAPOT;
        given(restTemplate.getForEntity(msApi.giosApiSupplierAll(), MeasuringStationDto[].class)).willReturn(new ResponseEntity(measuringStRresponseEntity, teaStatus));
        String expectedMessage = "Can't find any measuring station because of external API error. HTTP Status code => " + teaStatus.toString();
        //when
        exception.expect(RestClientException.class);
        exception.expectMessage(expectedMessage);
        //then
        apiSupplierRetriever.airMeasurementsAndStProcessor();
    }

    @Test
    public void shouldGetSynopticMeasurementProcessor() {
        //given
        List<SynopticMeasurement> synopticMeasurementList = mockSynopticMeasurementRepository.synopticMeasurementsOrderColdest();
        Map<String, SynopticMeasurement> expect = mockSynopticMeasurementRepository.measurementMap();
        given(restTemplate.getForEntity(msApi.imgwApiSupplierAll(), SynopticMeasurementDto[].class))
                .willReturn(new ResponseEntity(synopticMstResponseEntity, HttpStatus.OK));
        //when
        IntStream.range(0, synopticMeasurementDtoList.size())
                .forEach(s -> when(synopticMeasurementMapper.maptToSynopticMeasurement(synopticMeasurementDtoList.get(s))).thenReturn(synopticMeasurementList.get(s)));
        //then
        Map<String, SynopticMeasurement> result = apiSupplierRetriever.synopticMeasurementProcessor();
        assertNotNull(result);
        assertEquals(expect, result);
    }

    @Test
    public void shouldGetSynopticMeasurementProcessorAndThrowsRestClientException() {
        //given
        String expectedMessage = "External Api error. Can't find any synoptic measurement because of error-> no connection";
        given(restTemplate.getForEntity(msApi.imgwApiSupplierAll(), SynopticMeasurementDto[].class))
                .willThrow(ResourceAccessException.class);
        //when
        exception.expect(RestClientException.class);
        exception.expectMessage(expectedMessage);
        //then
        apiSupplierRetriever.synopticMeasurementProcessor();
    }

    @Test
    public void shouldGetSynopticMeasurementProcessorAndThrowsRestClientExceptionStatus400() {
        //given
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        given(restTemplate.getForEntity(msApi.imgwApiSupplierAll(), SynopticMeasurementDto[].class))
                .willReturn(new ResponseEntity(synopticMstResponseEntity, badRequest));
        String expectedMessage = "Can't find any synoptic measurement because of external API error. HTTP Status code => " + badRequest.toString();
        //when
        exception.expect(RestClientException.class);
        exception.expectMessage(expectedMessage);
        //then
        apiSupplierRetriever.synopticMeasurementProcessor();
    }

    @Test
    public void shouldGetSynopticMeasurementProcessorAndThrowsRestClientExceptionStatus() {
        //given
        HttpStatus status = HttpStatus.BAD_GATEWAY;
        given(restTemplate.getForEntity(msApi.imgwApiSupplierAll(), SynopticMeasurementDto[].class))
                .willReturn(new ResponseEntity(synopticMstResponseEntity, status));
        String expectedMessage = "Can't find any synoptic measurement because of external API error. HTTP Status code => " + status.toString();
        //when
        exception.expect(RestClientException.class);
        exception.expectMessage(expectedMessage);
        //then
        apiSupplierRetriever.synopticMeasurementProcessor();
    }

}
