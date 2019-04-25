package pl.testaarosa.airmeasurements.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
import pl.testaarosa.airmeasurements.supplier.MeasurementsApiSupplier;

import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static pl.testaarosa.airmeasurements.services.ConsolerData.*;

@Service
public class ApiSupplierRetrieverImpl implements ApiSupplierRetriever {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiSupplierRetrieverImpl.class);

    private final RestTemplate restTemplate;
    private final MeasurementsApiSupplier msApi;
    private final MeasuringStationMapper measuringStationMapper;
    private final SynopticMeasurementMapper synopticMeasurementMapper;
    private final ApiAirAsyncRetriever apiAirAsyncRetriever;

    @Autowired
    public ApiSupplierRetrieverImpl(MeasuringStationMapper measuringStationMapper, RestTemplate restTemplate, MeasurementsApiSupplier msApi,
                                    SynopticMeasurementMapper synopticMeasurementMapper,ApiAirAsyncRetriever apiAirAsyncRetriever) {
        this.measuringStationMapper = measuringStationMapper;
        this.synopticMeasurementMapper = synopticMeasurementMapper;
        this.restTemplate = restTemplate;
        this.msApi = msApi;
        this.apiAirAsyncRetriever = apiAirAsyncRetriever;
    }

    @Override
    public Map<MeasuringStation, AirMeasurement> airMeasurementsAndStProcessor(Integer stationID) throws RestClientException,
            NoSuchElementException {
        LOGGER.info(ANSI_YELLOW + "LOOOKING FOR MEASURING STATIONS" + ANSI_RESET);
        LinkedHashMap<MeasuringStation, CompletableFuture<AirMeasurement>> measurementMap = new LinkedHashMap<>();
        try {
            ResponseEntity<MeasuringStationDto[]> responseEntity = restTemplate
                    .getForEntity(msApi.giosApiSupplierAll(), MeasuringStationDto[].class);
            HttpStatus statusCode = responseEntity.getStatusCode();
            if (!statusCode.is2xxSuccessful()) {
                throw new RestClientException("Can't find any measuring station because of external API error. HTTP Status code => " + statusCode.toString());
            }
            MeasuringStationDto[] measuringStationDtos = responseEntity.getBody();
            List<MeasuringStation> mStList = Arrays.stream(measuringStationDtos)
                    .map(measuringStationMapper::mapToMeasuringSt)
                    .collect(Collectors.toList());
            LOGGER.info(ANSI_YELLOW + "GOT MEASURING STATIONS => " + mStList.size() + ANSI_RESET);

            mStList.forEach(st -> {
                if (stationID != 0) {
                    if (st.getStationId() == stationID) {
//                        CompletableFuture<AirMeasurement> airMeasurementCompletableFuture = airMeasurementProcessorById(stationID);
                        measurementMap.put(st, apiAirAsyncRetriever.airMeasurementProcessorById(stationID));
                    } else if (!mStList.stream().anyMatch(s -> s.getStationId() == stationID)) {
                        throw new NoSuchElementException("Can't find station id: " + stationID + " in data base!");
                    }
                } else {
//                    CompletableFuture<AirMeasurement> airMeasurementCompletableFuture = airMeasurementProcessorById(st.getStationId());
                    measurementMap.put(st, apiAirAsyncRetriever.airMeasurementProcessorById(st.getStationId()));
                }
            });
        } catch (ResourceAccessException e) {
            throw new RestClientException("External Api error. Can't find any measuring station or air measurement because of error-> no connection");
        }
        return measurementMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, v -> (v.getValue().join())));
    }

    @Override
    public Map<MeasuringStation, AirMeasurement> airMeasurementsAndStProcessor() throws RestClientException, NoSuchElementException {
        return airMeasurementsAndStProcessor(0);
    }

    @Override
    public Map<String, SynopticMeasurement> synopticMeasurementProcessor() throws RestClientException {
        try {
            URI uri = msApi.imgwApiSupplierAll();
            ResponseEntity<SynopticMeasurementDto[]> responseEntity = restTemplate.getForEntity(uri,
                    SynopticMeasurementDto[].class);
            HttpStatus statusCode = responseEntity.getStatusCode();
            if (!statusCode.is2xxSuccessful()) {
                LOGGER.error("Can't find any synoptic measurement because of external API error. HTTP Status code => ");
                throw new RestClientException("Can't find any synoptic measurement because of external API error. HTTP Status code => " + statusCode.toString());
            }
            SynopticMeasurementDto[] measurementDtos = responseEntity.getBody();
            LOGGER.info(ANSI_BLUE + "RECEIVED SYNOPTIC MEASUREMENTS. GOT => " + measurementDtos.length + ANSI_RESET);
            return Arrays.stream(measurementDtos)
                    .collect(Collectors.toMap(SynopticMeasurementDto::getCity, synopticMeasurementMapper::maptToSynopticMeasurement, (o, n) -> n, LinkedHashMap::new));
        } catch (ResourceAccessException e) {
            throw new RestClientException("External Api error. Can't find any synoptic measurement because of error-> no connection");
        }
    }
}
