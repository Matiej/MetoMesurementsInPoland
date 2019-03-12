package pl.testaarosa.airmeasurements.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SynopticMeasurementRepository extends CrudRepository<SynopticMeasurement, Long> {

    List<SynopticMeasurement> findAll();

    @Query("SELECT s FROM SynopticMeasurement s WHERE s.measurementDate BETWEEN ?1 AND ?2")
    List<SynopticMeasurement> findSynopticMeasurementsByDate(LocalDateTime measurementDate, LocalDateTime measurementDatePlus);

    @Query("SELECT s FROM SynopticMeasurement s ORDER BY s.temperature DESC, s.airHumidity ASC, s.windSpeed ASC")
    List<SynopticMeasurement> findHottestPlaces(Pageable pageable);

    @Query("SELECT s FROM SynopticMeasurement s ORDER BY s.temperature ASC, s.airHumidity DESC, s.windSpeed DESC")
    List<SynopticMeasurement> findColdestPlaces(Pageable pageable);
}
