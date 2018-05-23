package pl.testaarosa.airmeasurements.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurements;

import java.util.List;

public interface SynopticMeasurementRepository extends CrudRepository<SynopticMeasurements, Long> {
    List<SynopticMeasurements> findAll();
}
