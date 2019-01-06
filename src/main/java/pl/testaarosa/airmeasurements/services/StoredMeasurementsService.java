package pl.testaarosa.airmeasurements.services;

import org.hibernate.HibernateException;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.AirMeasurementLevel;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;

import java.time.DateTimeException;
import java.util.List;
import java.util.NoSuchElementException;

public interface StoredMeasurementsService {
    List<MeasuringStation> findAll() throws NoSuchElementException, HibernateException;

    List<AirMeasurement> getAirMeasurements(AirMeasurementLevel airMeasurementLevel) throws IllegalArgumentException,
            NoSuchElementException, HibernateException;

    List<AirMeasurement> getAirMeasurements(String date) throws NoSuchElementException, DateTimeException, HibernateException;

    List<SynopticMeasurement> getSynopticMeasuremets(String date) throws NoSuchElementException, DateTimeException, HibernateException;

    SynopticMeasurement getHottestPlaceGivenDate(String date) throws DateTimeException, NoSuchElementException, HibernateException;

    SynopticMeasurement getColdestPlaceGivenDate(String date) throws DateTimeException, NoSuchElementException, HibernateException;

    List<SynopticMeasurement> getHottestPlaces() throws NoSuchElementException, HibernateException;

    List<SynopticMeasurement> getColdestPlaces() throws NoSuchElementException, HibernateException;

}
