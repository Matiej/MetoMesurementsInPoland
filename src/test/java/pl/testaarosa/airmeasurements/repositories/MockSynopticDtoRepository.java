package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.measurementsdto.SynopticMeasurementDto;

public class MockSynopticDtoRepository {

    public SynopticMeasurementDto synoptic1(){
        SynopticMeasurementDto synopticMeasurementDto = new SynopticMeasurementDto();
        synopticMeasurementDto.setId(1);
        synopticMeasurementDto.setCity("City");
        synopticMeasurementDto.setTemperature(11);
        synopticMeasurementDto.setWindSpeed(50);
        synopticMeasurementDto.setAirHumidity(77);
        synopticMeasurementDto.setPressure(1001);
        return synopticMeasurementDto;
    }

    public SynopticMeasurementDto synoptic2(){
        SynopticMeasurementDto synopticMeasurementDto = new SynopticMeasurementDto();
        synopticMeasurementDto.setId(1);
        synopticMeasurementDto.setCity("City");
        synopticMeasurementDto.setTemperature(11);
        synopticMeasurementDto.setWindSpeed(50);
        synopticMeasurementDto.setAirHumidity(77);
        synopticMeasurementDto.setPressure(1001);
        return synopticMeasurementDto;
    }
}
