package pl.testaarosa.airmeasurements.mapper;

import org.springframework.stereotype.Component;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurements;
import pl.testaarosa.airmeasurements.domain.measurementsdto.SynopticMeasurementDto;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class SynopticMeasurementMapper {

    public SynopticMeasurements maptToSynopticMeasurement(SynopticMeasurementDto synDto) {
        SynopticMeasurementDto synMeasurements = Optional.ofNullable(synDto).orElse(emptyObj());
        LocalDateTime currentDate = LocalDateTime.now().withNano(0);
        return new SynopticMeasurements.SynopticMeasurementsBuilder()
                .foreignId(synMeasurements.getId())
                .city(synMeasurements.getCity())
                .saveDate(currentDate)
                .temperature(synMeasurements.getTemperature())
                .windSpeed(synMeasurements.getWindSpeed())
                .airHumidity(synMeasurements.getAirHumidity())
                .pressure(synMeasurements.getPressure())
                .build();
    }

    private SynopticMeasurementDto emptyObj() {
        return new SynopticMeasurementDto(9999, "->>no data available<<-", 9999.0, 9999.0, 9999.0, 9999.0);
    }
}
