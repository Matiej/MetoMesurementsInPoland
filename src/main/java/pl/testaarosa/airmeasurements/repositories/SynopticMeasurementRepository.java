package pl.testaarosa.airmeasurements.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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

    //native because of limit one measurement need limit. Doesn't want to take all measurements. suppose low efficiency
    //TODO warning. Check how it is work in postgre db on linux vps
    @Query(value = "SELECT * FROM synoptic_measurement s WHERE s.measurement_date BETWEEN :measurementDate AND :measurementDatePlus " +
            "ORDER BY s.temperature DESC, s.AIR_HUMIDITY ASC, s.WIND_SPEED ASC LIMIT 1", nativeQuery = true)
    SynopticMeasurement findHottestPlacesByDate(@Param("measurementDate")LocalDateTime measurementDate,
                                                @Param("measurementDatePlus")LocalDateTime measurementDatePlus);

    //TODO warning. Check how it is work in postgre db on linux vps
    @Query(value = "SELECT * FROM synoptic_measurement s WHERE s.measurement_date BETWEEN :measurementDate AND :measurementDatePlus " +
            "ORDER BY s.temperature ASC, s.AIR_HUMIDITY DESC, s.WIND_SPEED DESC LIMIT 1", nativeQuery = true)
    SynopticMeasurement findColestPlacesByDate(@Param("measurementDate")LocalDateTime measurementDate,
                                                @Param("measurementDatePlus")LocalDateTime measurementDatePlus);

}
