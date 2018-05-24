package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.measurementsdto.SynopticMeasurementDto;

import java.util.ArrayList;
import java.util.List;

public class MockSynopticDtoRepository {

    public List<SynopticMeasurementDto> mockSynopticDtoRepositories() {
        List<SynopticMeasurementDto> measurementDtoList = new ArrayList<>();

        SynopticMeasurementDto synopticMeasurementDto = new SynopticMeasurementDto();
        synopticMeasurementDto.setId(1);
        synopticMeasurementDto.setCity("City");
        synopticMeasurementDto.setTemperature(11);
        synopticMeasurementDto.setWindSpeed(50);
        synopticMeasurementDto.setAirHumidity(77);
        synopticMeasurementDto.setPressure(1001);

        SynopticMeasurementDto synopticMeasurementDto1 = new SynopticMeasurementDto();
        synopticMeasurementDto1.setId(1);
        synopticMeasurementDto1.setCity("City");
        synopticMeasurementDto1.setTemperature(11);
        synopticMeasurementDto1.setWindSpeed(50);
        synopticMeasurementDto1.setAirHumidity(77);
        synopticMeasurementDto1.setPressure(1001);

        SynopticMeasurementDto synopticMeasurementDto2 = new SynopticMeasurementDto();
        synopticMeasurementDto2.setId(1);
        synopticMeasurementDto2.setCity("City");
        synopticMeasurementDto2.setTemperature(2);
        synopticMeasurementDto2.setWindSpeed(77);
        synopticMeasurementDto2.setAirHumidity(88);
        synopticMeasurementDto2.setPressure(1001);

        SynopticMeasurementDto synopticMeasurementDto3 = new SynopticMeasurementDto();
        synopticMeasurementDto3.setId(4);
        synopticMeasurementDto3.setCity("City");
        synopticMeasurementDto3.setTemperature(25);
        synopticMeasurementDto3.setWindSpeed(50);
        synopticMeasurementDto3.setAirHumidity(77);
        synopticMeasurementDto3.setPressure(1001);

        measurementDtoList.add(synopticMeasurementDto);
        measurementDtoList.add(synopticMeasurementDto1);
        measurementDtoList.add(synopticMeasurementDto2);
        measurementDtoList.add(synopticMeasurementDto3);

        return measurementDtoList;
    }
}
