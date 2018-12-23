package pl.testaarosa.airmeasurements.services;

import org.springframework.core.convert.ConversionFailedException;
import pl.testaarosa.airmeasurements.domain.AirMeasurements;
import pl.testaarosa.airmeasurements.domain.MeasurementsAirLevel;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurements;

import java.time.DateTimeException;
import java.util.List;
import java.util.NoSuchElementException;

public interface GetMeasurementsService {
    List<MeasuringStation> findAll() throws NoSuchElementException;

    List<AirMeasurements> getAirMeasurements(String date) throws NoSuchElementException, DateTimeException;

    List<AirMeasurements> getAirMeasurements(MeasurementsAirLevel measurementsAirLevel) throws RuntimeException;

    List<SynopticMeasurements> getSynopticMeasuremets(String date) throws NoSuchElementException, DateTimeException;

    SynopticMeasurements getHottestPlaceGivenDate(String date) throws DateTimeException, NoSuchElementException;

    SynopticMeasurements getColdestPlaceGivenDate(String date) throws DateTimeException, NoSuchElementException;

    List<SynopticMeasurements> getColdestPlaces() throws NoSuchElementException;

    List<SynopticMeasurements> getHottestPlaces() throws NoSuchElementException;

}
