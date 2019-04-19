package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.AirMeasurementLevel;

import java.time.LocalDateTime;
import java.util.List;

public interface AirMeasurementDao {

    AirMeasurement save(AirMeasurement airMeasurement);

    List<AirMeasurement> findAllAirMeasurementsByAirQuality(AirMeasurementLevel airMeasurementLevel);

    List<AirMeasurement> getAirMeasurementsBySaveDate(LocalDateTime saveDate);

}
