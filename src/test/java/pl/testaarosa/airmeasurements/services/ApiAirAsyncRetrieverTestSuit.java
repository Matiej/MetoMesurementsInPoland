package pl.testaarosa.airmeasurements.services;

import org.junit.Before;
import org.junit.BeforeClass;
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
import pl.testaarosa.airmeasurements.domain.dtoApi.AirMeasurementDto;
import pl.testaarosa.airmeasurements.domain.dtoApi.MeasuringStationDto;
import pl.testaarosa.airmeasurements.mapper.AirMeasurementMapper;
import pl.testaarosa.airmeasurements.repositories.MockAirMeasurementDtoRepository;
import pl.testaarosa.airmeasurements.repositories.MockAirMeasurementRepository;
import pl.testaarosa.airmeasurements.supplier.MeasurementsApiSupplier;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApiAirAsyncRetrieverTestSuit {

    private MockAirMeasurementRepository airMeasurementRepository;
    private MockAirMeasurementDtoRepository airMeasurementDtoRepository;
    private List<AirMeasurementDto> airMeasurementDtos;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private ApiAirAsyncRetriever apiAirAsyncRetriever;

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private MeasurementsApiSupplier msApi;
    @Mock
    private AirMeasurementMapper airMeasurementMapper;

    @Before
    public void init() {
        airMeasurementRepository = new MockAirMeasurementRepository();
        airMeasurementDtoRepository = new MockAirMeasurementDtoRepository();
        airMeasurementDtos = airMeasurementDtoRepository.airMeasurementsDtos();
    }

    @Test
    public void shouldGetAirMeasurementByIdAsync() {
        //given
//        List<AirMeasurementDto> airMeasurementDtos = airMeasurementDtoRepository.airMeasurementsDtos();
        List<AirMeasurement> airMeasurementList = airMeasurementRepository.airMeasurements1();
        CompletableFuture<AirMeasurement> expect = CompletableFuture.completedFuture(airMeasurementList.get(0));
        AirMeasurementDto airMeasurementDto = airMeasurementDtos.get(0);
        given(restTemplate.getForEntity(msApi.giosApiSupplierIndex(airMeasurementDto.getId()), AirMeasurementDto.class))
                .willReturn(new ResponseEntity(airMeasurementDto, HttpStatus.OK));
        //when
        when(airMeasurementMapper.mapToAirMeasurements(airMeasurementDto))
                .thenReturn(airMeasurementList.get(0));
        //then
        CompletableFuture<AirMeasurement> result = apiAirAsyncRetriever.airMeasurementProcessorById(airMeasurementDto.getId());
        assertNotNull(result);
        assertEquals(expect.join(),result.join());
        assertNotEquals(airMeasurementList.get(1), result.join());
    }

    @Test
    public void shouldGetAirMeasurementByIdAsyncAndThrowsRestClientExceptionStatus400() {
        String expectedMessage = "Can't find any air measurement because of external API error. HTTP Status code => 400";
        AirMeasurementDto airMeasurementDto = airMeasurementDtos.get(0);
        int id = airMeasurementDto.getId();
        given(restTemplate.getForEntity(msApi.giosApiSupplierIndex(id), AirMeasurementDto.class))
                .willReturn(new ResponseEntity(airMeasurementDto, HttpStatus.BAD_REQUEST));
        //when
        exception.expect(RestClientException.class);
        exception.expectMessage(expectedMessage);
        //then
        apiAirAsyncRetriever.airMeasurementProcessorById(id);
    }
    @Test
    public void shouldGetAirMeasurementByIdAsyncAndThrowsRestClientExceptionStatus502() {
        String expectedMessage = "Can't find any air measurement because of external API error. HTTP Status code => 502";
        AirMeasurementDto airMeasurementDto = airMeasurementDtos.get(0);
        int id = airMeasurementDto.getId();
        given(restTemplate.getForEntity(msApi.giosApiSupplierIndex(id), AirMeasurementDto.class))
                .willReturn(new ResponseEntity(airMeasurementDto, HttpStatus.BAD_GATEWAY));
        //when
        exception.expect(RestClientException.class);
        exception.expectMessage(expectedMessage);
        //then
        apiAirAsyncRetriever.airMeasurementProcessorById(id);
    }

    @Test
    public void shouldGetAirMeasurementByIdAsyncAndThrowsRestClientExceptionNoConnection() {
        String expectedMessage = "External Api error. Can't find any air measurement because of error-> no connection";
        AirMeasurementDto airMeasurementDto = airMeasurementDtos.get(0);
        int id = airMeasurementDto.getId();
        given(restTemplate.getForEntity(msApi.giosApiSupplierIndex(id), AirMeasurementDto.class))
                .willThrow(ResourceAccessException.class);
        //when
        exception.expect(RestClientException.class);
        exception.expectMessage(expectedMessage);
        //then
        apiAirAsyncRetriever.airMeasurementProcessorById(id);
    }


}
