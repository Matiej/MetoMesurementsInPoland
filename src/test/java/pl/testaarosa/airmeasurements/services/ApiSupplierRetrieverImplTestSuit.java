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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.instanceOf;
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
    private Method airMeasurementProcessorById;

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
        MockMeasuringStationDtoRepository mockMeasuringStationDtoRepository = new MockMeasuringStationDtoRepository();
        MockSynopticMeasurementDtoRepository mockSynopticMeasurementDtoRepository = new MockSynopticMeasurementDtoRepository();
        mockAirMeasurementDtoRepository = new MockAirMeasurementDtoRepository();
        mockAirMeasurementRepository = new MockAirMeasurementRepository();
        measuringStationDtoList = mockMeasuringStationDtoRepository.measuringStationDtoList();
        measuringStRresponseEntity = measuringStationDtoList.toArray(new MeasuringStationDto[measuringStationDtoList.size()]);
        synopticMeasurementDtoList = mockSynopticMeasurementDtoRepository.mockSynopticDtoRepositories();
        synopticMstResponseEntity = synopticMeasurementDtoList.toArray(new SynopticMeasurementDto[synopticMeasurementDtoList.size()]);

        try {
            airMeasurementProcessorById = ApiSupplierRetrieverImpl.class.getDeclaredMethod("airMeasurementProcessorById", int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
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
        List<AirMeasurementDto> airMeasurementDtos = mockAirMeasurementDtoRepository.airMeasurementsDtos();
        //when
        IntStream.range(0, stations.size()).forEach(s -> {
            when(measuringStationMapper.mapToMeasuringSt(measuringStationDtoList.get(s))).thenReturn(stations.get(s));
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

    @Test
    public void shouldGetAirMeasurementsByIdAndThrowsRestClientExceptionStatus400() {
        //given
        airMeasurementProcessorById.setAccessible(true);
        given(restTemplate.getForEntity(msApi.giosApiSupplierIndex(1), AirMeasurementDto.class))
                .willReturn(new ResponseEntity(mockAirMeasurementDtoRepository.airMeasurementsDtos().get(0), HttpStatus.BAD_REQUEST));
        String expectMessage = "Can't find any air measurement because of external API error. HTTP Status code => 400";
        //when
        //then
        try {
            airMeasurementProcessorById.invoke(apiSupplierRetriever, 1);
        } catch (InvocationTargetException | IllegalAccessException e) {
            assertThat(e.getCause(), instanceOf(RestClientException.class));
            assertEquals(expectMessage, e.getCause().getMessage());
        }
    }

    @Test
    public void shouldGetAirMeasurementsByIdAndThrowsRestClientExceptionStatus502() {
        //given
        airMeasurementProcessorById.setAccessible(true);
        given(restTemplate.getForEntity(msApi.giosApiSupplierIndex(1), AirMeasurementDto.class))
                .willReturn(new ResponseEntity(mockAirMeasurementDtoRepository.airMeasurementsDtos().get(0), HttpStatus.BAD_GATEWAY));
        String expectMessage = "Can't find any air measurement because of external API error. HTTP Status code => 502";
        //when
        //then
        try {
            airMeasurementProcessorById.invoke(apiSupplierRetriever, 1);
        } catch (InvocationTargetException | IllegalAccessException e) {
            assertThat(e.getCause(), instanceOf(RestClientException.class));
            assertEquals(expectMessage, e.getCause().getMessage());
        }
    }

    @Test
    public void shouldGetAirMeasurementsByIdAndThrowsResourceAccessException() {
        //given
        airMeasurementProcessorById.setAccessible(true);
        given(restTemplate.getForEntity(msApi.giosApiSupplierIndex(1), AirMeasurementDto.class))
                .willThrow(ResourceAccessException.class);
        String expectMessage = "External Api error. Can't find any air measurement because of error-> no connection";
        //when
        //then
        try {
            airMeasurementProcessorById.invoke(apiSupplierRetriever, 1);
        } catch (InvocationTargetException | IllegalAccessException e) {
            assertThat(e.getCause(), instanceOf(RestClientException.class));
            assertEquals(expectMessage, e.getCause().getMessage());
        }
    }
}
