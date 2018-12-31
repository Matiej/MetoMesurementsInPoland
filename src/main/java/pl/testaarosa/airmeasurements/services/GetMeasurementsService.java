package pl.testaarosa.airmeasurements.services;

import org.hibernate.HibernateException;
import org.springframework.core.convert.ConversionFailedException;
import pl.testaarosa.airmeasurements.domain.AirMeasurements;
import pl.testaarosa.airmeasurements.domain.MeasurementsAirLevel;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurements;

import java.time.DateTimeException;
import java.util.List;
import java.util.NoSuchElementException;

public interface GetMeasurementsService {
    List<MeasuringStation> findAll() throws NoSuchElementException, HibernateException;

    List<AirMeasurements> getAirMeasurements(MeasurementsAirLevel measurementsAirLevel) throws IllegalArgumentException,
            NoSuchElementException, HibernateException;

    List<AirMeasurements> getAirMeasurements(String date) throws NoSuchElementException, DateTimeException, HibernateException;

    List<SynopticMeasurements> getSynopticMeasuremets(String date) throws NoSuchElementException, DateTimeException, HibernateException;

    SynopticMeasurements getHottestPlaceGivenDate(String date) throws DateTimeException, NoSuchElementException, HibernateException;

    SynopticMeasurements getColdestPlaceGivenDate(String date) throws DateTimeException, NoSuchElementException, HibernateException;

    List<SynopticMeasurements> getHottestPlaces() throws NoSuchElementException, HibernateException;

    List<SynopticMeasurements> getColdestPlaces() throws NoSuchElementException, HibernateException;

}
