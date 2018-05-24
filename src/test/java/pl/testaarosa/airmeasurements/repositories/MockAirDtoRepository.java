package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.measurementsdto.AirMeasurementsDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.LevelDto;

public class MockAirDtoRepository {

    public AirMeasurementsDto airMeasurementsDto() {
        LevelDto stIndexLevel = new LevelDto.Builder()
                .indexLevelName("stILevel")
                .id(1)
                .build();

        LevelDto so2IndexLevel = new LevelDto.Builder()
                .indexLevelName("so2LEVEL")
                .id(0)
                .build();

        LevelDto no2IndexLevel = new LevelDto.Builder()
                .indexLevelName("noLEVEL")
                .id(0)
                .build();

        LevelDto coIndexLevel = new LevelDto.Builder()
                .indexLevelName("coLEVEL")
                .id(0)
                .build();

        LevelDto pm10IndexLevel = new LevelDto.Builder()
                .indexLevelName("pm10LEVEL")
                .id(0)
                .build();

        LevelDto pm25IndexLevel = new LevelDto.Builder()
                .indexLevelName("pm25LEVEL")
                .id(0)
                .build();

        LevelDto o3IndexLevel = new LevelDto.Builder()
                .indexLevelName("o3LEVEL")
                .id(0)
                .build();

        LevelDto c6h6IndexLevel = new LevelDto.Builder()
                .indexLevelName("c6hLEVEL")
                .id(0)
                .build();

        AirMeasurementsDto airMeasurementsDto1 = new AirMeasurementsDto();
        airMeasurementsDto1.setStIndexLevel(stIndexLevel);
        airMeasurementsDto1.setSo2IndexLevel(so2IndexLevel);
        airMeasurementsDto1.setNo2IndexLevel(no2IndexLevel);
        airMeasurementsDto1.setCoIndexLevel(coIndexLevel);
        airMeasurementsDto1.setPm10IndexLevel(pm10IndexLevel);
        airMeasurementsDto1.setPm25IndexLevel(pm25IndexLevel);
        airMeasurementsDto1.setO3IndexLevel(o3IndexLevel);
        airMeasurementsDto1.setC6h6IndexLevel(c6h6IndexLevel);
        airMeasurementsDto1.setStCalcDate("2018-05-12 12:05:01:05");
        airMeasurementsDto1.setSo2SourceDataDate("2018-05-05 12:01:05");
        airMeasurementsDto1.setStCalcDate("2018-05-05 12:01:05");
        airMeasurementsDto1.setId(1);
        return airMeasurementsDto1;
    }

    public AirMeasurementsDto airMeasurementsDto1() {
        LevelDto stIndexLevel = new LevelDto.Builder()
                .indexLevelName("stILevel")
                .id(1)
                .build();

        LevelDto so2IndexLevel = new LevelDto.Builder()
                .indexLevelName("so2LEVEL")
                .id(0)
                .build();

        LevelDto no2IndexLevel = new LevelDto.Builder()
                .indexLevelName("noLEVEL")
                .id(0)
                .build();

        LevelDto coIndexLevel = new LevelDto.Builder()
                .indexLevelName("coLEVEL")
                .id(0)
                .build();

        LevelDto pm10IndexLevel = new LevelDto.Builder()
                .indexLevelName("pm10LEVEL")
                .id(0)
                .build();

        LevelDto pm25IndexLevel = new LevelDto.Builder()
                .indexLevelName("pm25LEVEL")
                .id(0)
                .build();

        LevelDto o3IndexLevel = new LevelDto.Builder()
                .indexLevelName("o3LEVEL")
                .id(0)
                .build();

        LevelDto c6h6IndexLevel = new LevelDto.Builder()
                .indexLevelName("c6hLEVEL")
                .id(0)
                .build();

        AirMeasurementsDto airMeasurementsDto = new AirMeasurementsDto();
        airMeasurementsDto.setStIndexLevel(stIndexLevel);
        airMeasurementsDto.setSo2IndexLevel(so2IndexLevel);
        airMeasurementsDto.setNo2IndexLevel(no2IndexLevel);
        airMeasurementsDto.setCoIndexLevel(coIndexLevel);
        airMeasurementsDto.setPm10IndexLevel(pm10IndexLevel);
        airMeasurementsDto.setPm25IndexLevel(pm25IndexLevel);
        airMeasurementsDto.setO3IndexLevel(o3IndexLevel);
        airMeasurementsDto.setC6h6IndexLevel(c6h6IndexLevel);
        airMeasurementsDto.setStCalcDate("2018-05-12 12:05:01:05");
        airMeasurementsDto.setSo2SourceDataDate("2018-05-05 12:01:05");
        airMeasurementsDto.setStCalcDate("2018-05-05 12:01:05");
        airMeasurementsDto.setId(1);
        return airMeasurementsDto;
    }
}
