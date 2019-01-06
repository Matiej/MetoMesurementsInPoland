package pl.testaarosa.airmeasurements.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
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
import pl.testaarosa.airmeasurements.supplier.MeasuringStationApiSupplier;
import pl.testaarosa.airmeasurements.supplier.SynopticStationApiSupplier;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static pl.testaarosa.airmeasurements.services.ConsolerData.ANSI_RESET;
import static pl.testaarosa.airmeasurements.services.ConsolerData.ANSI_YELLOW;

@Service
public class ApiSupplierRetrieverImpl implements ApiSupplierRetriever {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiSupplierRetrieverImpl.class);
    private RestTemplate restTemplate = new RestTemplate();

    private final MeasuringStationMapper measuringStationMapper;
    private final AirMeasurementMapper airMeasurementMapper;
    private final SynopticMeasurementMapper synopticMeasurementMapper;

    @Autowired
    public ApiSupplierRetrieverImpl(MeasuringStationMapper measuringStationMapper,
                                    AirMeasurementMapper airMeasurementMapper, SynopticMeasurementMapper synopticMeasurementMapper) {
        this.measuringStationMapper = measuringStationMapper;
        this.airMeasurementMapper = airMeasurementMapper;
        this.synopticMeasurementMapper = synopticMeasurementMapper;
    }

    @Async
    @Override
    public CompletableFuture<List<MeasuringStation>> measuringStationApiProcessor() throws RestClientException {
        try {
            String url = MeasuringStationApiSupplier.ALL_MEASURING_STATIONS_API_URL;
            ResponseEntity<MeasuringStationDto[]> responseEntity = restTemplate
                    .getForEntity(url, MeasuringStationDto[].class);
            MeasuringStationDto[] measuringStationDtos = responseEntity.getBody();
            LOGGER.info(ANSI_YELLOW + "LOOKING MEASURING STATIONS.GOT => " + measuringStationDtos.length + ANSI_RESET);
            return CompletableFuture.completedFuture(Arrays
                    .stream(measuringStationDtos)
                    .parallel()
                    .map(measuringStationMapper::mapToMeasuringSt)
                    .collect(Collectors.toList()));
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RuntimeException(" REST API CONNECTION ERROR-> " + e.getMessage());
        }
    }

    @Async
    @Override
    public CompletableFuture<Map<String, SynopticMeasurement>> synopticMeasurementProcessor() throws RestClientException {
        try {
            String url = SynopticStationApiSupplier.ALL_SYNOPTIC_STATIONS_API_URL;
            ResponseEntity<SynopticMeasurementDto[]> responseEntity = restTemplate.getForEntity(url,
                    SynopticMeasurementDto[].class);
            SynopticMeasurementDto[] measurementDtos = responseEntity.getBody();
            LOGGER.info("\u001B[32mLOOOKING FOR SYNOPTIC MEASUREMENTES. GOT => " + measurementDtos.length + " \u001B[0m");
            return CompletableFuture.completedFuture(Arrays.stream(measurementDtos)
                    .parallel()
                    .collect(Collectors.toMap(SynopticMeasurementDto::getCity, synopticMeasurementMapper::maptToSynopticMeasurement)));
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't find any synoptic measurement because of REST API error-> " + e.getMessage());
        }
    }

    @Async
    @Override
    public CompletableFuture<Map<Integer, AirMeasurement>> airMeasurementsProcessor(List<MeasuringStation> measuringStationList) throws RestClientException {
        String url = MeasuringStationApiSupplier.MEASURING_STATION_API_URL_BY_ID;
        LOGGER.info("\u001B[34mLOOOKING FOR AIR MEASUREMENTS-> \u001B[0m");
        try {
            Map<Integer, AirMeasurement> airMap = new HashMap<>();
            measuringStationList
                    .parallelStream()
                    .forEach(a -> {
                        int stationId = a.getStationId();
                        AirMeasurementDto obj = restTemplate.getForObject(url + stationId, AirMeasurementDto.class);
                        airMap.put(stationId, airMeasurementMapper.mapToAirMeasurements(obj));
                    });
            return CompletableFuture.completedFuture(Optional.ofNullable(airMap).orElse(new HashMap<>()));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("Can't find any measurement because of REST API error-> " + e.getMessage());
        }
    }

    @Async
    @Override
    public CompletableFuture<AirMeasurement> airMeasurementProcessorById(int stationId) throws RestClientException {
        String url = MeasuringStationApiSupplier.MEASURING_STATION_API_URL_BY_ID;
        LOGGER.info("\u001B[34mLOOOKING FOR AIR MEASUREMENTS FOR STATION ID-> " + stationId + "\u001B[0m");
        try {
            AirMeasurementDto airMeasurementDto = restTemplate.getForObject(url + stationId, AirMeasurementDto.class);
            return CompletableFuture.completedFuture(airMeasurementMapper.mapToAirMeasurements(airMeasurementDto));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("Can't find any air measurement for stationID: " + stationId + " because of REST API error-> " + e.getMessage());
        }
    }
}
