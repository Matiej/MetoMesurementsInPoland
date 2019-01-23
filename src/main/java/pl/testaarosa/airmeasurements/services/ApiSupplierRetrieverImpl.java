package pl.testaarosa.airmeasurements.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
import pl.testaarosa.airmeasurements.supplier.MeasurementsApiSupplier;
import pl.testaarosa.airmeasurements.supplier.MeasuringStationApiSupplier;
import pl.testaarosa.airmeasurements.supplier.SynopticStationApiSupplier;

import java.net.URI;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static pl.testaarosa.airmeasurements.services.ConsolerData.ANSI_RESET;
import static pl.testaarosa.airmeasurements.services.ConsolerData.ANSI_YELLOW;

@Service
public class ApiSupplierRetrieverImpl implements ApiSupplierRetriever {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiSupplierRetrieverImpl.class);

    private final RestTemplate restTemplate;
    private final MeasurementsApiSupplier msApi;
    private final MeasuringStationMapper measuringStationMapper;
    private final AirMeasurementMapper airMeasurementMapper;
    private final SynopticMeasurementMapper synopticMeasurementMapper;

    @Autowired
    public ApiSupplierRetrieverImpl(MeasuringStationMapper measuringStationMapper, RestTemplate restTemplate, MeasurementsApiSupplier msApi,
                                    AirMeasurementMapper airMeasurementMapper, SynopticMeasurementMapper synopticMeasurementMapper) {
        this.measuringStationMapper = measuringStationMapper;
        this.airMeasurementMapper = airMeasurementMapper;
        this.synopticMeasurementMapper = synopticMeasurementMapper;
        this.restTemplate = restTemplate;
        this.msApi = msApi;
    }

    //    @Async
    @Override
    public List<MeasuringStation> measuringStationApiProcessor() throws RestClientException {
        try {
            ResponseEntity<MeasuringStationDto[]> responseEntity = restTemplate
                    .getForEntity(msApi.giosApiSupplierAll(), MeasuringStationDto[].class);
            MeasuringStationDto[] measuringStationDtos = responseEntity.getBody();
            LOGGER.info(ANSI_YELLOW + "LOOKING MEASURING STATIONS. GOT => " + measuringStationDtos.length + ANSI_RESET);
            return Arrays.stream(measuringStationDtos)
                    .map(measuringStationMapper::mapToMeasuringSt)
                    .collect(Collectors.toList());
        } catch (RestClientResponseException e) {
            e.printStackTrace();
            throw new RestClientException(" REST API CONNECTION ERROR-> " + e.getMessage());
        }
    }

    //    @Async
    @Override
    public Map<String, SynopticMeasurement> synopticMeasurementProcessor() throws RestClientException {
        try {
            URI uri = msApi.imgwApiSupplierAll();
            ResponseEntity<SynopticMeasurementDto[]> responseEntity = restTemplate.getForEntity(uri,
                    SynopticMeasurementDto[].class);
            SynopticMeasurementDto[] measurementDtos = responseEntity.getBody();
            LOGGER.info("\u001B[32mLOOOKING FOR SYNOPTIC MEASUREMENTES. GOT => " + measurementDtos.length + " \u001B[0m");
            return Arrays.stream(measurementDtos)
                    .collect(Collectors.toMap(SynopticMeasurementDto::getCity, synopticMeasurementMapper::maptToSynopticMeasurement,(o,n)->n, LinkedHashMap::new));
        } catch (RestClientResponseException e) {
            e.printStackTrace();
            throw new RestClientException("Can't find any synoptic measurement because of REST API error-> " + e.getMessage());
        }
    }

    @Override
    public Map<MeasuringStation, AirMeasurement> airMeasurementsAndStProcessor() throws RestClientException {
        LOGGER.info("\u001B[34mLOOOKING FOR AIR MEASUREMENTS-> \u001B[0m");
        List<MeasuringStation> measuringStationLists = measuringStationApiProcessor();
        ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 5);
        try {
            LinkedHashMap<MeasuringStation, AirMeasurement> measurementMap = new LinkedHashMap<>();
            forkJoinPool.submit(() -> measuringStationLists
                    .parallelStream()
                    .forEach(st -> {
                        AirMeasurementDto airMeasurementDto = restTemplate.getForObject(msApi.giosApiSupplierIndex(st.getStationId()), AirMeasurementDto.class);
                        measurementMap.put(st, airMeasurementMapper.mapToAirMeasurements(airMeasurementDto));
                    }));
            return measurementMap;
        } catch (RestClientResponseException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RestClientException("Can't find any measurement because of REST API error-> " + e.getMessage());
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
        LOGGER.info("\u001B[34mLOOOKING FOR AIR MEASUREMENTS FOR STATION ID-> " + stationId + "\u001B[0m");
        try {
            AirMeasurementDto airMeasurementDto = restTemplate.getForObject(msApi.giosApiSupplierIndex(stationId), AirMeasurementDto.class);
            return airMeasurementMapper.mapToAirMeasurements(airMeasurementDto);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("Can't find any air measurement for stationID: " + stationId + " because of REST API error-> " + e.getMessage());
        }
    }
}
