package pl.testaarosa.airmeasurements.services;

import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.AirMeasurementLevel;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;

import java.sql.SQLException;
import java.time.DateTimeException;
import java.util.List;
import java.util.NoSuchElementException;

public interface StoredMeasurementsService {
    List<MeasuringStation> findAll() throws NoSuchElementException, DataIntegrityViolationException;

    List<SynopticMeasurement> getSynopticMeasuremets(String date) throws NoSuchElementException, DateTimeException, DataIntegrityViolationException;

    List<AirMeasurement> getAirMeasurementsByLevel(AirMeasurementLevel airMeasurementLevel) throws IllegalArgumentException,
            NoSuchElementException, DataIntegrityViolationException;

    List<AirMeasurement> getAirMeasurementsByDate(String date) throws NoSuchElementException, DateTimeException, DataIntegrityViolationException;

    List<SynopticMeasurement> getHottestPlaces() throws NoSuchElementException, DataIntegrityViolationException;

    List<SynopticMeasurement> getColdestPlaces() throws NoSuchElementException, DataIntegrityViolationException;

    SynopticMeasurement getHottestPlaceGivenDate(String date) throws DateTimeException, NoSuchElementException, DataIntegrityViolationException;

    SynopticMeasurement getColdestPlaceGivenDate(String date) throws DateTimeException, NoSuchElementException, DataIntegrityViolationException;

}
