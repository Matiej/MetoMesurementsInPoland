package pl.testaarosa.airmeasurements.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.testaarosa.airmeasurements.domain.measurementsdto.AirMeasurementsDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.MeasuringStationDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.SynopticMeasurementDto;
import pl.testaarosa.airmeasurements.supplier.MeasuringStationApiSupplier;
import pl.testaarosa.airmeasurements.supplier.SynopticStationApiSupplier;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
class ApiSupplierRetriever {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiSupplierRetriever.class);

    private RestTemplate restTemplate = new RestTemplate();
//TODO zabezpiecznia na wypadek braku danych ze strony
    @Async
    public CompletableFuture<List<MeasuringStationDto>> measuringStationApiProcessor() {

        LOGGER.info("LOOOKIG FOR MEASURING STATIONS-> ");

        String url = MeasuringStationApiSupplier.allMeasuringStationsApi;
        ResponseEntity<MeasuringStationDto[]> responseEntity = restTemplate
                .getForEntity(url, MeasuringStationDto[].class);
        synopticMeasurementProcessor();
        return CompletableFuture.completedFuture(Arrays.stream(responseEntity.getBody()).collect(Collectors.toList()));
    }

    @Async
    public CompletableFuture<Map<String, SynopticMeasurementDto>> synopticMeasurementProcessor() {

        LOGGER.info("LOOOKING FOR SYNOPTIC MEASUREMENTES-> ");

        String url = SynopticStationApiSupplier.allSynopticStationsData;
        ResponseEntity<SynopticMeasurementDto[]> responseEntity = restTemplate.getForEntity(url,
                SynopticMeasurementDto[].class);
        return CompletableFuture.completedFuture(Arrays.stream(responseEntity.getBody())
                .collect(Collectors.toMap(SynopticMeasurementDto::getCity, v -> v)));
    }

    @Async
    public CompletableFuture<Map<Integer, AirMeasurementsDto>> airMeasurementsProcessor() throws ExecutionException, InterruptedException {
        String url = MeasuringStationApiSupplier.measurementsAdi;

        LOGGER.info("LOOOKING FOR AIR MEASUREMENTS-> " );

        Map<Integer, AirMeasurementsDto> airMap = new HashMap<>();

        for (MeasuringStationDto measuringStationDto : measuringStationApiProcessor().get()) {
            int stationId = measuringStationDto.getId();
            AirMeasurementsDto obj = restTemplate.getForObject(url + stationId, AirMeasurementsDto.class);
            airMap.put(stationId, obj);
        }
        return CompletableFuture.completedFuture(airMap);
    }
}
