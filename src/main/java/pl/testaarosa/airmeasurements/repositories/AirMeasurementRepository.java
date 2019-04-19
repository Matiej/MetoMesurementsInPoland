package pl.testaarosa.airmeasurements.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.AirMeasurementLevel;

import java.util.List;

public interface AirMeasurementRepository extends JpaRepository<AirMeasurement, Long> {

    @Override
    List<AirMeasurement> findAll();

    List<AirMeasurement> findAllByAirQuality(AirMeasurementLevel airMeasurementLevel);

//    List<AirMeasurement> findAllAirMeasurementsByAirQuality(AirMeasurementLevel airMeasurementLevel);
//
//    List<AirMeasurement> getAirMeasurementsBySaveDate(LocalDateTime saveDate);

}
