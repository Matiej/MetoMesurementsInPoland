package pl.testaarosa.airmeasurements.services;

import org.springframework.http.ResponseEntity;
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
import java.util.stream.Collectors;

@Service
class ApiSupplierRetriever {
    private RestTemplate restTemplate = new RestTemplate();

    public List<MeasuringStationDto> measuringStationApiProcessor() {
        String url = MeasuringStationApiSupplier.allMeasuringStationsApi;
        ResponseEntity<MeasuringStationDto[]> responseEntity = restTemplate
                .getForEntity(url,MeasuringStationDto[].class);
        synopticMeasurementProcessor();
        return Arrays.stream(responseEntity.getBody()).collect(Collectors.toList());
    }

    public Map<String, SynopticMeasurementDto> synopticMeasurementProcessor() {
        String url = SynopticStationApiSupplier.allSynopticStationsData;
        ResponseEntity<SynopticMeasurementDto[]> responseEntity = restTemplate.getForEntity(url,
                                                                                            SynopticMeasurementDto[].class);
        return Arrays.stream(responseEntity.getBody())
                     .collect(Collectors.toMap(SynopticMeasurementDto::getCity, v -> v));
    }

    public Map<Integer, AirMeasurementsDto> airMeasurementsProcessor() {
        String url = MeasuringStationApiSupplier.measurementsAdi;
        Map<Integer, AirMeasurementsDto> airMap = new HashMap<>();
        for (MeasuringStationDto measuringStationDto : measuringStationApiProcessor()) {
            int stationId = measuringStationDto.getId();
            AirMeasurementsDto obj = restTemplate.getForObject(url + stationId, AirMeasurementsDto.class);
            airMap.put(stationId, obj);
        }
        return airMap;
    }
}
