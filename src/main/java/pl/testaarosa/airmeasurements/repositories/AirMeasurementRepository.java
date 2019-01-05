package pl.testaarosa.airmeasurements.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.AirMeasurementLevel;

import java.util.List;

public interface AirMeasurementRepository extends CrudRepository<AirMeasurement, Long> {
    List<AirMeasurement> findAll();

    List<AirMeasurement> findAllByAirQuality(AirMeasurementLevel airMeasurementLevel);
}
