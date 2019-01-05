package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.dtoApi.SynopticMeasurementDto;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toMap;

public class MockSynopticDtoRepository {

    public List<SynopticMeasurementDto> mockSynopticDtoRepositories() {
        List<SynopticMeasurementDto> measurementDtoList = new ArrayList<>();

        SynopticMeasurementDto synopticMeasurementDto = new SynopticMeasurementDto();
        synopticMeasurementDto.setId(1);
        synopticMeasurementDto.setCity("City1");
        synopticMeasurementDto.setTemperature(11);
        synopticMeasurementDto.setWindSpeed(50);
        synopticMeasurementDto.setAirHumidity(77);
        synopticMeasurementDto.setPressure(1001);

        SynopticMeasurementDto synopticMeasurementDto1 = new SynopticMeasurementDto();
        synopticMeasurementDto1.setId(1);
        synopticMeasurementDto1.setCity("Warszawa");
        synopticMeasurementDto1.setTemperature(11);
        synopticMeasurementDto1.setWindSpeed(50);
        synopticMeasurementDto1.setAirHumidity(77);
        synopticMeasurementDto1.setPressure(1001);

        SynopticMeasurementDto synopticMeasurementDto2 = new SynopticMeasurementDto();
        synopticMeasurementDto2.setId(2);
        synopticMeasurementDto2.setCity("Poznan");
        synopticMeasurementDto2.setTemperature(2);
        synopticMeasurementDto2.setWindSpeed(77);
        synopticMeasurementDto2.setAirHumidity(88);
        synopticMeasurementDto2.setPressure(1001);

        SynopticMeasurementDto synopticMeasurementDto3 = new SynopticMeasurementDto();
        synopticMeasurementDto3.setId(3);
        synopticMeasurementDto3.setCity("Krakow");
        synopticMeasurementDto3.setTemperature(25);
        synopticMeasurementDto3.setWindSpeed(50);
        synopticMeasurementDto3.setAirHumidity(77);
        synopticMeasurementDto3.setPressure(1001);

        SynopticMeasurementDto synopticMeasurementDto4 = new SynopticMeasurementDto();
        synopticMeasurementDto4.setId(1);
        synopticMeasurementDto4.setCity("City1");
        synopticMeasurementDto4.setTemperature(11);
        synopticMeasurementDto4.setWindSpeed(50);
        synopticMeasurementDto4.setAirHumidity(77);
        synopticMeasurementDto4.setPressure(1001);

        measurementDtoList.add(synopticMeasurementDto);
        measurementDtoList.add(synopticMeasurementDto1);
        measurementDtoList.add(synopticMeasurementDto2);
        measurementDtoList.add(synopticMeasurementDto3);
        measurementDtoList.add(synopticMeasurementDto4);

        return measurementDtoList;

    }

    public Map<String, SynopticMeasurementDto> mockSynopticMeasurementsMap(){
        Map<String, SynopticMeasurementDto>     map = new HashMap<>();
        for(int j = 0; j<mockSynopticDtoRepositories().size(); j++){
            SynopticMeasurementDto dto = mockSynopticDtoRepositories().get(j);
            map.put(dto.getCity(), dto);
        }
        return map;
//        return mockSynopticDtoRepositories()
//                .stream()
//                .distinct()
//                .collect(toMap(SynopticMeasurementDto::getCity, c-> c));
    }

    public CompletableFuture<Map<String, SynopticMeasurementDto>> mockSynopticMeasurementsMapCF() {
        return CompletableFuture.completedFuture(Optional.ofNullable(mockSynopticMeasurementsMap()).orElse(new HashMap<>()));
    }
}
