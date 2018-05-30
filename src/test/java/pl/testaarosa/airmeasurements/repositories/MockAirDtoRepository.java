package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.measurementsdto.AirMeasurementsDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.LevelDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MockAirDtoRepository {

    public List<AirMeasurementsDto> airMeasurementsDtos() {
        List<AirMeasurementsDto> airMeasurementsDtoList = new ArrayList<>();

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

        AirMeasurementsDto airMeasurementsDto2 = new AirMeasurementsDto();
        airMeasurementsDto2.setStIndexLevel(stIndexLevel);
        airMeasurementsDto2.setSo2IndexLevel(so2IndexLevel);
        airMeasurementsDto2.setNo2IndexLevel(no2IndexLevel);
        airMeasurementsDto2.setCoIndexLevel(coIndexLevel);
        airMeasurementsDto2.setPm10IndexLevel(pm10IndexLevel);
        airMeasurementsDto2.setPm25IndexLevel(pm25IndexLevel);
        airMeasurementsDto2.setO3IndexLevel(o3IndexLevel);
        airMeasurementsDto2.setC6h6IndexLevel(c6h6IndexLevel);
        airMeasurementsDto2.setStCalcDate("2018-05-12 12:05:01:05");
        airMeasurementsDto2.setSo2SourceDataDate("2018-05-05 12:01:05");
        airMeasurementsDto2.setStCalcDate("2018-05-05 12:01:05");
        airMeasurementsDto2.setId(2);

        AirMeasurementsDto airMeasurementsDto3 = new AirMeasurementsDto();
        airMeasurementsDto3.setStIndexLevel(stIndexLevel);
        airMeasurementsDto3.setSo2IndexLevel(so2IndexLevel);
        airMeasurementsDto3.setNo2IndexLevel(no2IndexLevel);
        airMeasurementsDto3.setCoIndexLevel(coIndexLevel);
        airMeasurementsDto3.setPm10IndexLevel(pm10IndexLevel);
        airMeasurementsDto3.setPm25IndexLevel(pm25IndexLevel);
        airMeasurementsDto3.setO3IndexLevel(o3IndexLevel);
        airMeasurementsDto3.setC6h6IndexLevel(c6h6IndexLevel);
        airMeasurementsDto3.setStCalcDate("2018-05-12 12:05:01:05");
        airMeasurementsDto3.setSo2SourceDataDate("2018-05-05 12:01:05");
        airMeasurementsDto3.setStCalcDate("2018-05-05 12:01:05");
        airMeasurementsDto3.setId(3);

        airMeasurementsDtoList.add(airMeasurementsDto);
        airMeasurementsDtoList.add(airMeasurementsDto1);
        airMeasurementsDtoList.add(airMeasurementsDto2);
        airMeasurementsDtoList.add(airMeasurementsDto3);
        return airMeasurementsDtoList;
    }

    public Map<Integer, AirMeasurementsDto> measurementsDtoMap(){
        Map<Integer, AirMeasurementsDto> map = new TreeMap<>();
        int i =0;
        for (AirMeasurementsDto airMeasurementsDto : airMeasurementsDtos()) {
            map.put(i, airMeasurementsDto);
            i++;
        }
        return map;
    }
}
