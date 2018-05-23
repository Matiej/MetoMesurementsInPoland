package pl.testaarosa.airmeasurements.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.testaarosa.airmeasurements.domain.AirMeasurements;
import pl.testaarosa.airmeasurements.domain.MeasurementsAirLevel;

import java.util.List;

public interface AirMeasurementRepository extends CrudRepository<AirMeasurements, Long> {
    List<AirMeasurements> findAll();

    List<AirMeasurements> findAllByAirQuality(MeasurementsAirLevel measurementsAirLevel);
}
