package pl.testaarosa.airmeasurements.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.AirMeasurementLevel;

import javax.persistence.OneToMany;
import javax.transaction.Transactional;
import java.util.List;

public interface AirMeasurementRepository extends CrudRepository<AirMeasurement, Long> {

    @Override
    List<AirMeasurement> findAll();

    List<AirMeasurement> findAllByAirQuality(AirMeasurementLevel airMeasurementLevel);
}
