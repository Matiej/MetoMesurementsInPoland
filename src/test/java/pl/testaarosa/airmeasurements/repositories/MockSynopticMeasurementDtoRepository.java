package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.dtoApi.SynopticMeasurementDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockSynopticMeasurementDtoRepository {

    public List<SynopticMeasurementDto> mockSynopticDtoRepositories() {
        List<SynopticMeasurementDto> measurementDtoList = new ArrayList<>();

        SynopticMeasurementDto synopticMeasurementDto = new SynopticMeasurementDto();
        synopticMeasurementDto.setId(1);
        synopticMeasurementDto.setCity("Warszawa");
        synopticMeasurementDto.setTemperature(6);
        synopticMeasurementDto.setWindSpeed(35);
        synopticMeasurementDto.setAirHumidity(77);
        synopticMeasurementDto.setPressure(999);

        SynopticMeasurementDto synopticMeasurementDto1 = new SynopticMeasurementDto();
        synopticMeasurementDto1.setId(1);
        synopticMeasurementDto1.setCity("Warszawa");
        synopticMeasurementDto1.setTemperature(6);
        synopticMeasurementDto1.setWindSpeed(35);
        synopticMeasurementDto1.setAirHumidity(77);
        synopticMeasurementDto1.setPressure(999);

        SynopticMeasurementDto synopticMeasurementDto2 = new SynopticMeasurementDto();
        synopticMeasurementDto2.setId(2);
        synopticMeasurementDto2.setCity("Poznan");
        synopticMeasurementDto2.setTemperature(12);
        synopticMeasurementDto2.setWindSpeed(35);
        synopticMeasurementDto2.setAirHumidity(66);
        synopticMeasurementDto2.setPressure(999);

        SynopticMeasurementDto synopticMeasurementDto3 = new SynopticMeasurementDto();
        synopticMeasurementDto3.setId(3);
        synopticMeasurementDto3.setCity("Krakow");
        synopticMeasurementDto3.setTemperature(15);
        synopticMeasurementDto3.setWindSpeed(35);
        synopticMeasurementDto3.setAirHumidity(66);
        synopticMeasurementDto3.setPressure(999);

        measurementDtoList.add(synopticMeasurementDto);
        measurementDtoList.add(synopticMeasurementDto1);
        measurementDtoList.add(synopticMeasurementDto2);
        measurementDtoList.add(synopticMeasurementDto3);
        return measurementDtoList;
    }

    public Map<String, SynopticMeasurementDto> mockSynopticMeasurementsMap(){
        Map<String, SynopticMeasurementDto>     map = new HashMap<>();
        for(int j = 0; j<mockSynopticDtoRepositories().size(); j++){
            SynopticMeasurementDto dto = mockSynopticDtoRepositories().get(j);
            map.put(dto.getCity(), dto);
        }
        return map;
    }
}
