package pl.testaarosa.airmeasurements.services;

import org.springframework.dao.DataIntegrityViolationException;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;

import java.time.DateTimeException;
import java.util.List;
import java.util.NoSuchElementException;

public interface StoredSynopticMeasurementService {

    List<SynopticMeasurement> findAll() throws NoSuchElementException, DataIntegrityViolationException;

    List<SynopticMeasurement> getSynopticMeasuremets(String date) throws NoSuchElementException, DateTimeException, DataIntegrityViolationException;

    List<SynopticMeasurement> getHottestPlaces(String noOfResults) throws NoSuchElementException, DataIntegrityViolationException;

    List<SynopticMeasurement> getColdestPlaces(String noOfResults) throws NoSuchElementException, DataIntegrityViolationException;

    SynopticMeasurement getHottestPlaceGivenDate(String date) throws DateTimeException, NoSuchElementException, DataIntegrityViolationException;

    SynopticMeasurement getColdestPlaceGivenDate(String date) throws DateTimeException, NoSuchElementException, DataIntegrityViolationException;
}
