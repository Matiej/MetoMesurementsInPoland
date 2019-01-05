package pl.testaarosa.airmeasurements.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;

import java.util.List;

public interface SynopticMeasurementRepository extends CrudRepository<SynopticMeasurement, Long> {
    List<SynopticMeasurement> findAll();
}
