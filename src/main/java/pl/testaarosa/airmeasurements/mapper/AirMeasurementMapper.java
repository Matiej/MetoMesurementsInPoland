package pl.testaarosa.airmeasurements.mapper;

import org.springframework.stereotype.Component;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.AirMeasurementLevel;
import pl.testaarosa.airmeasurements.domain.dtoApi.AirMeasurementDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Component
public class AirMeasurementMapper {

    public AirMeasurement mapToAirMeasurements(AirMeasurementDto airDto) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime currentDate = LocalDateTime.now().withNano(0);
        LocalDateTime toSql;
        toSql = LocalDateTime.parse(airDto.getStCalcDate(), formatter);
        return new AirMeasurement.AirMaesurementsBuilder()
                .foreignId(airDto.getId())
                .measurementDate(toSql)
                .saveDate(currentDate)
                .airQuality(airQuality(airDto))
                .stIndexLevel(airDto.getStIndexLevel().getIndexLevelName())
                .so2IndexLevel(airDto.getSo2IndexLevel().getIndexLevelName())
                .no2IndexLevel(airDto.getNo2IndexLevel().getIndexLevelName())
                .coIndexLevel(airDto.getCoIndexLevel().getIndexLevelName())
                .pm10IndexLevel(airDto.getPm10IndexLevel()
                        .getIndexLevelName())
                .pm25IndexLevel(airDto.getPm25IndexLevel()
                        .getIndexLevelName())
                .o3IndexLevel(airDto.getO3IndexLevel().getIndexLevelName())
                .c6h6IndexLevel(airDto.getC6h6IndexLevel()
                        .getIndexLevelName())
                .build();
    }

    private AirMeasurementLevel airQuality(AirMeasurementDto air) {
        int l1 = air.getC6h6IndexLevel().getId();
        int l2 = air.getStIndexLevel().getId();
        int l3 = air.getNo2IndexLevel().getId();
        int l4 = air.getCoIndexLevel().getId();
        int l5 = air.getPm10IndexLevel().getId();
        int l6 = air.getPm25IndexLevel().getId();
        List<Integer> airLevels = Arrays.asList(l1, l2, l3, l4, l5, l6);
        int level = airLevels.stream().filter(t-> t<6).mapToInt(l -> l).max().orElse(6);

        switch (level) {
            case 0:
                return AirMeasurementLevel.VERY_GOOD;
            case 1:
                return AirMeasurementLevel.GOOD;
            case 2:
                return AirMeasurementLevel.MODERATE;
            case 3:
                return AirMeasurementLevel.SUFFICIENT;
            case 4:
                return AirMeasurementLevel.BAD;
            case 5:
                return AirMeasurementLevel.VERY_BAD;
            default:
                return AirMeasurementLevel.NO_DATA;
        }
    }

}
