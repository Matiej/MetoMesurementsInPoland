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
import pl.testaarosa.airmeasurements.domain.dtoApi.AirMeasurementDto;
import pl.testaarosa.airmeasurements.domain.dtoApi.MeasuringStationDto;
import pl.testaarosa.airmeasurements.domain.dtoApi.SynopticMeasurementDto;
import pl.testaarosa.airmeasurements.mapper.AirMeasurementMapper;
import pl.testaarosa.airmeasurements.mapper.MeasuringStationMapper;
import pl.testaarosa.airmeasurements.mapper.SynopticMeasurementMapper;
import pl.testaarosa.airmeasurements.supplier.MeasurementsApiSupplier;

import java.net.URI;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static pl.testaarosa.airmeasurements.services.ConsolerData.*;

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
    public Map<MeasuringStation, AirMeasurement> airMeasurementsAndStProcessor(Integer stationID) throws RestClientException,
            NoSuchElementException {
        LOGGER.info(ANSI_YELLOW + "LOOOKING FOR MEASURING STATIONS" + ANSI_RESET);
        LinkedHashMap<MeasuringStation, AirMeasurement> measurementMap = new LinkedHashMap<>();
        try {
            ResponseEntity<MeasuringStationDto[]> responseEntity = restTemplate
                    .getForEntity(msApi.giosApiSupplierAll(), MeasuringStationDto[].class);
            HttpStatus statusCode = responseEntity.getStatusCode();
            if(!statusCode.is2xxSuccessful()) {
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
                        measurementMap.put(st, airMeasurementProcessorById(stationID));
                    } else if (!mStList.stream().anyMatch(s -> s.getStationId() == stationID)) {
                        throw new NoSuchElementException(ANSI_RED + "Can't find station id: " + stationID + " in data base!" + ANSI_RESET);
                    }
                } else {
                    measurementMap.put(st, airMeasurementProcessorById(st.getStationId()));
                }
            });
        } catch (ResourceAccessException e) {
            throw new RestClientException("External Api error. Can't find any measuring station or air measurement because of error-> no connection");
        }
        return measurementMap;
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
            if(!statusCode.is2xxSuccessful()) {
                throw new RestClientException("Can't find any synoptic measurement because of external API error. HTTP Status code => " + statusCode.toString());
            }
            SynopticMeasurementDto[] measurementDtos = responseEntity.getBody();
            LOGGER.info(ANSI_BLUE + "RECEIVED SYNOPTIC MEASUREMENTS. GOT => " + measurementDtos.length  + ANSI_RESET);
            return Arrays.stream(measurementDtos)
                    .collect(Collectors.toMap(SynopticMeasurementDto::getCity, synopticMeasurementMapper::maptToSynopticMeasurement, (o, n) -> n, LinkedHashMap::new));
        } catch (ResourceAccessException e) {
            throw new RestClientException("External Api error. Can't find any synoptic measurement because of error-> no connection");
        }
    }

    private AirMeasurement airMeasurementProcessorById(int stationId) throws RestClientException {
        try {
            ResponseEntity<AirMeasurementDto> responseEntity = restTemplate.getForEntity(msApi.giosApiSupplierIndex(stationId), AirMeasurementDto.class);
            HttpStatus statusCode = responseEntity.getStatusCode();
            if(!statusCode.is2xxSuccessful()) {
                throw new RestClientException("Can't find any air measurement because of external API error. HTTP Status code => " + statusCode.toString());
            }
            AirMeasurementDto airMeasurementDto = responseEntity.getBody();
            LOGGER.info(ANSI_GREEN + "RECEIVED AIR MEASUREMENT FOR STATION => " + stationId + ANSI_RESET);
            return airMeasurementMapper.mapToAirMeasurements(airMeasurementDto);
        } catch (ResourceAccessException e) {
            throw new RestClientException("External Api error. Can't find any air measurement because of error-> no connection");
        }
    }
}
