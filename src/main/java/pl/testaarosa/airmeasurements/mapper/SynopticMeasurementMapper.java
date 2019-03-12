package pl.testaarosa.airmeasurements.mapper;

import org.springframework.stereotype.Component;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.domain.dtoApi.SynopticMeasurementDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class SynopticMeasurementMapper {
    public SynopticMeasurement maptToSynopticMeasurement(SynopticMeasurementDto synMeasurements) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = synMeasurements.getMeasurementDate()+ " " + synMeasurements.getGetMeasurementHour() + ":00:00";
        LocalDateTime measurementDate = LocalDateTime.parse(date, formatter);
        return new SynopticMeasurement.SynopticMeasurementsBuilder()
                .foreignId(synMeasurements.getId())
                .city(synMeasurements.getCity())
                .saveDate(LocalDateTime.now().withNano(0))
                .temperature(synMeasurements.getTemperature())
                .windSpeed(synMeasurements.getWindSpeed())
                .airHumidity(synMeasurements.getAirHumidity())
                .pressure(synMeasurements.getPressure())
                .measurementDate(measurementDate)
                .build();
    }
}
