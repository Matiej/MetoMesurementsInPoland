package pl.testaarosa.airmeasurements.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    //    @Async
    @Override
    public List<MeasuringStation> measuringStationApiProcessor() throws RestClientException {
        try {
            String url = MeasuringStationApiSupplier.ALL_MEASURING_STATIONS_API_URL;
            ResponseEntity<MeasuringStationDto[]> responseEntity = restTemplate
                    .getForEntity(url, MeasuringStationDto[].class);
            MeasuringStationDto[] measuringStationDtos = responseEntity.getBody();
            LOGGER.info(ANSI_YELLOW + "LOOKING MEASURING STATIONS. GOT => " + measuringStationDtos.length + ANSI_RESET);
            return Arrays.stream(measuringStationDtos)
                    .map(measuringStationMapper::mapToMeasuringSt)
                    .collect(Collectors.toList());
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RuntimeException(" REST API CONNECTION ERROR-> " + e.getMessage());
        }
    }

    //    @Async
    @Override
    public Map<String, SynopticMeasurement> synopticMeasurementProcessor() throws RestClientException {
        try {
            String url = SynopticStationApiSupplier.ALL_SYNOPTIC_STATIONS_API_URL;
            ResponseEntity<SynopticMeasurementDto[]> responseEntity = restTemplate.getForEntity(url,
                    SynopticMeasurementDto[].class);
            SynopticMeasurementDto[] measurementDtos = responseEntity.getBody();
            LOGGER.info("\u001B[32mLOOOKING FOR SYNOPTIC MEASUREMENTES. GOT => " + measurementDtos.length + " \u001B[0m");
            return Arrays.stream(measurementDtos)
                    .collect(Collectors.toMap(SynopticMeasurementDto::getCity, synopticMeasurementMapper::maptToSynopticMeasurement,(o,n)->n, LinkedHashMap::new));
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't find any synoptic measurement because of REST API error-> " + e.getMessage());
        }
    }

    @Override
    public Map<MeasuringStation, AirMeasurement> airMeasurementsAndStProcessor() throws RestClientException {
        String url = MeasuringStationApiSupplier.MEASURING_STATION_API_URL_BY_ID;
        LOGGER.info("\u001B[34mLOOOKING FOR AIR MEASUREMENTS-> \u001B[0m");
        List<MeasuringStation> measuringStationLists = measuringStationApiProcessor();
        ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 5);
        try {
            LinkedHashMap<MeasuringStation, AirMeasurement> measurementMap = new LinkedHashMap<>();
            forkJoinPool.submit(() -> measuringStationLists
                    .parallelStream()
                    .forEach(st -> {
                        AirMeasurementDto airMeasurementDto = restTemplate.getForObject(url + st.getStationId(), AirMeasurementDto.class);
                        measurementMap.put(st, airMeasurementMapper.mapToAirMeasurements(airMeasurementDto));
                    }));
            return measurementMap;
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("Can't find any measurement because of REST API error-> " + e.getMessage());
        } finally {
            forkJoinPool.shutdown();
            try {
                forkJoinPool.awaitTermination(1,TimeUnit.DAYS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //    @Async
    @Override
    public AirMeasurement airMeasurementProcessorById(int stationId) throws RestClientException {
        String url = MeasuringStationApiSupplier.MEASURING_STATION_API_URL_BY_ID;
        LOGGER.info("\u001B[34mLOOOKING FOR AIR MEASUREMENTS FOR STATION ID-> " + stationId + "\u001B[0m");
        try {
            AirMeasurementDto airMeasurementDto = restTemplate.getForObject(url + stationId, AirMeasurementDto.class);
            return airMeasurementMapper.mapToAirMeasurements(airMeasurementDto);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("Can't find any air measurement for stationID: " + stationId + " because of REST API error-> " + e.getMessage());
        }
    }
}
