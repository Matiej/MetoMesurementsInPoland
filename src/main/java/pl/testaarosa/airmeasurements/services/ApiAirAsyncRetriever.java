package pl.testaarosa.airmeasurements.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.dtoApi.AirMeasurementDto;
import pl.testaarosa.airmeasurements.mapper.AirMeasurementMapper;
import pl.testaarosa.airmeasurements.supplier.MeasurementsApiSupplier;

import java.util.concurrent.CompletableFuture;

import static pl.testaarosa.airmeasurements.services.ConsolerData.ANSI_GREEN;
import static pl.testaarosa.airmeasurements.services.ConsolerData.ANSI_RESET;

@Service
public class ApiAirAsyncRetriever {

    private Logger LOGGER = LoggerFactory.getLogger(ApiAirAsyncRetriever.class);
    private final RestTemplate restTemplate;
    private final MeasurementsApiSupplier msApi;
    private final AirMeasurementMapper airMeasurementMapper;

    @Autowired
    public ApiAirAsyncRetriever(RestTemplate restTemplate, MeasurementsApiSupplier msApi,
                                    AirMeasurementMapper airMeasurementMapper) {
        this.airMeasurementMapper = airMeasurementMapper;
        this.restTemplate = restTemplate;
        this.msApi = msApi;
    }

    @Async()
    protected CompletableFuture<AirMeasurement> airMeasurementProcessorById(int stationId) throws RestClientException {
        try {
            ResponseEntity<AirMeasurementDto> responseEntity = restTemplate.getForEntity(msApi.giosApiSupplierIndex(stationId), AirMeasurementDto.class);
            HttpStatus statusCode = responseEntity.getStatusCode();
            if(!statusCode.is2xxSuccessful()) {
                throw new RestClientException("Can't find any air measurement because of external API error. HTTP Status code => " + statusCode.toString());
            }
            AirMeasurementDto airMeasurementDto = responseEntity.getBody();
            LOGGER.info(ANSI_GREEN + "RECEIVED AIR MEASUREMENT FOR STATION => " + stationId + ANSI_RESET);
            return CompletableFuture.completedFuture(airMeasurementMapper.mapToAirMeasurements(airMeasurementDto));
        } catch (ResourceAccessException e) {
            throw new RestClientException("External Api error. Can't find any air measurement because of error-> no connection");
        }
    }
}
