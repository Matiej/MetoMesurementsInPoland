package pl.testaarosa.airmeasurements.services;

import org.springframework.dao.DataIntegrityViolationException;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.AirMeasurementLevel;

import java.time.DateTimeException;
import java.util.List;
import java.util.NoSuchElementException;

public interface StoredAirMeasurementsService {

    List<AirMeasurement> getAirMeasurementsByLevel(AirMeasurementLevel airMeasurementLevel) throws IllegalArgumentException,
            NoSuchElementException, DataIntegrityViolationException;

    List<AirMeasurement> getAirMeasurementsByDate(String date) throws NoSuchElementException, DateTimeException, DataIntegrityViolationException;
}
