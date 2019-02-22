package pl.testaarosa.airmeasurements.repositories;

import org.apache.poi.hssf.util.HSSFColor;
import pl.testaarosa.airmeasurements.domain.dtoApi.AirMeasurementDto;
import pl.testaarosa.airmeasurements.domain.dtoApi.LevelDto;

import java.util.ArrayList;
import java.util.List;

public class MockAirMeasurementDtoRepository {

    private final String DATE1 = "2018-05-05 12:01:05";

    public List<AirMeasurementDto> airMeasurementsDtos() {
        List<AirMeasurementDto> airMeasurementDtoList = new ArrayList<>();

        LevelDto stIndexLevel = new LevelDto.Builder()
                .indexLevelName("Bardzo dobry")
                .id(0)
                .build();

        LevelDto so2IndexLevel = new LevelDto.Builder()
                .indexLevelName("Dobry")
                .id(1)
                .build();

        LevelDto no2IndexLevel = new LevelDto.Builder()
                .indexLevelName("Umiarkowany")
                .id(2)
                .build();

        LevelDto coIndexLevel = new LevelDto.Builder()
                .indexLevelName("Dostateczny")
                .id(3)
                .build();

        LevelDto pm10IndexLevel = new LevelDto.Builder()
                .indexLevelName("Zły")
                .id(4)
                .build();

        LevelDto pm25IndexLevel = new LevelDto.Builder()
                .indexLevelName("Bardzo zły")
                .id(5)
                .build();

        LevelDto o3IndexLevel = new LevelDto.Builder()
                .indexLevelName("Bardzo dobry")
                .id(0)
                .build();

        LevelDto c6h6IndexLevel = new LevelDto.Builder()
                .indexLevelName("Dobry")
                .id(1)
                .build();

        AirMeasurementDto airMeasurementDto1 = new AirMeasurementDto();
        airMeasurementDto1.setStIndexLevel(stIndexLevel);
        airMeasurementDto1.setSo2IndexLevel(so2IndexLevel);
        airMeasurementDto1.setNo2IndexLevel(no2IndexLevel);
        airMeasurementDto1.setCoIndexLevel(coIndexLevel);
        airMeasurementDto1.setPm10IndexLevel(pm10IndexLevel);
        airMeasurementDto1.setPm25IndexLevel(pm25IndexLevel);
        airMeasurementDto1.setO3IndexLevel(o3IndexLevel);
        airMeasurementDto1.setC6h6IndexLevel(c6h6IndexLevel);
        airMeasurementDto1.setStCalcDate(DATE1);
        airMeasurementDto1.setSo2SourceDataDate(DATE1);
        airMeasurementDto1.setStCalcDate(DATE1);
        airMeasurementDto1.setId(1);

        AirMeasurementDto airMeasurementDto = new AirMeasurementDto();
        airMeasurementDto.setStIndexLevel(stIndexLevel);
        airMeasurementDto.setSo2IndexLevel(so2IndexLevel);
        airMeasurementDto.setNo2IndexLevel(no2IndexLevel);
        airMeasurementDto.setCoIndexLevel(coIndexLevel);
        airMeasurementDto.setPm10IndexLevel(pm10IndexLevel);
        airMeasurementDto.setPm25IndexLevel(pm25IndexLevel);
        airMeasurementDto.setO3IndexLevel(o3IndexLevel);
        airMeasurementDto.setC6h6IndexLevel(c6h6IndexLevel);
        airMeasurementDto.setStCalcDate(DATE1);
        airMeasurementDto.setSo2SourceDataDate(DATE1);
        airMeasurementDto.setStCalcDate(DATE1);
        airMeasurementDto.setId(1);

        AirMeasurementDto airMeasurementDto2 = new AirMeasurementDto();
        airMeasurementDto2.setStIndexLevel(stIndexLevel);
        airMeasurementDto2.setSo2IndexLevel(so2IndexLevel);
        airMeasurementDto2.setNo2IndexLevel(no2IndexLevel);
        airMeasurementDto2.setCoIndexLevel(coIndexLevel);
        airMeasurementDto2.setPm10IndexLevel(pm10IndexLevel);
        airMeasurementDto2.setPm25IndexLevel(pm25IndexLevel);
        airMeasurementDto2.setO3IndexLevel(o3IndexLevel);
        airMeasurementDto2.setC6h6IndexLevel(c6h6IndexLevel);
        airMeasurementDto2.setStCalcDate(DATE1);
        airMeasurementDto2.setSo2SourceDataDate(DATE1);
        airMeasurementDto2.setStCalcDate(DATE1);
        airMeasurementDto2.setId(2);

        AirMeasurementDto airMeasurementDto3 = new AirMeasurementDto();
        airMeasurementDto3.setStIndexLevel(stIndexLevel);
        airMeasurementDto3.setSo2IndexLevel(so2IndexLevel);
        airMeasurementDto3.setNo2IndexLevel(no2IndexLevel);
        airMeasurementDto3.setCoIndexLevel(coIndexLevel);
        airMeasurementDto3.setPm10IndexLevel(pm10IndexLevel);
        airMeasurementDto3.setPm25IndexLevel(pm25IndexLevel);
        airMeasurementDto3.setO3IndexLevel(o3IndexLevel);
        airMeasurementDto3.setC6h6IndexLevel(c6h6IndexLevel);
        airMeasurementDto3.setStCalcDate(DATE1);
        airMeasurementDto3.setSo2SourceDataDate(DATE1);
        airMeasurementDto3.setStCalcDate(DATE1);
        airMeasurementDto3.setId(3);

        airMeasurementDtoList.add(airMeasurementDto);
        airMeasurementDtoList.add(airMeasurementDto1);
        airMeasurementDtoList.add(airMeasurementDto2);
        airMeasurementDtoList.add(airMeasurementDto3);
        return airMeasurementDtoList;
    }
}
