package pl.testaarosa.airmeasurements.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.testaarosa.airmeasurements.domain.MeasuringStationDetails;

public interface MeasuringStationDetailsRepository extends CrudRepository<MeasuringStationDetails, Long> {
}
