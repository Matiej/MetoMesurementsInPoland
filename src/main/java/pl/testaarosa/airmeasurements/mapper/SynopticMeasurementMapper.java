package pl.testaarosa.airmeasurements.mapper;

import org.springframework.stereotype.Component;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.domain.dtoApi.SynopticMeasurementDto;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class SynopticMeasurementMapper {
//TODO porządek z tym emptyObj w pizdu.
    public SynopticMeasurement maptToSynopticMeasurement(SynopticMeasurementDto synDto) {
        SynopticMeasurementDto synMeasurements = Optional.ofNullable(synDto).orElse(emptyObj());
        LocalDateTime currentDate = LocalDateTime.now().withNano(0);
        return new SynopticMeasurement.SynopticMeasurementsBuilder()
                .foreignId(synMeasurements.getId())
                .city(synMeasurements.getCity())
                .saveDate(currentDate)
                .temperature(synMeasurements.getTemperature())
                .windSpeed(synMeasurements.getWindSpeed())
                .airHumidity(synMeasurements.getAirHumidity())
                .pressure(synMeasurements.getPressure())
                .measurementDate(synDto.getMeasurementDate())
                .measurementHour(synDto.getGetMeasurementHour())
                .build();
    }

    private SynopticMeasurementDto emptyObj() {
        return new SynopticMeasurementDto(9999, "->>no data available<<-", 9999.0, 9999.0, 9999.0, 9999.0);
    }
}
