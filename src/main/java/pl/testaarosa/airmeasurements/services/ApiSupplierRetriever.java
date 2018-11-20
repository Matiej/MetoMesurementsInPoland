package pl.testaarosa.airmeasurements.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.testaarosa.airmeasurements.domain.measurementsdto.AirMeasurementsDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.MeasuringStationDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.SynopticMeasurementDto;
import pl.testaarosa.airmeasurements.repositories.MeasuringStationRepository;
import pl.testaarosa.airmeasurements.supplier.MeasuringStationApiSupplier;
import pl.testaarosa.airmeasurements.supplier.SynopticStationApiSupplier;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
class ApiSupplierRetriever {

    @Autowired
    private MeasuringStationRepository measuringStationRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiSupplierRetriever.class);

    private RestTemplate restTemplate = new RestTemplate();

    //TODO zabezpiecznia na wypadek braku danych ze strony
    @Async
    public CompletableFuture<List<MeasuringStationDto>> measuringStationApiProcessor() {

        LOGGER.info("\u001B[33mLOOOKIG FOR MEASURING STATIONS-> \u001B[0m");

        String url = MeasuringStationApiSupplier.allMeasuringStationsApi;
        ResponseEntity<MeasuringStationDto[]> responseEntity = restTemplate
                .getForEntity(url, MeasuringStationDto[].class);
        return CompletableFuture.completedFuture(Arrays.stream(responseEntity.getBody()).collect(Collectors.toList()));
    }

    @Async
    public CompletableFuture<Map<String, SynopticMeasurementDto>> synopticMeasurementProcessor() {

        LOGGER.info("\u001B[32mLOOOKING FOR SYNOPTIC MEASUREMENTES-> \u001B[0m");

        String url = SynopticStationApiSupplier.ALL_SYNOPTIC_STATIONS_DATA;
        ResponseEntity<SynopticMeasurementDto[]> responseEntity = restTemplate.getForEntity(url,
                SynopticMeasurementDto[].class);
        return CompletableFuture.completedFuture(Arrays.stream(responseEntity.getBody())
                .collect(Collectors.toMap(SynopticMeasurementDto::getCity, v -> v)));
    }

    @Async
    public CompletableFuture<Map<Integer, AirMeasurementsDto>> airMeasurementsProcessor() throws ExecutionException, InterruptedException {
        String url = MeasuringStationApiSupplier.measurementsAdi;

        LOGGER.info("\u001B[34mLOOOKING FOR AIR MEASUREMENTS-> \u001B[0m");
        try {
            Map<Integer, AirMeasurementsDto> airMap = new HashMap<>();

            for (MeasuringStationDto measuringStationDto : measuringStationApiProcessor().get()) {
                int stationId = measuringStationDto.getId();
                AirMeasurementsDto obj = restTemplate.getForObject(url + stationId, AirMeasurementsDto.class);
                airMap.put(stationId, obj);
            }
            return CompletableFuture.completedFuture(Optional.ofNullable(airMap).orElse(new HashMap<>()));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(new HashMap<>());
        }
    }

    @Async
    public CompletableFuture<AirMeasurementsDto> airMeasurementsProcessorNew(int stationId) throws ExecutionException, InterruptedException {
        String url = MeasuringStationApiSupplier.measurementsAdi;
        AirMeasurementsDto airMeasurementsDto = new AirMeasurementsDto();
        LOGGER.info("\u001B[34mLOOOKING FOR AIR MEASUREMENTS FOR STATION ID-> " + stationId + "\u001B[0m");
        try {
            airMeasurementsDto = restTemplate.getForObject(url + stationId, AirMeasurementsDto.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return CompletableFuture.completedFuture(airMeasurementsDto);
    }
}
